package net.moisesborges.gametrading.login.view;

import net.moisesborges.gametrading.mvp.BaseView;

/**
 * Created by Moisés on 25/08/2016.
 */

public interface SignInView extends BaseView {
    void setProgressIndicator(boolean loading);

    void navigateToDashboard();

    void showWrongEmailAndPassword();

    void showInvalidEmail();

    void showInvalidPassword();

    void navigateToSignUp();
}
