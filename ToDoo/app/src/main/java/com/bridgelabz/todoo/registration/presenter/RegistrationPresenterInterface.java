package com.bridgelabz.todoo.registration.presenter;


import com.bridgelabz.todoo.registration.model.RegistrationModel;
import com.bridgelabz.todoo.registration.view.RegistrationInterface;

/**
 * Created by bridgeit on 23/3/17.
 */

public interface RegistrationPresenterInterface extends RegistrationInterface {

    public  void setNewUser(RegistrationModel registrationModel);


}
