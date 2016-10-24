package net.moisesborges.gametrading.sign_in;

import com.facebook.AccessToken;

import net.moisesborges.gametrading.mvp.BasePresenter;

/**
 * Created by Moisés on 25/08/2016.
 */

public class SignInPresenter extends BasePresenter<SignInView> {
    private final SignInService mSignInService;
    private SignInService.SignInCallback mCallback = new SignInService.SignInCallback() {
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

    public SignInPresenter(SignInService signInService) {
        mSignInService = signInService;
    }


    public void signInWithEmail(String email, String password) {
        checkView();

        if (!mSignInService.validateEmail(email)) {
            getView().showInvalidEmail();
            return;
        }

        if (!mSignInService.validatePassword(password)) {
            getView().showInvalidPassword();
            return;
        }

        getView().setProgressIndicator(true);

        mSignInService.loginWithEmail(email, password, mCallback);
    }

    public void signInWithFacebook(AccessToken accessToken) {
        checkView();

        getView().setProgressIndicator(true);

        mSignInService.loginWithFacebook(accessToken, mCallback);
    }

    public void signUp() {
        checkView();

        getView().navigateToSignUp();
    }
}
