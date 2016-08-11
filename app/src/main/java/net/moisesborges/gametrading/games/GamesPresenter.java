package net.moisesborges.gametrading.games;

import net.moisesborges.gametrading.model.Game;
import net.moisesborges.gametrading.mvp.BasePresenter;
import net.moisesborges.gametrading.repositories.GamesRepository;

import java.util.List;

/**
 * Created by moises.anjos on 11/08/2016.
 */

public class GamesPresenter implements BasePresenter<GamesView> {

    private final GamesRepository mGamesRepository;
    private GamesView mView;

    public GamesPresenter(GamesRepository gamesRepository) {
        mGamesRepository = gamesRepository;
    }

    public void searchGames(String gameName) {
        if (mView == null) {
            throw new IllegalStateException("Should bing view first");
        }

        if (gameName.isEmpty()) {
            mView.clearSearch();
            return;
        }

        mGamesRepository.getGameByName(gameName, new GamesRepository.Callback() {
            @Override
            public void onSuccess(List<Game> games) {
                if (mView == null) {
                    return;
                }
                if (games.size() > 0) {
                    mView.showGames(games);
                }else {
                    mView.showGameNotFound();
                }
            }

            @Override
            public void onError() {
                if (mView == null) {
                    return;
                }
                mView.showGameNotFound();
            }
        });
    }

    public void openGame(int gameId) {

    }

    @Override
    public void bindView(GamesView view) {
        mView = view;
    }

    @Override
    public void unbindView() {
        mView = null;
    }
}
