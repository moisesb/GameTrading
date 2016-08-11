package net.moisesborges.gametrading.games;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import net.moisesborges.gametrading.R;
import net.moisesborges.gametrading.model.Game;
import net.moisesborges.gametrading.repositories.GamesRepository;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GamesActivity extends AppCompatActivity implements GamesView {

    @BindView(R.id.game_name_edit_text)
    EditText mGameNameEditText;

    @BindView(R.id.games_recycler_view)
    RecyclerView mGamesRecyclerView;

    private GameAdapter mAdapter = new GameAdapter(new ArrayList<Game>(0));

    private GamesPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games);
        ButterKnife.bind(this);
        initRecyclerView();

        mPresenter = new GamesPresenter(new GamesRepository());
        mPresenter.bindView(this);
    }

    @Override
    protected void onDestroy() {
        mPresenter.unbindView();
        super.onDestroy();
    }

    private void initRecyclerView() {
        mGamesRecyclerView.setHasFixedSize(true);
        mGamesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mGamesRecyclerView.setAdapter(mAdapter);
    }

    @OnClick(R.id.search_button)
    void onSearchClick() {
        final String gameName = mGameNameEditText.getText().toString();
        mPresenter.searchGames(gameName);
    }

    @Override
    public void showLoading(boolean loading) {

    }

    @Override
    public void showGames(List<Game> games) {
        mAdapter.replaceData(games);
    }

    @Override
    public void showGameNotFound() {

    }

    @Override
    public void clearSearch() {

    }


    static class GameAdapter extends RecyclerView.Adapter<GameAdapter.ViewHolder> {

        private List<Game> mGames;

        public GameAdapter(@NonNull List<Game> games) {
            this.mGames = games;
        }

        public void replaceData(@NonNull List<Game> games) {
            this.mGames = games;
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View layout = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_game_item, parent, false);
            return new ViewHolder(layout);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final Game game = mGames.get(position);
            holder.mGameNameTextView.setText(game.toString());
        }

        @Override
        public int getItemCount() {
            return mGames.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mGameNameTextView;

            public ViewHolder(View itemView) {
                super(itemView);
                mGameNameTextView = (TextView) itemView.findViewById(R.id.game_name_text_view);
            }
        }
    }
}
