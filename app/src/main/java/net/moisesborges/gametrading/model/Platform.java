package net.moisesborges.gametrading.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by moises.anjos on 10/08/2016.
 */

public class Platform {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("abbreviation")
    private String abbreviation;

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

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    @Override
    public String toString() {
        return "Platform[id:" + id + ", name:" + name + "]" ;
    }
}
