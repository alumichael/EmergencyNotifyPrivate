package com.mikelearning.projecttest.fragements;


import android.Manifest;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.NetworkOnMainThreadException;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikelearning.projecttest.Constant;
import com.mikelearning.projecttest.R;
import com.mikelearning.projecttest.SoundSensor.SoundMeter;
import com.mikelearning.projecttest.Utils.NetworkConnection;
import com.mikelearning.projecttest.Utils.UserPreferences;
import com.mikelearning.projecttest.adapter.StatusAdapter;
import com.mikelearning.projecttest.chat.ChatContract;
import com.mikelearning.projecttest.chat.ChatPresenter;
import com.mikelearning.projecttest.getall.GetOfficesContract;
import com.mikelearning.projecttest.getall.GetOfficesPresenter;
import com.mikelearning.projecttest.model.All_Offices;
import com.mikelearning.projecttest.model.Chat;
import com.mikelearning.projecttest.model.EmergencyOffice;
import com.mikelearning.projecttest.model.User;
import com.mikelearning.projecttest.timer_widget.CircularProgressBar;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class Fragment_UserHome extends Fragment implements ChatContract.View, View.OnClickListener, SensorEventListener {


    /** ButterKnife Code **/
    @BindView(R.id.home_layout)
    FrameLayout mHomeLayout;
    @BindView(R.id.user_layout)
    LinearLayout mUserLayout;
    @BindView(R.id.relative_layout_photo)
    FrameLayout mRelativeLayoutPhoto;
    @BindView(R.id.progressbar_timerview)
    ProgressBar mProgressbarTimerview;
    @BindView(R.id.textView_timerview_time)
    TextView mTextViewTimerviewTime;
    @BindView(R.id.progressbar1_timerview)
    ProgressBar mProgressbar1Timerview;
    @BindView(R.id.stop_notification)
    ImageView mStopNotification;
    @BindView(R.id.manual_report_btn)
    MaterialCardView mManualReportBtn;
    @BindView(R.id.avi1)
    com.wang.avi.AVLoadingIndicatorView mAvi1;
    @BindView(R.id.progress_load_office)
    AVLoadingIndicatorView  mProgressLoadOffice;

    /** ButterKnife Code **/

    private int mYear, mMonth, mHour, mMinute,mSecond, mDay;
    private String mDate,mTime;

    //Acelerometer Sensor
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;

    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 2500;

    private ChatPresenter mChatPresenter;

    UserPreferences userPreferences;
    private Calendar mCalendar;
    Double latitude,longitude;




    //Sound Sensor

    /* constants */
    private static final int POLL_INTERVAL = 300;

    /** running state **/
    private boolean mRunning = false;

    /** config state **/
    private int mThreshold;

    private PowerManager.WakeLock mWakeLock;

    private Handler mHandler = new Handler();

    View view;

    /* data source */
    private SoundMeter mSensor;
    double amp;

    NetworkConnection networkConnection;
    FusedLocationProviderClient mFusedLocationClient;


    String message = "Hello, the owner of this phone just got a scholarship";

    String phnNo = "+2349053681466"; //;

    Fragment fragment;
    private Location mLastLocation;



    int i = -1;
    private CountDownTimer countDownTimer;
    private long totalTimeCountInMilliseconds;

    /****************** Define runnable thread again and again detect noise *********/

    private Runnable mSleepTask = new Runnable() {
        public void run() {
            //Log.i("Noise", "runnable mSleepTask");

            start();
        }
    };


    // Create runnable thread to Monitor Voice
    private Runnable mPollTask = new Runnable() {
        public void run() {

            amp = mSensor.getAmplitude();
            //Log.i("Noise", "runnable mPollTask");

            // Runnable(mPollTask) will again execute after POLL_INTERVAL
            mHandler.postDelayed(mPollTask, POLL_INTERVAL);

        }
    };



    public Fragment_UserHome() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       // getOfficesPresenter = new GetOfficesPresenter(this);
        userPreferences=new UserPreferences(getContext());
        mCalendar = Calendar.getInstance();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());

        // Used to record voice
        mSensor = new SoundMeter();

        networkConnection=new NetworkConnection();
        senSensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);


        PowerManager pm = (PowerManager) getContext().getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK, "noise: NoiseAlert");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment__home, container, false);
        ButterKnife.bind(this,view);

        mManualReportBtn.setOnClickListener(this);
        mStopNotification.setOnClickListener(this);

        mAvi1=view.findViewById(R.id.avi1);

        mAvi1.setVisibility(View.GONE);




        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
       // getUsers();
        init();
    }


    private boolean checkPermission(String sendSms) {

        int checkpermission= ContextCompat.checkSelfPermission(getContext(),sendSms);
        return checkpermission== PackageManager.PERMISSION_GRANTED;

    }

    private void init() {

        this.mChatPresenter = new ChatPresenter(this);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        Sensor mySensor = event.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 100) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                float speed = Math.abs(x + y + z - last_x - last_y - last_z)/ diffTime * 10000;

                if (speed > SHAKE_THRESHOLD) {
                   // Toast.makeText(getContext(), "Shakes", Toast.LENGTH_SHORT).show();
                    if ((amp > mThreshold)) {
                        timer();

                    }
                }

                last_x = x;
                last_y = y;
                last_z = z;
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
       // Toast.makeText(getContext(),"Not Accurate",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        initializeApplicationConstants();


        if (!mRunning) {
            mRunning = true;
            start();
        }

    }


    private void setTimer(){
        int time = 60;

        totalTimeCountInMilliseconds =  time * 1000;
        mProgressbar1Timerview.setMax( time * 1000);
    }


    private void startTimer() {
        countDownTimer = new CountDownTimer(totalTimeCountInMilliseconds, 1) {
            @Override
            public void onTick(long leftTimeInMilliseconds) {
                long seconds = leftTimeInMilliseconds / 1000;
                mProgressbar1Timerview.setProgress((int) (leftTimeInMilliseconds));

                mTextViewTimerviewTime.setText(String.format("%02d", seconds / 60)
                        + ":" + String.format("%02d", seconds % 60));
            }

            @Override
            public void onFinish() {

                callForHelp();
                mTextViewTimerviewTime.setText("00:00");
                mTextViewTimerviewTime.setVisibility(View.VISIBLE);
                mStopNotification.setVisibility(View.VISIBLE);
                mProgressbarTimerview.setVisibility(View.VISIBLE);
                mProgressbar1Timerview.setVisibility(View.GONE);

            }
        }.start();
    }




    @Override
    public void onStop() {
        super.onStop();
        stop();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        senSensorManager.unregisterListener(this);
    }


    private void start() {

      /*  if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO},
                    RECORD_AUDIO);
        }*/


        mSensor.start();
        if (!mWakeLock.isHeld()) {
            mWakeLock.acquire();
        }

        //Noise monitoring start
        // Runnable(mPollTask) will execute after POLL_INTERVAL
        mHandler.postDelayed(mPollTask, POLL_INTERVAL);
    }

    private void stop() {
        Log.i("Noise", "==== Stop Noise Monitoring===");
        if (mWakeLock.isHeld()) {
            mWakeLock.release();
        }

        mHandler.removeCallbacks(mSleepTask);
        mHandler.removeCallbacks(mPollTask);
        mSensor.stop();
        mRunning = false;

    }

    private void initializeApplicationConstants() {
        // Set Noise Threshold
        mThreshold = 5;

    }


    private void callForHelp() {
        // Show alert when noise thersold crossed
        Toast.makeText(getContext(), "Calling for Help....",
                Toast.LENGTH_LONG).show();

        try {
            if (networkConnection.isNetworkConnected(getContext())) {

                if(checkPermission(Manifest.permission.SEND_SMS))
                {
                    try {
                 /*   SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phnNo, null, message, null, null);*/
                        Toast.makeText(getContext(), "Sending..", Toast.LENGTH_LONG).show();
                        mManualReportBtn.setVisibility(View.VISIBLE);
                        mAvi1.setVisibility(View.GONE);
                    }catch (Exception o){
                        Toast.makeText(getContext(), "Error Sending", Toast.LENGTH_LONG).show();
                        mManualReportBtn.setVisibility(View.VISIBLE);
                        mAvi1.setVisibility(View.GONE);
                    }



                }
                else {
                    Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    mManualReportBtn.setVisibility(View.VISIBLE);
                    mAvi1.setVisibility(View.GONE);
                }


                mProgressLoadOffice.setVisibility(View.VISIBLE);
                //Send Notification  Online
                sendMessage();



            } else {
                // Show alert when noise thersold crossed
                Toast.makeText(getContext(), "No Network....",
                        Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){

        }

    }


    private void timer(){

        setTimer();


        mStopNotification.setVisibility(View.VISIBLE);
        mProgressbarTimerview.setVisibility(View.INVISIBLE);

        startTimer();
        mProgressbar1Timerview.setVisibility(View.VISIBLE);


    }

    private String getTime(){
        mHour = mCalendar.get(Calendar.HOUR_OF_DAY);
        mMinute = mCalendar.get(Calendar.MINUTE);
        mSecond = mCalendar.get(Calendar.SECOND);
        return  mTime = mHour + ":" + mMinute+ ":"+ mSecond ;
    }

    private String getDate() {

        mYear = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH) + 1;
        mDay = mCalendar.get(Calendar.DATE);
        return mDate = mDay + "/" + mMonth + "/" + mYear;

    }

    private void sendMessage() {

        mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                mLastLocation =location;
                if (mLastLocation!= null) {
                   // latitude=mLastLocation.getLatitude();
                    //longitude=mLastLocation.getLongitude();

                    try {

                        String message = "The person is in emergency";
                        String senderEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                        String firstname = userPreferences.getFirstname();
                        String lastname = userPreferences.getLastname();
                        String phone_num = userPreferences.getPersonalNum();
                        String nextkin_num = userPreferences.getNextKinPhoneNum();
                        String receiverFirebaseToken = "Emergency-Office";



                        Chat chat = new Chat(
                                firstname,
                                lastname,
                                phone_num,
                                nextkin_num,
                                mLastLocation.getLatitude(),
                                mLastLocation.getLongitude(),
                                getDate(),
                                message,
                                "Emergency-Office",
                                senderEmail,
                                getTime());

                        mChatPresenter.sendMessage(getActivity().getApplicationContext(),
                                chat,
                                receiverFirebaseToken);
                    }catch (Exception e){

                        Toast.makeText(getActivity(), "Not Succesfully Oh", Toast.LENGTH_LONG).show();


                    }



                    Log.i("Lat-Long: ", "Long: " + mLastLocation.getLatitude() + " Long: " + mLastLocation.getLongitude());
                }

            }
        });




    }




    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.manual_report_btn){

            mManualReportBtn.setVisibility(View.GONE);
            mAvi1.setVisibility(View.VISIBLE);

            if(checkPermission(Manifest.permission.SEND_SMS))
            {

                try {


                 /*   SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phnNo, null, message, null, null);*/
                    Toast.makeText(getContext(), "Sending..", Toast.LENGTH_LONG).show();
                    mManualReportBtn.setVisibility(View.VISIBLE);
                    mAvi1.setVisibility(View.GONE);
                }catch (Exception o){
                    Toast.makeText(getContext(), "Error Sending", Toast.LENGTH_LONG).show();
                    mManualReportBtn.setVisibility(View.VISIBLE);
                    mAvi1.setVisibility(View.GONE);
                }
            }
            else {
                Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                mManualReportBtn.setVisibility(View.VISIBLE);
                mAvi1.setVisibility(View.GONE);
            }

        }else if(v.getId()==R.id.stop_notification){


            stop();
            countDownTimer.cancel();
            mTextViewTimerviewTime.setText("00:00");
            mProgressbar1Timerview.setVisibility(View.GONE);
            mProgressbarTimerview.setVisibility(View.VISIBLE);
            mStopNotification.setVisibility(View.INVISIBLE);
            getActivity().finish();

        }

    }



    private void showFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        ft.commit();
    }






    @Override
    public void onGetMessagesFailure(String str) {
        mProgressLoadOffice.setVisibility(View.GONE);
    }

    @Override
    public void onGetMessagesSuccess(Chat chat) {
        mProgressLoadOffice.setVisibility(View.GONE);
        Toast.makeText(getContext(), "Get Message Succsful", Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onSendMessageFailure(String str) {
        mProgressLoadOffice.setVisibility(View.GONE);
    }

    @Override
    public void onSendMessageSuccess() {
        mProgressLoadOffice.setVisibility(View.GONE);
        Toast.makeText(getContext(), "Message Sent Succsful", Toast.LENGTH_SHORT).show();
    }


}
