package net.moisesborges.gametrading.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by moise on 11/08/2016.
 */
public class Image {

    @SerializedName("icon_url")
    private String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
