package com.mikelearning.projecttest.chat;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikelearning.projecttest.Constant;
import com.mikelearning.projecttest.Utils.UserPreferences;
import com.mikelearning.projecttest.fcm.FcmNotificationBuilder;
import com.mikelearning.projecttest.model.Chat;

import com.mikelearning.projecttest.chat.ChatContract.OnGetMessagesListener;
import com.mikelearning.projecttest.chat.ChatContract.OnSendMessageListener;

public class ChatInteractor implements ChatContract.Interactor {
    private static final String TAG = "ChatInteractor";
    private OnGetMessagesListener mOnGetMessagesListener;
    private OnSendMessageListener mOnSendMessageListener;
    Context context;

   // public UserPreferences userPreferences=new UserPreferences(context.getApplicationContext());

    public ChatInteractor(OnSendMessageListener onSendMessageListener) {
        this.mOnSendMessageListener = onSendMessageListener;
    }

    public ChatInteractor(OnGetMessagesListener onGetMessagesListener) {
        this.mOnGetMessagesListener = onGetMessagesListener;
    }

    public ChatInteractor(OnSendMessageListener onSendMessageListener, OnGetMessagesListener onGetMessagesListener) {
        this.mOnSendMessageListener = onSendMessageListener;
        this.mOnGetMessagesListener = onGetMessagesListener;

    }

    @Override
    public void sendMessageToFirebaseUser(final Context context, final Chat chat, final String receiverFirebaseToken) {


        final String room_type_1=chat.receiver_type;
      //  final String room_type_2=chat.receiver_type+"_"+chat.senderEmail.replace(".",",");

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child(Constant.ARG_CHAT_ROOMS).getRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(room_type_1)) {
                    Log.e(TAG, "sendMessageToFirebaseUser: " + room_type_1 + " exists");
                    databaseReference.child(Constant.ARG_CHAT_ROOMS).child(room_type_1).child(String.valueOf(chat.timestamp)).setValue(chat);
                } /*else if (dataSnapshot.hasChild(room_type_2)) {
                    Log.e(TAG, "sendMessageToFirebaseUser: " + room_type_2 + " exists");
                    databaseReference.child(Constant.ARG_CHAT_ROOMS).child(room_type_2).child(String.valueOf(chat.timestamp)).setValue(chat);
                }*/ else {
                    Log.e(TAG, "sendMessageToFirebaseUser: success");
                    databaseReference.child(Constant.ARG_CHAT_ROOMS).child(room_type_1).child(String.valueOf(chat.timestamp)).setValue(chat);
                    getMessageFromFirebaseUser(chat.receiver_type);

                    new UserPreferences(context).setSentSuccess(true);


                }
                // send push notification to the receiver
                sendPushNotificationToReceiver(
                        chat.firstname,
                        chat.lastname,
                        chat.nextofkin_phone,
                        chat.message,
                        chat.senderEmail,
                        new UserPreferences(context).getString(Constant.ARG_FIREBASE_TOKEN),
                        receiverFirebaseToken);
                mOnSendMessageListener.onSendMessageSuccess();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                mOnSendMessageListener.onSendMessageFailure("Unable to send message: " + databaseError.getMessage());
            }
        });
    }

    private void sendPushNotificationToReceiver(String firstname,String lastname,String nextKinNum,String message, String email, String firebaseToken, String receiverFirebaseToken) {
        FcmNotificationBuilder.initialize().title("Emergency Notification").firstname(firstname).lastname(lastname).next_kin_num(nextKinNum).message(message).email(email).firebaseToken(firebaseToken).receiverFirebaseToken(receiverFirebaseToken).send();
    }
    @Override
    public void getMessageFromFirebaseUser(String senderType) {
        //receiverEmail=receiverEmail.replace(".",",");

        final String room_type_1 = senderType;
       // final String room_type_2 = receiverEmail + "_" + senderType;

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();


        databaseReference.child(Constant.ARG_CHAT_ROOMS).getRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(room_type_1)) {
                    Log.e(TAG, "getMessageFromFirebaseUser: " + room_type_1 + " exists");
                    FirebaseDatabase.getInstance()
                            .getReference()
                            .child(Constant.ARG_CHAT_ROOMS)
                            .child(room_type_1).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            Chat chat = dataSnapshot.getValue(Chat.class);
                            mOnGetMessagesListener.onGetMessagesSuccess(chat);
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            mOnGetMessagesListener.onGetMessagesFailure("Unable to get message: " + databaseError.getMessage());
                        }
                    });
                } /*else if (dataSnapshot.hasChild(room_type_2)) {
                    Log.e(TAG, "getMessageFromFirebaseUser: " + room_type_2 + " exists");
                    FirebaseDatabase.getInstance()
                            .getReference()
                            .child(Constant.ARG_CHAT_ROOMS)
                            .child(room_type_2).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            Chat chat = dataSnapshot.getValue(Chat.class);
                            mOnGetMessagesListener.onGetMessagesSuccess(chat);
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            mOnGetMessagesListener.onGetMessagesFailure("Unable to get message: " + databaseError.getMessage());
                        }
                    });
                } else {
                    Log.e(TAG, "getMessageFromFirebaseUser: no such room available");
                }*/
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mOnGetMessagesListener.onGetMessagesFailure("Unable to get message: " + databaseError.getMessage());
            }
        });

    }
}
