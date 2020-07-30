package com.mikelearning.projecttest.actiivties;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikelearning.projecttest.BaseActivity;
import com.mikelearning.projecttest.Constant;
import com.mikelearning.projecttest.NotificationHome;
import com.mikelearning.projecttest.R;
import com.mikelearning.projecttest.Utils.NetworkConnection;
import com.mikelearning.projecttest.Utils.UserPreferences;
import com.mikelearning.projecttest.model.EmergencyOffice;
import com.mikelearning.projecttest.model.User;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.regex.Pattern;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    FirebaseUser authUser;
    private ProgressDialog dlg;
    @BindView(R.id.login_layout)
    FrameLayout loginLayout;
    @BindView(R.id.email_editxt)
    EditText emailEditxt;
    FirebaseAuth firebaseAuth;
    @BindView(R.id.login_btn)
    Button loginBtn;
    @BindView(R.id.password_editxt)
    EditText passwordEditxt;
    @BindView(R.id.avi1)
    AVLoadingIndicatorView progressView;
    DatabaseReference reference;
    @BindView(R.id.txt_signup)
    TextView txtRegister;
    @BindView(R.id.txt_forget_pass)
    TextView txt_forget_pass;
    @BindView(R.id.usertype_spinner_signin)
    Spinner usertypeSpinner;
    NetworkConnection networkConnection;
    String userTypeString;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        UserPreferences userPreferences = new UserPreferences(this);
        firebaseAuth = FirebaseAuth.getInstance();
        networkConnection=new NetworkConnection();


        userTypeSpinner();

        if (firebaseAuth.getCurrentUser() != null) {
            userPreferences.setUserLogged(true);
            startActivity(new Intent(this, NotificationHome.class));
            finish();

        }else {
            txtRegister.setOnClickListener(this);
            txt_forget_pass.setOnClickListener(this);
            loginBtn.setOnClickListener(this);
        }
    }




    private void userTypeSpinner() {
        // Create an ArrayAdapter using the string array and a default spinner
        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter
                .createFromResource(this, R.array.usertype_array,
                        android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        staticAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        usertypeSpinner.setAdapter(staticAdapter);

        usertypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String stringText = (String) parent.getItemAtPosition(position);
              

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                usertypeSpinner.getItemAtPosition(0);
             


            }
        });

    }



    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.login_btn) {
            
            validateUserInputs();
            
        } else if (id == R.id.txt_forget_pass) {
            startActivity(new Intent(getApplicationContext(), ForgetPasswordActivity.class));
        } else if (id == R.id.txt_signup) {
            startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
        }
    }


    private void validateUserInputs() {

        if (networkConnection.isNetworkConnected(this)) {
            boolean isValid = true;
            userTypeString = usertypeSpinner.getSelectedItem().toString();
            if (userTypeString.equals("Category")) {
                showMessage("Select your category");
                isValid = false;

            }else if (emailEditxt.getText().toString().isEmpty()) {
                showMessage("Email is required!");
                isValid = false;

            } else if (!isValidEmailAddress(emailEditxt.getText().toString())) {
                showMessage("Valid Email is required!");
                isValid = false;

            } else if (passwordEditxt.getText().toString().isEmpty()) {
                showMessage("Password is required");
                isValid = false;

            } else if (passwordEditxt.getText().toString().length() < 6) {
                showMessage("Your Password must be greater than 5 in length");
                isValid = false;

            }


            if (isValid) {
                getUserInput();
            }
           
        }else {
            showMessage("Kindly connect to the Internet.");
        }

    }
    
    private void getUserInput(){
        String email = emailEditxt.getText().toString().toLowerCase();
        String password = passwordEditxt.getText().toString();

        loginBtn.setVisibility(View.GONE);
        progressView.setVisibility(View.VISIBLE);
        
        
        
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener( this, new OnCompleteListener<AuthResult>() {
           
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressView.setVisibility(View.GONE);
                    loginBtn.setVisibility(View.VISIBLE);

                    updateFirebaseToken((task.getResult()).getUser().getEmail(), new UserPreferences(getApplicationContext()).getString(Constant.ARG_FIREBASE_TOKEN, null));
                    setUserData();
                    return;
                }
                progressView.setVisibility(View.GONE);
                loginBtn.setVisibility(View.VISIBLE);
                showMessage("Something went wrong, check the data you entered !");

            }
        });
        new UserPreferences(getApplicationContext()).setUserLogged(true);
        
    }



    private void setUserData() {
        authUser = FirebaseAuth.getInstance().getCurrentUser();

        if (authUser != null) {
            if(userTypeString.equals("Normal-User")){
                reference = FirebaseDatabase.getInstance().getReference(Constant.ARG_USERS);
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshotChild : dataSnapshot.getChildren()) {

                            User user = dataSnapshotChild.getValue(User.class);
                            Log.i("AuthUser",user.getEmail());
                            if (TextUtils.equals(user.getEmail(), FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                                User user2 = new User(user.getUser_type(),user.getFirstname(), user.getLastname(), user.getAddress(), user.getPhone_num(), user.getEmail(), user.getPassword(), user.getNext_of_kin_phone_num(), new UserPreferences(LoginActivity.this).getString(user.getFirebaseToken()));
                                UserPreferences userPreferences= new UserPreferences(getApplicationContext());
                                userPreferences.setUserType(user2.getUser_type());
                                userPreferences.setFirstName(user2.getFirstname());
                                userPreferences.setLastName(user2.getLastname());
                                userPreferences.setPersonalNum(user2.getPhone_num());
                                userPreferences.setNextKinPhoneNum(user2.getNext_of_kin_phone_num());

                                showMessage("Successfully Log in !");
                                Intent intent= new Intent(getApplicationContext(), NotificationHome.class);
                                startActivity(intent);
                                finish();

                            }else{
                                showMessage("Invalid Entry!");
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(LoginActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                reference = FirebaseDatabase.getInstance().getReference(Constant.ARG_ORGS);
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshotChild : dataSnapshot.getChildren()) {

                            EmergencyOffice emergencyOffice = dataSnapshotChild.getValue(EmergencyOffice.class);
                            if (TextUtils.equals(emergencyOffice.getOffice_email(), FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                                EmergencyOffice emergencyOffice2 = new EmergencyOffice(emergencyOffice.getUser_type(), emergencyOffice.getOffice_email(), emergencyOffice.getOffice_organization(), emergencyOffice.getState(), emergencyOffice.getOffice_password(), emergencyOffice.getOffice_phone_num(), new UserPreferences(LoginActivity.this).getString(emergencyOffice.getFirebaseToken()));
                                new UserPreferences(getApplicationContext()).setUserType(emergencyOffice2.getUser_type());



                                Intent intent= new Intent(getApplicationContext(), NotificationHome.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(LoginActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
            
            return;
        }
        Toast.makeText(this, "Null User Data", Toast.LENGTH_LONG).show();
    }

    private static boolean isValidEmailAddress(String email) {
        if (email == null || Pattern.compile("^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z0-9-]{1,8}))?$").matcher(email).matches()) {
            return true;
        }
        return false;
    }

    private void updateFirebaseToken(String email, String token) {
        if(userTypeString.equals("Normal-User")){
            
            FirebaseDatabase.getInstance().getReference().child(Constant.ARG_USERS).child(email.replace(".", ",")).child(Constant.ARG_FIREBASE_TOKEN).setValue(token);
       
        }else{
            FirebaseDatabase.getInstance().getReference().child(Constant.ARG_ORGS).child(email.replace(".", ",")).child(Constant.ARG_FIREBASE_TOKEN).setValue(token);

        }
    }

    private void showMessage(String s) {
        Snackbar.make(loginLayout, s, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
