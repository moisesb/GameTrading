package net.moisesborges.gametrading.games;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.EditText;

import net.moisesborges.gametrading.R;
import net.moisesborges.gametrading.model.Game;
import net.moisesborges.gametrading.repositories.GamesRepository;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GamesActivity extends AppCompatActivity {

    @BindView(R.id.game_name_edit_text)
    EditText mGameNameEditText;

    @BindView(R.id.games_recycler_view)
    RecyclerView mGamesRecyclerView;

    GamesRepository mGamesRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games);

        mGamesRepository = new GamesRepository();
        ButterKnife.bind(this);

    }

    @OnClick(R.id.search_button)
    void onSearchClick() {
        final String gameName = mGameNameEditText.getText().toString();
        Log.d("GameSearch", "Searching for " + gameName);
        mGamesRepository.getGameByName(gameName, new GamesRepository.Callback() {
            @Override
            public void onSuccess(List<Game> games) {
                for (Game game : games) {
                    Log.d("GameSearch", game.toString());
                }
            }

            @Override
            public void onError() {
                Log.d("GameSearcj","onError");
            }
        });
    }


}
