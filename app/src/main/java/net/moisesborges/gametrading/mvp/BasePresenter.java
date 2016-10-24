package net.moisesborges.gametrading.mvp;

import java.lang.ref.WeakReference;

/**
 * Created by moises.anjos on 11/08/2016.
 */

public abstract class BasePresenter<T extends BaseView> {

    private WeakReference<T> mViewReference;

    public void checkView() {
        if (!isViewAttached()) {
            throw new IllegalStateException("View should be attached first");
        }
    }

    public boolean isViewAttached() {
        return getView() != null;
    }

    public T getView() {
        return mViewReference.get();
    }

    public void bindView(T view) {
        mViewReference = new WeakReference<>(view);
    }

    public void unbindView() {
        mViewReference.clear();
    }
}
