package com.mikelearning.projecttest.getall;

import com.mikelearning.projecttest.model.EmergencyOffice;

import java.util.List;

public interface GetOfficesContract {

    interface View {

        void onGetChatOfficesSuccess(List<EmergencyOffice> offices);
        void onGetChatOfficesFailure(String message);

        void onGetAllOfficesFailure(String message);

        void onGetAllOfficesSuccess(List<EmergencyOffice> offices);


    }

    interface Presenter {
        void getAllOffices();

        void getChatOffices();
    }

    interface Interactor {
        void getAllOfficesFromFirebase();

        void getChatOfficesFromFirebase();
    }

    interface OnGetAllOfficesListener {
        void onGetAllOfficesFailure(String message);

        void onGetAllOfficesSuccess(List<EmergencyOffice> offices);
    }

     interface OnGetChatOfficesListener {
        void onGetChatOfficesFailure(String message);

        void onGetChatOfficesSuccess(List<EmergencyOffice> offices);
    }



}
