package net.moisesborges.gametrading.login.view;

import net.moisesborges.gametrading.mvp.BaseView;

/**
 * Created by Moisés on 23/08/2016.
 */

public interface SignUpView extends BaseView {
    void setProgressIndicator(boolean loading);

    void setInvalidUsername();

    void setInvalidPassword();

    void setInvalidEmail();

    void navigateToDashboard();

    void navigateToLogin();
}
