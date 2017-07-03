package com.bridgelabz.todoo.registration.view;


import com.bridgelabz.todoo.registration.model.RegistrationModel;

public interface RegistrationInterface {

    public  void showProgressDialog();
    public  void closeProgressDialog();
    public void getResponce(String uid, RegistrationModel model);
}
