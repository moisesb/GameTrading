package net.moisesborges.gametrading.sign_in;

import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Patterns;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

/**
 * Created by Moisés on 25/08/2016.
 */
public class SignInService {

    private FirebaseAuth mAuth;

    public SignInService() {
        mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    Log.d("signIn", firebaseAuth.getCurrentUser().getEmail());
                } else {
                    logout();
                }
            }
        });
    }

    public void loginWithEmail(String email, String password, final SignInCallback callback) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            callback.onSuccess();
                        } else {
                            callback.onError();
                        }
                    }
                });
    }

    public void loginWithFacebook(AccessToken accessToken, final SignInCallback callback) {
        AuthCredential authCredential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(authCredential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            callback.onSuccess();
                        } else {
                            callback.onError();
                        }
                    }
                });
    }

    public boolean validateEmail(String email) {
        return Patterns.EMAIL_ADDRESS
                .matcher(email)
                .matches();
    }

    public boolean validatePassword(String password) {
        return password != null && password.length() > 3;
    }

    public void logout() {
        mAuth.signOut();
        LoginManager.getInstance().logOut();
    }

    public interface SignInCallback {
        void onSuccess();

        void onError();
    }
}
