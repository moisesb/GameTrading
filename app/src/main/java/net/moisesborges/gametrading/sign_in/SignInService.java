package net.moisesborges.gametrading.sign_in;

import android.support.annotation.NonNull;
import android.util.Patterns;

import com.facebook.AccessToken;
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

    public interface SignInCallback {
        void onSuccess();

        void onError();
    }
}
