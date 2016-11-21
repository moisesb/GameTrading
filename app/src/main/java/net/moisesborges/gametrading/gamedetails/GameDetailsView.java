package net.moisesborges.gametrading.gamedetails;

import net.moisesborges.gametrading.mvp.BaseView;

/**
 * Created by moises.anjos on 09/11/2016.
 */

public interface GameDetailsView extends BaseView {
    void setGameCoverImage(String imageUrl);

    void setDescription(String description);

    void setTitle(String title);
}
