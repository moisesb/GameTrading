package net.moisesborges.gametrading.gamesearch;

import net.moisesborges.gametrading.model.Game;
import net.moisesborges.gametrading.mvp.BaseView;

import java.util.List;

/**
 * Created by moises.anjos on 31/10/2016.
 */

public interface GameSearchView extends BaseView {
    void setProgress(boolean loading);

    void showGames(List<Game> games);

    void showNoGamesFound(String query);

    void showEmptyQueryMessage();

    void navigateToGame(Game game);
}
