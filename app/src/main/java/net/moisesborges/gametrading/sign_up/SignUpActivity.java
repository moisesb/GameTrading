package net.moisesborges.gametrading.sign_up;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import net.moisesborges.gametrading.R;
import net.moisesborges.gametrading.games.GamesActivity;
import net.moisesborges.gametrading.sign_in.SignInActivity;
import net.moisesborges.gametrading.utils.UiHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Moisés on 23/08/2016.
 */

public class SignUpActivity extends AppCompatActivity implements SignUpView {

    @BindView(R.id.name_edit_text)
    EditText mUsernameEditText;

    @BindView(R.id.password_edit_text)
    EditText mPasswordEditText;

    @BindView(R.id.email_edit_text)
    EditText mEmailEditText;

    @BindView(R.id.facebook_signup_button)
    LoginButton mFacebookLoginButton;

    private CallbackManager mCallbackManager;

    ProgressDialog mProgressDialog;

    private SignUpPresenter mSignUpPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        setupFacebookLoginButton();

        mProgressDialog = UiHelper.createProgressDialog(this, R.string.creating_account_dialog_message);
    }

    private void setupFacebookLoginButton() {
        mCallbackManager = CallbackManager.Factory.create();

        mFacebookLoginButton.setReadPermissions("email");
        mFacebookLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                mSignUpPresenter.signupWithFacebook(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d("onCancel", "cancelled");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("onError", error.toString());
            }
        });
    }

    @OnClick(R.id.signup_button)
    @SuppressWarnings("unused")
    void onSignUpClick() {
        String userName = mUsernameEditText.getText().toString();
        String email = mEmailEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();
        mSignUpPresenter.signupWithEmail(userName, email, password);
    }

    @OnClick(R.id.link_login)
    @SuppressWarnings("unused")
    void onLinkClick() {
        mSignUpPresenter.login();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mSignUpPresenter = new SignUpPresenter(new SignUpService());
        mSignUpPresenter.bindView(this);
    }

    @Override
    protected void onDestroy() {
        mSignUpPresenter.unbindView();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void setProgressIndicator(boolean loading) {
        if (loading) {
            mProgressDialog.show();
        } else {
            mProgressDialog.hide();
        }
    }

    @Override
    public void setInvalidUsername() {

    }

    @Override
    public void setInvalidPassword() {

    }

    @Override
    public void setInvalidEmail() {
        String errorMessage = getResources().getString(R.string.invalid_email_messsage);
        mEmailEditText.setError(errorMessage);
    }

    @Override
    public void navigateToDashboard() {
        GamesActivity.start(this);
        finish();
    }

    @Override
    public void navigateToLogin() {
        SignInActivity.start(this);
        finish();
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, SignUpActivity.class);
        context.startActivity(intent);
    }
}
