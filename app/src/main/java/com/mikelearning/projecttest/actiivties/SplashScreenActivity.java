package com.mikelearning.projecttest.actiivties;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mikelearning.projecttest.R;
import com.mikelearning.projecttest.Utils.UserPreferences;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashScreenActivity extends AppCompatActivity {
    Animation ButtomUp;
    Animation blink;
    @BindView(R.id.img_logo)
    ImageView imgLogo;
    @BindView(R.id.txt_desc)
    TextView txtDesc;
    @BindView(R.id.txt_version)
    TextView txtVersion;
    Animation zoomIn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ButterKnife.bind( this);
        UserPreferences userPreferences = new UserPreferences(this);

        try{
            PackageInfo packageInfo = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = "Version: " + packageInfo.versionName;
            txtVersion.setText(version);
        }catch (NameNotFoundException e){
            e.printStackTrace();
        }

        ButtomUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_from_buttom);
        blink = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
        imgLogo.startAnimation(this.ButtomUp);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e2) {
            e2.printStackTrace();
        }
        this.txtDesc.startAnimation(this.blink);

            Thread myThread = new Thread() {
                @Override
                public void run() {
                    try {
                        sleep(3000);

                        if (!userPreferences.isFirstTimeLaunch()) {

                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            finish();
                        }else {
                            userPreferences.setFirstTimeLaunch(false);
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            finish();
                        }


                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            myThread.start();

    }
}
