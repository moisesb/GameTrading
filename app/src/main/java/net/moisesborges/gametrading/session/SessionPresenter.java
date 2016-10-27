package net.moisesborges.gametrading.session;

import net.moisesborges.gametrading.login.service.LoginService;
import net.moisesborges.gametrading.mvp.BasePresenter;

/**
 * Created by moises.anjos on 24/10/2016.
 */

public class SessionPresenter extends BasePresenter<SessionView> {

    private final LoginService mLoginService;

    public SessionPresenter(LoginService loginService) {
        mLoginService = loginService;
    }

    public void checkIfUserIsLoggedIn() {
        checkView();

        if (!mLoginService.isLogged()) {
            getView().natigateToLogin();
        }
    }

    public void loggout() {
        checkView();

        mLoginService.logout();
        getView().natigateToLogin();
    }
}
