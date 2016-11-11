package net.moisesborges.gametrading.gamedetails;

import android.support.annotation.NonNull;

import net.moisesborges.gametrading.model.Game;
import net.moisesborges.gametrading.mvp.BasePresenter;

/**
 * Created by moises.anjos on 09/11/2016.
 */

public class GameDetailsPresenter extends BasePresenter<GameDetailsView> {

    public GameDetailsPresenter() {
    }

    public void loadGameInfo(@NonNull Game game) {
        checkView();

        getView().setGameCoverImage(game.getImage().getImageUrl());
        getView().setDescription(game.getSummary());
    }
}
