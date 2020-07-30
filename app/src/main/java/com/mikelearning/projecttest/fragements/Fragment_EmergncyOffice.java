package com.mikelearning.projecttest.fragements;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.PowerManager;
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

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.mikelearning.projecttest.Constant;
import com.mikelearning.projecttest.R;
import com.mikelearning.projecttest.SoundSensor.SoundMeter;
import com.mikelearning.projecttest.Utils.NetworkConnection;
import com.mikelearning.projecttest.Utils.UserPreferences;
import com.mikelearning.projecttest.adapter.StatusAdapter;
import com.mikelearning.projecttest.chat.ChatContract;
import com.mikelearning.projecttest.chat.ChatPresenter;
import com.mikelearning.projecttest.events.PushNotificationEvent;
import com.mikelearning.projecttest.model.Chat;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Callback;
import retrofit2.Response;


public class Fragment_EmergncyOffice extends Fragment implements ChatContract.View, StatusAdapter.MessageAdapterListener {


    /** ButterKnife Code **/
    @BindView(R.id.home_layout)
    FrameLayout mHomeLayout;
    @BindView(R.id.emergency_layout)
    LinearLayout mEmergencyLayout;
    @BindView(R.id.recycler_status)
    RecyclerView mRecyclerStatus;
    @BindView(R.id.not_found_layout)
    LinearLayout mNotFoundLayout;
    @BindView(R.id.progress_load_office)
    AVLoadingIndicatorView mProgress;
    /** ButterKnife Code **/

    NetworkConnection networkConnection;

    private ChatPresenter mChatPresenter;

    UserPreferences userPreferences;
    
    StatusAdapter mStatusAdapter;

    ArrayList<Chat> mChatList;




    String message = "Hello, the owner of this phone just got a scholarship";

    String phnNo = "+2349053681466"; //;

    Fragment fragment;

    public Fragment_EmergncyOffice() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Used to record voice

        networkConnection=new NetworkConnection();
        userPreferences=new UserPreferences(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment__emergency_office, container, false);
        ButterKnife.bind(this,view);

        mNotFoundLayout.setVisibility(View.GONE);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // getUsers();
        init();
    }




    private void init() {
        mProgress.setVisibility(View.VISIBLE);
        mRecyclerStatus.setVisibility(View.GONE);
        getNotice();

    }

    private void getNotice(){
        mChatPresenter = new ChatPresenter(this);
        ChatPresenter chatPresenter = mChatPresenter;
        chatPresenter.getMessage(userPreferences.getUserType());
    }




    private void showFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        ft.commit();
    }


    @Override
    public void onGetMessagesFailure(String str) {
        mProgress.setVisibility(View.GONE);
        mRecyclerStatus.setVisibility(View.VISIBLE);
        Toast.makeText(getActivity(), str, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onGetMessagesSuccess(Chat chat) {
        mProgress.setVisibility(View.GONE);
        mRecyclerStatus.setVisibility(View.VISIBLE);
        mChatList=new ArrayList<Chat>();
        if (mStatusAdapter == null) {

            mStatusAdapter = new StatusAdapter(getContext(),mChatList,this );
            mRecyclerStatus.setAdapter(mStatusAdapter);



        }

            mStatusAdapter.add(chat);

            mRecyclerStatus.smoothScrollToPosition(mStatusAdapter.getItemCount() - 1);

        if(mStatusAdapter.getItemCount()==0){

            mNotFoundLayout.setVisibility(View.VISIBLE);

        }else{
            mNotFoundLayout.setVisibility(View.GONE);
        }




    }

    @Override
    public void onSendMessageFailure(String str) {

    }

    @Override
    public void onSendMessageSuccess() {

    }


    public void onPushNotificationEvent(PushNotificationEvent pushNotificationEvent) {
        StatusAdapter status = mStatusAdapter;
        if (status == null || status.getItemCount() == 0) {
            mChatPresenter.getMessage(userPreferences.getUserType());
        }
    }


    @Override
    public void onDataClicked(Chat data) {


        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.fragment_container, Fragment_Locations.newInstance(data.getLatitude(),data.getLongitude(),data.getFirstname()));
        ft.commit();
    }
}
