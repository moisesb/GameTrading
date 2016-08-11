package net.moisesborges.gametrading.api;

import net.moisesborges.gametrading.model.Game;
import net.moisesborges.gametrading.model.GameSearch;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by moise on 10/08/2016.
 */

public interface GiantBombApi {

    @GET("games?format=json")
    Call<GameSearch> listGames(@Query("api_key") String apiKey, @Query("filter") String gameName);
}
