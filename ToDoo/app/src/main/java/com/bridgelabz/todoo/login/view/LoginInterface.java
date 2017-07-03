package com.bridgelabz.todoo.login.view;

import com.bridgelabz.todoo.registration.model.RegistrationModel;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;



public interface LoginInterface
{
    public void  loginSuccess(RegistrationModel registrationModel, String userUid);
    public  void handleGoogleSignInResult(GoogleSignInAccount result, String uid);
    public  void loginFailuar();
    public  void showProgress();
    public  void closeProgress();
    public void facebookResponce(String uid);
}


