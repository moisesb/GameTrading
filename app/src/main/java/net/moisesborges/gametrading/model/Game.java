package net.moisesborges.gametrading.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * Created by moises.anjos on 10/08/2016.
 */

public class Game {

    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("original_release_date")
    private Date releaseDate;
    private List<Platform> platforms;

    public Game() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public List<Platform> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(List<Platform> platforms) {
        this.platforms = platforms;
    }

    @Override
    public String toString() {
        return "Game[id:" + id + ",name:" + name + ",release_date:" + releaseDate + "]";
    }
}
