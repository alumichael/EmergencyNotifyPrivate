package com.mikelearning.projecttest.actiivties;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import com.mikelearning.projecttest.R;
import com.mikelearning.projecttest.Utils.NetworkConnection;
import com.wang.avi.AVLoadingIndicatorView;

public class ForgetPasswordActivity extends AppCompatActivity implements OnClickListener {
    @BindView(R.id.activity_layout)
    FrameLayout activity_forgot;
    private FirebaseAuth auth;
    @BindView(R.id.btn_back)
    TextView btn_back;
    @BindView(R.id.email_editxt_forget)
    EditText email_editxt_forget;
    @BindView(R.id.forget_pass_btn)
    Button forget_pass_btn;
    @BindView(R.id.avi1)
    AVLoadingIndicatorView progresBar;
    NetworkConnection networkConnection;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);
        networkConnection =new NetworkConnection();
        forget_pass_btn.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        auth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_back) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else if (view.getId() == R.id.forget_pass_btn) {

            if (networkConnection.isNetworkConnected(this)) {
                Snackbar.make(activity_forgot,  "Error Network Connection, Check and Try again", Snackbar.LENGTH_SHORT).show();
            } else if (email_editxt_forget.getText().toString().isEmpty()) {
                Snackbar.make(activity_forgot,"Empty Field", Snackbar.LENGTH_SHORT).show();
            } else {
                resetPassword(email_editxt_forget.getText().toString());
            }
        }
    }

    private void resetPassword(final String email) {
        forget_pass_btn.setVisibility(View.INVISIBLE);
        progresBar.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(email).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progresBar.setVisibility(View.INVISIBLE);
                    View snackBar = activity_forgot;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("We have sent password to email: ");
                    stringBuilder.append(email);
                    Snackbar.make(snackBar, stringBuilder.toString(), Snackbar.LENGTH_SHORT).show();
                    return;
                }
                forget_pass_btn.setVisibility(View.VISIBLE);
                progresBar.setVisibility(View.INVISIBLE);
                Snackbar.make(activity_forgot,  "Failed to send password", Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}
