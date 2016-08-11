package net.moisesborges.gametrading.games;

import net.moisesborges.gametrading.model.Game;
import net.moisesborges.gametrading.mvp.BaseView;

import java.util.List;

/**
 * Created by moises.anjos on 11/08/2016.
 */

public interface GamesView extends BaseView {
    void showLoading(boolean loading);

    void showGames(List<Game> games);

    void showGameNotFound();

    void clearSearch();
}
