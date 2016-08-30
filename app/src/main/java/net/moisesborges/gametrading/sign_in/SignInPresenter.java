package net.moisesborges.gametrading.sign_in;

import com.facebook.AccessToken;

import net.moisesborges.gametrading.mvp.BasePresenter;

/**
 * Created by Moisés on 25/08/2016.
 */

public class SignInPresenter implements BasePresenter<SignInView> {
    private final SignInService mSignInService;
    private SignInView mView;
    private SignInService.SignInCallback mCallback = new SignInService.SignInCallback() {
        @Override
        public void onSuccess() {
            mView.setProgressIndicator(false);
            mView.navigateToDashboard();
        }

        @Override
        public void onError() {
            mView.setProgressIndicator(false);
            mView.showWrongEmailAndPassword();
        }
    };

    public SignInPresenter(SignInService signInService) {
        mSignInService = signInService;
    }


    public void signInWithEmail(String email, String password) {
        if (mView == null) {
            return;
        }

        if (!mSignInService.validateEmail(email)) {
            mView.showInvalidEmail();
            return;
        }

        if (mSignInService.validatePassword(password)) {
            mView.showInvalidPassword();
            return;
        }

        mView.setProgressIndicator(true);

        mSignInService.loginWithEmail(email, password, mCallback);
    }

    public void signInWithFacebook(AccessToken accessToken) {
        if (mView == null) {
            return;
        }

        mView.setProgressIndicator(true);

        mSignInService.loginWithFacebook(accessToken, mCallback);
    }

    @Override
    public void bindView(SignInView view) {
        mView = view;
    }

    @Override
    public void unbindView() {
        mView = null;
    }
}
