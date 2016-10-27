package net.moisesborges.gametrading.login.presenter;

import com.facebook.AccessToken;

import net.moisesborges.gametrading.login.service.LoginService;
import net.moisesborges.gametrading.login.view.SignInView;
import net.moisesborges.gametrading.mvp.BasePresenter;

/**
 * Created by Moisés on 25/08/2016.
 */

public class SignInPresenter extends BasePresenter<SignInView> {
    private final LoginService mLoginService;
    private LoginService.LoginCallback mCallback = new LoginService.LoginCallback() {
        @Override
        public void onSuccess() {
            getView().setProgressIndicator(false);
            getView().navigateToDashboard();
        }

        @Override
        public void onError() {
            getView().setProgressIndicator(false);
            getView().showWrongEmailAndPassword();
        }
    };

    public SignInPresenter(LoginService loginService) {
        mLoginService = loginService;
    }


    public void signInWithEmail(String email, String password) {
        checkView();

        if (!mLoginService.isEmailValid(email)) {
            getView().showInvalidEmail();
            return;
        }

        if (!mLoginService.isPasswordValid(password)) {
            getView().showInvalidPassword();
            return;
        }

        getView().setProgressIndicator(true);

        mLoginService.loginWithEmail(email, password, mCallback);
    }

    public void signInWithFacebook(AccessToken accessToken) {
        checkView();

        getView().setProgressIndicator(true);

        mLoginService.loginWithFacebook(accessToken, mCallback);
    }

    public void signUp() {
        checkView();

        getView().navigateToSignUp();
    }
}
