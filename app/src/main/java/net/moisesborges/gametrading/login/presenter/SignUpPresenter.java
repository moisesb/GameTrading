package net.moisesborges.gametrading.login.presenter;

import com.facebook.AccessToken;

import net.moisesborges.gametrading.login.service.LoginService;
import net.moisesborges.gametrading.login.view.SignUpView;
import net.moisesborges.gametrading.mvp.BasePresenter;

/**
 * Created by Moisés on 23/08/2016.
 */

public class SignUpPresenter extends BasePresenter<SignUpView> {
    private final LoginService mLoginService;
    private final LoginService.LoginCallback mCallback = new LoginService.LoginCallback() {
        @Override
        public void onSuccess() {
            getView().setProgressIndicator(false);
            getView().navigateToDashboard();
        }

        @Override
        public void onError() {
            getView().setProgressIndicator(false);
        }

    };

    public SignUpPresenter(LoginService loginService) {
        mLoginService = loginService;
    }


    public void signupWithEmail(String userName, String email, String password) {
        checkView();

        if (!mLoginService.isUsernameValid(userName)) {
            getView().setInvalidUsername();
            return;
        }

        if (!mLoginService.isPasswordValid(password)) {
            getView().setInvalidPassword();
            return;
        }

        if (!mLoginService.isEmailValid(email)) {
            getView().setInvalidEmail();
            return;
        }

        getView().setProgressIndicator(true);

        mLoginService.createAccountWithEmail(userName, email, password, mCallback);
    }

    public void signupWithFacebook(AccessToken accessToken) {
        checkView();

        getView().setProgressIndicator(true);

        mLoginService.loginWithFacebook(accessToken, mCallback);
    }

    public void checkLoggedIn() {
        checkView();

        if (mLoginService.isLogged()) {
            getView().navigateToDashboard();
        }
    }

    public void login() {
        checkView();

        getView().navigateToLogin();
    }
}
