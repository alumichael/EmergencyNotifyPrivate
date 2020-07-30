package com.mikelearning.projecttest.actiivties;


import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
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

public class RegistrationActivity extends BaseActivity implements View.OnClickListener {




    /** ButterKnife Code **/
    @BindView(R.id.signup_layout)
    FrameLayout signUpLayout;
    @BindView(R.id.usertype_spinner)
    Spinner usertypeSpinner;
    @BindView(R.id.normal_user_form)
    ScrollView normalUserForm;
    @BindView(R.id.firstname_editxt)
    EditText firstnameEditxt;
    @BindView(R.id.lastname_editxt)
    EditText lastnameEditxt;
    @BindView(R.id.email_editxt)
    EditText emailEditxt;
    @BindView(R.id.phone_editxt)
    EditText phoneEditxt;
    @BindView(R.id.address_editxt)
    EditText addressEditxt;
    @BindView(R.id.next_ofkin_phone)
    EditText nextOfKinPhoneEditxt;
    @BindView(R.id.password_editxt)
    EditText passwordEditxt;
    @BindView(R.id.confirm_pass_editxt)
    EditText confirmPassEditxt;
    @BindView(R.id.emergency_office_form)
    ScrollView emergencyOfficeForm;
    @BindView(R.id.organization_editxt)
    EditText organizationEditxt;
    @BindView(R.id.office_email_editxt)
    EditText officeEmailEditxt;
    @BindView(R.id.office_phone_editxt)
    EditText officePhoneEditxt;
    @BindView(R.id.state_spinner)
    Spinner stateSpinner;
    @BindView(R.id.office_password_editxt)
    EditText officePasswordEditxt;
    @BindView(R.id.office_confirm_pass_editxt)
    EditText officeConfirmPassEditxt;
    @BindView(R.id.signup_btn)
    Button signUpBtn;
    @BindView(R.id.avi1)
    AVLoadingIndicatorView progressBar;
    @BindView(R.id.txt_signin)
    TextView txtSignUp;
  
    /** ButterKnife Code **/

    DatabaseReference Userdatabase;
    DatabaseReference Officedatabase;
    FirebaseUser fuser;
    FirebaseAuth firebaseAuth;
    NetworkConnection networkConnection;
    String userTypeString;
    String firstname,lastname,phone_num,nextKinphone_num,address,password,email,emailEncode;
    String organizatnName,ofice_phone_num,stateString,office_password,office_email;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ButterKnife.bind(this);
        networkConnection=new NetworkConnection();
        userTypeSpinner();
        stateSpinner();

        setViewActions();
        firebaseAuth = FirebaseAuth.getInstance();
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
                if(stringText.equals("Normal-User")){
                   normalUserForm.setVisibility(View.VISIBLE);
                   emergencyOfficeForm.setVisibility(View.GONE);
                    normalUserForm.setClickable(true);
                    emergencyOfficeForm.setClickable(false);
                }else if(stringText.equals("Emergency-Office")){
                    normalUserForm.setVisibility(View.GONE);
                    emergencyOfficeForm.setVisibility(View.VISIBLE);
                    normalUserForm.setClickable(false);
                    emergencyOfficeForm.setClickable(true);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                usertypeSpinner.getItemAtPosition(0);
                normalUserForm.setVisibility(View.VISIBLE);
                emergencyOfficeForm.setVisibility(View.GONE);
                normalUserForm.setClickable(true);
                emergencyOfficeForm.setClickable(false);


            }
        });

    }


    private void stateSpinner() {
        // Create an ArrayAdapter using the string array and a default spinner
        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter
                .createFromResource(this, R.array.state_array,
                        android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        staticAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        stateSpinner.setAdapter(staticAdapter);

        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String stringText = (String) parent.getItemAtPosition(position);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                stateSpinner.getItemAtPosition(0);


            }
        });

    }

    private void validateUserInputs() {

        if (networkConnection.isNetworkConnected(this)) {
            boolean isValid = true;
            userTypeString = usertypeSpinner.getSelectedItem().toString();
            stateString = stateSpinner.getSelectedItem().toString();
            

            if(normalUserForm.isClickable()){
                if (userTypeString.equals("Category")) {
                    showMessage("Select your category");
                    isValid = false;
                    
                }else if (emailEditxt.getText().toString().isEmpty()) {
                    showMessage("Email is required!");
                    isValid = false;
                    
                } else if (!isValidEmailAddress(emailEditxt.getText().toString())) {
                    showMessage("Valid Email is required!");
                } else if (firstnameEditxt.getText().toString().isEmpty()) {
                    showMessage("First Name is required");
                    isValid = false;
                    

                }  else if (lastnameEditxt.getText().toString().isEmpty()) {
                    showMessage("Last Name is required");
                    isValid = false;
                    
                } else if (addressEditxt.getText().toString().isEmpty()) {
                    showMessage("Address is required");
                    isValid = false;
                    
                }else if (nextOfKinPhoneEditxt.getText().toString().isEmpty()) {
                    showMessage("Next Kin Phone Number is required");
                    isValid = false;
                    
                }else if (phoneEditxt.getText().toString().isEmpty()) {
                    showMessage("Phone number is required");
                    isValid = false;
                    
                } else if (phoneEditxt.getText().toString().length() < 11) {
                    showMessage("Your Phone Number must not be less than 11 in length");
                    isValid = false;
                    
                } else if (passwordEditxt.getText().toString().isEmpty()) {
                    showMessage("Password is required");
                    isValid = false;
                    
                } else if (passwordEditxt.getText().toString().length() < 6) {
                    showMessage("Your Password must be greater than 5 in length");
                    isValid = false;
                    
                }else if (confirmPassEditxt.getText().toString().isEmpty()) {
                    showMessage("Confirm Password");
                    isValid = false;
                    
                }else if (!confirmPassEditxt.getText().toString().equals(passwordEditxt.getText().toString())) {
                    showMessage("Password not match !");
                    isValid = false;
                    
                }
            }else if(emergencyOfficeForm.isClickable()){

                if (userTypeString.equals("Category")) {
                    showMessage("Select your category");
                    isValid = false;
                    
                }else if (officeEmailEditxt.getText().toString().isEmpty()) {
                    showMessage("Email is required!");
                    isValid = false;
                    
                } else if (!isValidEmailAddress(officeEmailEditxt.getText().toString())) {
                    showMessage("Valid Email is required!");
                    isValid = false;
                    
                }   else if (organizationEditxt.getText().toString().isEmpty()) {
                    showMessage("Organization Name is required");
                    isValid = false;
                    
                } else if (officePhoneEditxt.getText().toString().isEmpty()) {
                    showMessage("Phone Number is required");
                    isValid = false;
                    
                }else if (stateString.equals("Select State")) {
                    showMessage("Select your state");
                    isValid = false;
                    
                }  else if (officePasswordEditxt.getText().toString().isEmpty()) {
                    showMessage("Password is required");
                    isValid = false;
                    
                } else if (officePasswordEditxt.getText().toString().length() < 6) {
                    showMessage("Your Password must be greater than 5 in length");
                    isValid = false;
                    
                }else if (officeConfirmPassEditxt.getText().toString().isEmpty()) {
                    showMessage("Confirm Password");
                    isValid = false;
                    
                }else if (!officeConfirmPassEditxt.getText().toString().equals(officePasswordEditxt.getText().toString())) {
                    showMessage("Password not match !");
                    isValid = false;
                
                }

            }else{
                showMessage("Select your category !");
                isValid = false;
               
            }
           
            
            if (isValid) {
                sendData();
            }
            return;
        }
        showMessage("No Internet connection discovered!");
    }

    private void showMessage(String s) {
        Snackbar.make(signUpLayout, s, Snackbar.LENGTH_LONG).show();
    }

    private void setViewActions() {
        signUpBtn.setOnClickListener(this);
        txtSignUp.setOnClickListener(this);
    }

    private void sendData() {
        signUpBtn.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        if(userTypeString.equals("Normal-User")){

            firstname = firstnameEditxt.getText().toString();
            lastname = lastnameEditxt.getText().toString();
            nextKinphone_num = nextOfKinPhoneEditxt.getText().toString();
            phone_num = phoneEditxt.getText().toString();
            address = addressEditxt.getText().toString();
            email = emailEditxt.getText().toString().toLowerCase();
            password = passwordEditxt.getText().toString();
            emailEncode = email.replace('.', ',');


            Userdatabase = FirebaseDatabase.getInstance().getReference(Constant.ARG_USERS).child(emailEncode);
            Log.d("UserEmail", Userdatabase.toString());
            Userdatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String value = (String) dataSnapshot.getValue(String.class);
                        showMessage("The Email you use already Exist !");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        email.replace(".", ",");
                        final User user = new User(userTypeString,firstname, lastname,address,phone_num,email,password,nextKinphone_num, new UserPreferences(getApplicationContext()).getString(Constant.ARG_FIREBASE_TOKEN));
                        Userdatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (!dataSnapshot.exists()) {
                                    Userdatabase.setValue(user);
                                    showMessage("Registration Completed");
                                    if (firebaseAuth.getCurrentUser() != null) {
                                        progressBar.setVisibility(View.GONE);
                                    }
                                    new UserPreferences(getApplicationContext()).setUserType(userTypeString);

                                    UserPreferences userPreferences= new UserPreferences(getApplicationContext());
                                    userPreferences.setUserType(userTypeString);
                                    userPreferences.setFirstName(firstname);
                                    userPreferences.setLastName(lastname);
                                    userPreferences.setPersonalNum(phone_num);
                                    userPreferences.setNextKinPhoneNum(nextKinphone_num);

                                    Intent intent= new Intent(getApplicationContext(), NotificationHome.class);
                                    startActivity(intent);
                                    finish();

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                signUpBtn.setVisibility(View.VISIBLE);
                            }

                        });
                        new UserPreferences(getApplicationContext()).setUserLogged(true);
                        return;

                    }
                    signUpBtn.setVisibility(View.VISIBLE);
                    showMessage("The Email you use already Exist !");
                }
            });


        }else{

            organizatnName = organizationEditxt.getText().toString();
            office_email = officeEmailEditxt.getText().toString().toLowerCase();
            ofice_phone_num = officePhoneEditxt.getText().toString();
            office_password = officePasswordEditxt.getText().toString();
            emailEncode = office_email.replace('.', ',');


            Officedatabase = FirebaseDatabase.getInstance().getReference(Constant.ARG_ORGS).child(emailEncode);
            Log.d("OfficeUserEmail", Officedatabase.toString());
            Officedatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String value = (String) dataSnapshot.getValue(String.class);
                        showMessage("The Email you use already Exist !");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            firebaseAuth.createUserWithEmailAndPassword(office_email, office_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        office_email.replace(".", ",");
                        final EmergencyOffice office_user = new EmergencyOffice(userTypeString,office_email, organizatnName, stateString, office_password,ofice_phone_num, new UserPreferences(getApplicationContext()).getString(Constant.ARG_FIREBASE_TOKEN));
                        Officedatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (!dataSnapshot.exists()) {
                                    Officedatabase.setValue(office_user);
                                    showMessage("Registration Completed");
                                    if (firebaseAuth.getCurrentUser() != null) {
                                        progressBar.setVisibility(View.GONE);
                                    }
                                    new UserPreferences(getApplicationContext()).setUserType(userTypeString);

                                    Intent intent= new Intent(getApplicationContext(), NotificationHome.class);
                                    startActivity(intent);
                                    finish();

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                signUpBtn.setVisibility(View.VISIBLE);
                            }

                        });
                        new UserPreferences(getApplicationContext()).setUserLogged(true);
                        return;

                    }
                    signUpBtn.setVisibility(View.VISIBLE);
                    showMessage("The Email you use already Exist !");
                }
            });
        }

    }


    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.signup_btn) {
            validateUserInputs();
        }else if(id == R.id.txt_signin){
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        }
    }

    private static boolean isValidEmailAddress(String email) {
        if (email == null || Pattern.compile("^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z0-9-]{1,8}))?$").matcher(email).matches()) {
            return true;
        }
        return false;
    }
}
