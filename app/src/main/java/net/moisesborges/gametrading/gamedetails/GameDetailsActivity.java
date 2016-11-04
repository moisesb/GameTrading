package net.moisesborges.gametrading.gamedetails;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import net.moisesborges.gametrading.R;
import net.moisesborges.gametrading.model.Game;

import butterknife.ButterKnife;

/**
 * Created by moises.anjos on 04/11/2016.
 */

public class GameDetailsActivity extends AppCompatActivity {

    private static final String GAME_ARG = "net.moisesborges.gametrading.gamedetails.GameDetailsActivity.mGame";

    private Game mGame;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_details);
        ButterKnife.bind(this);

        mGame = getIntent().getParcelableExtra(GAME_ARG);
        Log.d("GameDetails", mGame + "");
    }



    public static void start(Context context, Game game) {
        if (game == null) {
            throw new IllegalArgumentException("Game cannot be null");
        }

        Intent intent = new Intent(context, GameDetailsActivity.class);
        intent.putExtra(GAME_ARG, game);
        context.startActivity(intent);
    }
}
