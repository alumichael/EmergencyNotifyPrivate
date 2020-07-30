package com.mikelearning.projecttest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.mikelearning.projecttest.Utils.PermissionCheckClass;
import com.mikelearning.projecttest.Utils.UserPreferences;
import com.mikelearning.projecttest.actiivties.LoginActivity;
import com.mikelearning.projecttest.fragements.Fragment_EmergncyOffice;
import com.mikelearning.projecttest.fragements.Fragment_Locations;
import com.mikelearning.projecttest.fragements.Fragment_UserHome;

public class NotificationHome extends AppCompatActivity {


    Fragment fragment;
    String user_flag;
    Toolbar toolBar;
    String type="a";
    LinearLayout mainContent;
    UserPreferences userPreferences;
    PermissionCheckClass mPermissionCheckClass;




    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    select_frame();
                    return true;
                case R.id.navigation_locations:
                    fragment = new Fragment_Locations();
                    showFragmentUser(fragment);

                    return true;
                case R.id.navigation_exit:

                    finish();

                    return true;


            }
            return false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolBar=findViewById(R.id.toolbar);
        mainContent=findViewById(R.id.main_content);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        userPreferences=new UserPreferences(this);

        mPermissionCheckClass = new PermissionCheckClass(this);
        if (!mPermissionCheckClass.checkPermission()){
            mPermissionCheckClass.requestPermission();
        }

        user_flag=userPreferences.getUserType();


            select_frame();

        applyToolbar("Emergency Notifier");

    }

    private void select_frame(){
        if(user_flag.equals("Normal-User")){
            fragment = new Fragment_UserHome();
            showFragmentUser(fragment);
        }else{
            fragment = new Fragment_EmergncyOffice();
            showFragmentUser(fragment);
        }
    }





    private void applyToolbar(String title) {

        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setElevation(0);

    }



    private void showFragmentUser(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        ft.commit();

    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_about) {

           // startActivity(new Intent(NotificationHome.this,AboutActivity.class));

            return true;
        }else if (itemId == R.id.profile) {


            return true;

        }else if (itemId == R.id.action_logout) {

            logout();
            return true;

        } else if (itemId == R.id.action_share) {

            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");

            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, " Download Emergency Notifier Mobile App");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, "http://play.google.com/store/apps/details?id=com.mikelearning.projecttest");

            startActivity(Intent.createChooser(sharingIntent, "Share via"));


            return true;
        } else if (itemId == R.id.action_update) {

            goPlayStore();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        new AlertDialog.Builder(this)
                .setTitle("Log out")
                .setMessage("Are you sure ?")
                .setPositiveButton("Log out", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                            FirebaseAuth.getInstance().signOut();
                            UserPreferences userPreferences = new UserPreferences(NotificationHome.this);
                            userPreferences.setUserLogged(false);
                            Context context = NotificationHome.this;
                            context.startActivity(new Intent(context, LoginActivity.class));
                            finish();
                            Toast.makeText(NotificationHome.this, "Successfully logged out!", Toast.LENGTH_LONG).show();
                            return;
                        }
                        Toast.makeText(NotificationHome.this, "No user logged in yet!", Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {

                        dialog.dismiss();
                    }
                })
                .show();
    }



    public void goPlayStore() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=com.mikelearning.projecttest"));
        startActivity(intent);
    }

    private void showMessage(String s) {
        Snackbar.make(mainContent, s, Snackbar.LENGTH_LONG).show();
    }


    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    protected void onResume() {
        super.onResume();
        EmergencyChatApp.setFeedbackActivityOpen(true);
    }

    protected void onPause() {
        super.onPause();
        EmergencyChatApp.setFeedbackActivityOpen(false);
    }


}
