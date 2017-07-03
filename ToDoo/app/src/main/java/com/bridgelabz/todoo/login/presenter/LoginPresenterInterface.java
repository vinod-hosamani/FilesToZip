package com.bridgelabz.todoo.login.presenter;

import com.bridgelabz.todoo.registration.model.RegistrationModel;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

/**
 * Created by bridgeit on 14/3/17.
 */

public interface LoginPresenterInterface
{
        public  void getLogin(String email, String password);
        public  void getLoginAuthentication(RegistrationModel registrationModel, String uid);

        public  void showProgress();
        public  void closeProgress();
        public  void handleGoogleSignInResult(GoogleSignInAccount account, String uid);
        public void facebookResponceUID(String uid);
}
