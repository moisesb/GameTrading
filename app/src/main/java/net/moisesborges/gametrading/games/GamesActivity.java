package net.moisesborges.gametrading.games;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.moisesborges.gametrading.R;
import net.moisesborges.gametrading.model.Game;
import net.moisesborges.gametrading.repositories.GamesRepository;
import net.moisesborges.gametrading.sign_in.SignInService;

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

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private GamesPresenter mPresenter;

    private OnGameClickCallback mOnGameClickCallback = new OnGameClickCallback() {
        @Override
        public void onClick(Game game) {
           mPresenter.openGame(game.getId());
        }
    };

    private GameAdapter mAdapter = new GameAdapter(new ArrayList<Game>(0), mOnGameClickCallback);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games);
        ButterKnife.bind(this);
        initToolbar();
        initRecyclerView();

        mPresenter = new GamesPresenter(new GamesRepository());
        mPresenter.bindView(this);
    }

    @Override
    protected void onDestroy() {
        mPresenter.unbindView();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                logout();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void logout() {
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
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

    public static void start(Context context) {
        Intent intent = new Intent(context, GamesActivity.class);
        context.startActivity(intent);
    }

    interface OnGameClickCallback {
        void onClick(Game game);
    }

    static class GameAdapter extends RecyclerView.Adapter<GameAdapter.ViewHolder> {

        private final OnGameClickCallback mCallback;
        private List<Game> mGames;

        public GameAdapter(@NonNull List<Game> games, OnGameClickCallback callback) {
            this.mGames = games;
            this.mCallback = callback;
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
            holder.mNameTextView.setText(game.getName());
            holder.mPlatformTextView.setText(game.getPlatforms().get(0).getAbbreviation());
            Picasso.with(holder.itemView.getContext())
                    .load(game.getImage().getImageUrl())
                    .resize(80,80)
                    .into(holder.mCoverImageView);

        }

        @Override
        public int getItemCount() {
            return mGames.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mNameTextView;
            final ImageView mCoverImageView;
            final TextView mPlatformTextView;

            public ViewHolder(View itemView) {
                super(itemView);
                mNameTextView = (TextView) itemView.findViewById(R.id.game_name_text_view);
                mCoverImageView = (ImageView) itemView.findViewById(R.id.game_cover_image_view);
                mPlatformTextView = (TextView) itemView.findViewById(R.id.platform_text_view);
            }
        }
    }
}
