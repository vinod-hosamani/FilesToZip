package com.bridgelabz.todoo.registration.interactor;


import com.bridgelabz.todoo.registration.model.RegistrationModel;

public interface RegistrationInteractorInterface {

    public  void saveUser(RegistrationModel registrationModel);
    public  void getResponce(boolean flag);
}
