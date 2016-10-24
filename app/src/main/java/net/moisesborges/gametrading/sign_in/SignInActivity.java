package net.moisesborges.gametrading.sign_in;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import net.moisesborges.gametrading.R;
import net.moisesborges.gametrading.games.GamesActivity;
import net.moisesborges.gametrading.sign_up.SignUpActivity;
import net.moisesborges.gametrading.utils.UiHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Moisés on 25/08/2016.
 */

public class SignInActivity extends AppCompatActivity implements SignInView{

    @BindView(R.id.email_edit_text)
    EditText mEmailEditText;

    @BindView(R.id.password_edit_text)
    EditText mPasswordEditText;

    @BindView(R.id.facebook_sign_in_button)
    LoginButton mFacebookLoginButton;

    private CallbackManager mCallbackManager;

    private SignInPresenter mSignInPresenter;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        ButterKnife.bind(this);

        initPresenter();
        setupFacebookButton();

        mProgressDialog = UiHelper.createProgressDialog(this, R.string.signing_dialog_message);
    }


    private void setupFacebookButton() {
        mCallbackManager = CallbackManager.Factory.create();

        mFacebookLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                mSignInPresenter.signInWithFacebook(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

    }

    private void initPresenter() {
        mSignInPresenter = new SignInPresenter(new SignInService());
        mSignInPresenter.bindView(this);
    }

    @Override
    protected void onDestroy() {
        mSignInPresenter.unbindView();
        super.onDestroy();
    }

    @OnClick(R.id.sign_in_button)
    @SuppressWarnings("unused")
    void onSignInClick() {
        String email = mEmailEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();
        mSignInPresenter.signInWithEmail(email, password);
    }

    @OnClick(R.id.link_signup)
    @SuppressWarnings("unused")
    void onLinkClick() {
        mSignInPresenter.signUp();
    }

    @Override
    public void setProgressIndicator(boolean loading) {
        if (loading) {
            mProgressDialog.show();
        }else {
            mProgressDialog.hide();
        }
    }

    @Override
    public void navigateToDashboard() {
        GamesActivity.start(this);
    }

    @Override
    public void showWrongEmailAndPassword() {

    }

    @Override
    public void showInvalidEmail() {
        mEmailEditText.setError(getResources().getString(R.string.invalid_email_messsage));
    }

    @Override
    public void showInvalidPassword() {
        mPasswordEditText.setError(getResources().getString(R.string.invalid_password_message));
    }

    @Override
    public void navigateToSignUp() {
        SignUpActivity.start(this);
        finish();
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, SignInActivity.class);
        context.startActivity(intent);
    }
}
