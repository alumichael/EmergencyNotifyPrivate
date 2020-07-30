package com.mikelearning.projecttest.getall;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikelearning.projecttest.Constant;
import com.mikelearning.projecttest.model.EmergencyOffice;
import com.mikelearning.projecttest.model.User;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.mikelearning.projecttest.getall.GetOfficesContract.OnGetAllOfficesListener;

public class GetOfficesInteractor implements GetOfficesContract.Interactor {
    private static final String TAG = "GetOfficesInteractor";
    private OnGetAllOfficesListener mOnGetAllOfficesListener;


    public GetOfficesInteractor(OnGetAllOfficesListener onGetAllOfficesListener) {
        this.mOnGetAllOfficesListener = onGetAllOfficesListener;
    }

    @Override
    public void getAllOfficesFromFirebase() {
        FirebaseDatabase.getInstance().getReference().child(Constant.ARG_ORGS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterator<DataSnapshot> dataSnapshots = dataSnapshot.getChildren().iterator();
                List<EmergencyOffice> emergencyOffices = new ArrayList<>();
                while (dataSnapshots.hasNext()) {
                    DataSnapshot dataSnapshotChild = dataSnapshots.next();
                    EmergencyOffice emergencyOffice = dataSnapshotChild.getValue(EmergencyOffice.class);
                    if (!TextUtils.equals(emergencyOffice.getOffice_email(), FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                        emergencyOffices.add(emergencyOffice);
                    }
                }
                mOnGetAllOfficesListener.onGetAllOfficesSuccess(emergencyOffices);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                mOnGetAllOfficesListener.onGetAllOfficesFailure(databaseError.getMessage());

            }
        });

    }
    @Override
    public void getChatOfficesFromFirebase() {
         /*FirebaseDatabase.getInstance().getReference().child(Constants.ARG_CHAT_ROOMS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> dataSnapshots=dataSnapshot.getChildren().iterator();
                List<User> users=new ArrayList<>();
                while (dataSnapshots.hasNext()){
                    DataSnapshot dataSnapshotChild=dataSnapshots.next();
                    dataSnapshotChild.getRef().
                    Chat chat=dataSnapshotChild.getValue(Chat.class);
                    if(chat.)4
                    if(!TextUtils.equals(user.uid,FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        users.add(user);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
    }
}
