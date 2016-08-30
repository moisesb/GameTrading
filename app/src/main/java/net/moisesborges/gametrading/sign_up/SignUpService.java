package net.moisesborges.gametrading.sign_up;

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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

/**
 * Created by Moisés on 23/08/2016.
 */

public class SignUpService {

    private FirebaseAuth mAuth;

    public SignUpService() {

        mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    Log.d("signIn", firebaseAuth.getCurrentUser().toString());
                } else {
                    logout();
                }
            }
        });
    }

    public boolean isUsernameValid(String userName) {
        return true;
    }

    public boolean isPasswordValid(String password) {
        return Patterns.EMAIL_ADDRESS
                .matcher(password)
                .matches();
    }

    public boolean isEmailValid(String email) {
        return true;
    }

    public void createAccountWithEmail(final String userName, String email, String password, final SignUpCallback callback) {
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
                                            callback.onSuccess();
                                        }
                                    });
                        } else {
                            callback.onError();
                        }
                    }
                });
    }

    public void createAccountWithFacebook(AccessToken accessToken, final SignUpCallback callback) {
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

    public void logout() {
        mAuth.signOut();
        LoginManager.getInstance().logOut();
    }


    public interface SignUpCallback {
        void onSuccess();

        void onError();
    }
}
