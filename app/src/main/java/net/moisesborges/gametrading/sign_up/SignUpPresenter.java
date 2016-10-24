package net.moisesborges.gametrading.sign_up;

import com.facebook.AccessToken;

import net.moisesborges.gametrading.mvp.BasePresenter;
import net.moisesborges.gametrading.sign_in.SignInService;

/**
 * Created by Moisés on 23/08/2016.
 */

public class SignUpPresenter extends BasePresenter<SignUpView> {

    private final SignUpService mSignUpService;
    private final SignInService mSignInService;
    private SignUpView mView;

    public SignUpPresenter(SignUpService signUpService, SignInService signInService) {
        mSignUpService = signUpService;
        mSignInService = signInService;
    }


    public void signupWithEmail(String userName, String email, String password) {
        if (mView == null) {
            return;
        }

        if (!mSignUpService.isUsernameValid(userName)) {
            mView.setInvalidUsername();
            return;
        }

        if (!mSignUpService.isPasswordValid(password)) {
            mView.setInvalidPassword();
            return;
        }

        if (!mSignUpService.isEmailValid(email)) {
            mView.setInvalidEmail();
            return;
        }

        mView.setProgressIndicator(true);

        mSignUpService.createAccountWithEmail(userName, email, password, new SignUpService.SignUpCallback() {

            @Override
            public void onSuccess() {
                mView.setProgressIndicator(false);
                mView.navigateToDashboard();
            }

            @Override
            public void onError() {
                mView.setProgressIndicator(false);
            }
        });
    }

    public void signupWithFacebook(AccessToken accessToken) {
        if (mView == null) {
            return;
        }

        mView.setProgressIndicator(true);
        mSignUpService.createAccountWithFacebook(accessToken, new SignUpService.SignUpCallback() {
            @Override
            public void onSuccess() {
                mView.setProgressIndicator(false);
                mView.navigateToDashboard();
            }

            @Override
            public void onError() {
                mView.setProgressIndicator(false);
            }
        });
    }

    @Override
    public void bindView(SignUpView view) {
        mView = view;
    }

    @Override
    public void unbindView() {
        mView = null;
    }

    public void login() {
        if (mView == null) {
            return;
        }

        mView.navigateToLogin();
    }

    public void checkLoggedIn() {
        if (mView == null) {
            return;
        }

        if (mSignInService.isLogged()) {
            mView.navigateToDashboard();
        }
    }
}
