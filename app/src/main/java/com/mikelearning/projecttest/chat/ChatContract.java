package com.mikelearning.projecttest.chat;

import android.content.Context;

import com.mikelearning.projecttest.model.Chat;

public interface ChatContract {


     interface View {
        void onGetMessagesFailure(String str);

        void onGetMessagesSuccess(Chat chat);

        void onSendMessageFailure(String str);

        void onSendMessageSuccess();
    }

    interface Presenter {
        void getMessage(String str);

        void sendMessage(Context context, Chat chat, String str);
    }

    interface Interactor {
        void getMessageFromFirebaseUser(String str);

        void sendMessageToFirebaseUser(Context context, Chat chat, String str);
    }

    interface OnSendMessageListener {
        void onSendMessageFailure(String str);

        void onSendMessageSuccess();
    }


    interface OnGetMessagesListener {
        void onGetMessagesFailure(String str);

        void onGetMessagesSuccess(Chat chat);
    }
}
