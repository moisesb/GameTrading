package net.moisesborges.gametrading.login.service;

import android.support.annotation.NonNull;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

/**
 * Created by moises.anjos on 24/10/2016.
 */

public class LoginService {

    private static final int MIN_PASSWORD_SIZE = 3;
    private static final int MIN_USERNAME_LENGHT = 3;
    private FirebaseAuth mAuth;

    public LoginService() {
        mAuth = FirebaseAuth.getInstance();
    }

    public boolean isUsernameValid(String userName) {
        return userName != null && userName.length() > MIN_USERNAME_LENGHT;
    }

    public boolean isPasswordValid(String password) {
        return password != null && password.length() > MIN_PASSWORD_SIZE;
    }

    public void logout() {
        mAuth.signOut();
        LoginManager.getInstance().logOut();
    }

    public boolean isLogged() {
        return mAuth.getCurrentUser() != null;
    }

    public boolean isEmailValid(String email) {
        return true;
    }

    public void loginWithEmail(String email, String password, final LoginCallback callback) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            callback.onSuccess();
                            Log.d("Facebook", mAuth.getCurrentUser().getEmail());
                        } else {
                            callback.onError();
                        }
                    }
                });
    }

    public void loginWithFacebook(AccessToken accessToken, final LoginCallback callback) {
        AuthCredential authCredential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(authCredential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            callback.onSuccess();
                        } else {
                            logout();
                            callback.onError();
                        }
                    }
                });
    }

    public void createAccountWithEmail(final String userName, final String email, final String password, final LoginCallback callback) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = task.getResult().getUser();
                            UserProfileChangeRequest changeRequest = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(userName)
                                    .build();
                            user.updateProfile(changeRequest)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            loginWithEmail(email,password,callback);
                                        }
                                    });

                        } else {
                            callback.onError();
                        }
                    }
                });
    }

    public interface LoginCallback {
        void onSuccess();

        void onError();
    }
}
