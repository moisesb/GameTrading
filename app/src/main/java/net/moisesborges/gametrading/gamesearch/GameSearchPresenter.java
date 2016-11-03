package net.moisesborges.gametrading.gamesearch;

import android.util.Log;

import net.moisesborges.gametrading.model.Game;
import net.moisesborges.gametrading.model.GameSearch;
import net.moisesborges.gametrading.mvp.BasePresenter;
import net.moisesborges.gametrading.repositories.GamesRepository;

import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by moises.anjos on 31/10/2016.
 */

public class GameSearchPresenter extends BasePresenter<GameSearchView> {

    private final GamesRepository mGamesRepository;
    private Subscription mSubscription;

    public GameSearchPresenter(GamesRepository gamesRepository) {
        mGamesRepository = gamesRepository;
    }

    public void searchGames(final String gameName) {
        checkView();

        unSubscribe();

        if (gameName != null && !gameName.trim().isEmpty()) {
            getView().setProgress(true);

            mSubscription = mGamesRepository.queryGamesByName(gameName.trim())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(new Func1<GameSearch, List<Game>>() {
                        @Override
                        public List<Game> call(GameSearch gameSearch) {
                            return gameSearch.getResults();
                        }
                    })
                    .subscribe(new Observer<List<Game>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            getView().setProgress(false);
                            Log.d("GameSearch", e.getClass().getCanonicalName() + "");
                        }

                        @Override
                        public void onNext(List<Game> games) {
                            getView().setProgress(false);

                            if (games.size() > 0) {
                                getView().showGames(games);
                            } else {
                                getView().showNoGamesFound(gameName);
                            }
                        }
                    });

        } else {
            getView().setProgress(false);
            getView().showEmptyQueryMessage();
        }
    }

    @Override
    public void unbindView() {
        unSubscribe();
        super.unbindView();
    }

    private void unSubscribe() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }

    public void openGame(Game game) {
        checkView();

        unSubscribe();

        getView().navigateToGame(game);
    }
}
