package net.moisesborges.gametrading.mvp;

/**
 * Created by moises.anjos on 11/08/2016.
 */

public interface BasePresenter<T extends BaseView> {
    void bindView(T view);

    void unbindView();
}
