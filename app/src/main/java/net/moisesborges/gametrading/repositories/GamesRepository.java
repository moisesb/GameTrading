package net.moisesborges.gametrading.repositories;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import net.moisesborges.gametrading.api.GiantBombApi;
import net.moisesborges.gametrading.model.Game;
import net.moisesborges.gametrading.model.GameSearch;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by moise on 10/08/2016.
 */

public class GamesRepository {

    private GiantBombApi giantBombApi;
    private static final String apiKey = "b38bf4a3804a53a070cf12cbf0f974842cf7fc2e";

    public GamesRepository() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.giantbomb.com/api/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        giantBombApi = retrofit.create(GiantBombApi.class);
    }

    public void getGameByName(String gameName, final Callback callback) {
        Call<GameSearch> call = giantBombApi.listGames(apiKey, "name:" + gameName);

        call.enqueue(new retrofit2.Callback<GameSearch>() {
            @Override
            public void onResponse(Call<GameSearch> call, Response<GameSearch> response) {
                if (response.code() == 200) {
                    GameSearch gameSearch = response.body();
                    callback.onSuccess(gameSearch.getResults());
                } else {
                    callback.onError();
                }
            }

            @Override
            public void onFailure(Call<GameSearch> call, Throwable t) {
                callback.onError();
            }
        });
    }


    public interface Callback {
        void onSuccess(List<Game> games);

        void onError();
    }

}
