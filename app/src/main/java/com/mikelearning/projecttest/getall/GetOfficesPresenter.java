package com.mikelearning.projecttest.getall;



import com.mikelearning.projecttest.model.EmergencyOffice;


import java.util.List;

public class GetOfficesPresenter implements GetOfficesContract.Presenter, GetOfficesContract.OnGetAllOfficesListener {
    private GetOfficesContract.View mView;
    private GetOfficesInteractor mGetOfficesInteractor;

    public GetOfficesPresenter(GetOfficesContract.View view) {
        this.mView = view;
        mGetOfficesInteractor = new GetOfficesInteractor(this);
    }

    @Override
    public void getAllOffices() {
        this.mGetOfficesInteractor.getAllOfficesFromFirebase();
    }

    @Override
    public void getChatOffices() {
        this.mGetOfficesInteractor.getChatOfficesFromFirebase();
    }

    @Override
    public void onGetAllOfficesSuccess(List<EmergencyOffice> emergencyOffices) {
        this.mView.onGetAllOfficesSuccess(emergencyOffices);
    }

    @Override
    public void onGetAllOfficesFailure(String message) {
        this.mView.onGetAllOfficesFailure(message);
    }
}
