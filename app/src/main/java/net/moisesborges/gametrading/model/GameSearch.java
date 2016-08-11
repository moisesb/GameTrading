package net.moisesborges.gametrading.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by moise on 10/08/2016.
 */

public class GameSearch {

    @SerializedName("number_of_total_results")
    private int numOfResults;
    @SerializedName("results")
    private List<Game> results;

    public int getNumOfResults() {
        return numOfResults;
    }

    public void setNumOfResults(int numOfResults) {
        this.numOfResults = numOfResults;
    }

    public List<Game> getResults() {
        return results;
    }

    public void setResults(List<Game> results) {
        this.results = results;
    }
}
