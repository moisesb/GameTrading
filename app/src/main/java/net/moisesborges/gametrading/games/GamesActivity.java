package net.moisesborges.gametrading.games;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import net.moisesborges.gametrading.R;
import net.moisesborges.gametrading.adapters.AdapterItemCallback;
import net.moisesborges.gametrading.adapters.GameAdapter;
import net.moisesborges.gametrading.login.service.LoginService;
import net.moisesborges.gametrading.login.view.SignInActivity;
import net.moisesborges.gametrading.model.Game;
import net.moisesborges.gametrading.repositories.GamesRepository;
import net.moisesborges.gametrading.session.SessionPresenter;
import net.moisesborges.gametrading.session.SessionView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GamesActivity extends AppCompatActivity implements GamesView, SessionView {

    @BindView(R.id.game_name_edit_text)
    EditText mGameNameEditText;

    @BindView(R.id.games_recycler_view)
    RecyclerView mGamesRecyclerView;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private GamesPresenter mGamesPresenter;
    private SessionPresenter mSessionPresenter;

    private AdapterItemCallback<Game> mOnGameClickCallback = new AdapterItemCallback<Game>() {
        @Override
        public void onClick(Game game) {
            mGamesPresenter.openGame(game.getId());
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
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGamesPresenter = new GamesPresenter(new GamesRepository());
        mGamesPresenter.bindView(this);
        mSessionPresenter = new SessionPresenter(new LoginService());
        mSessionPresenter.bindView(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSessionPresenter.checkIfUserIsLoggedIn();
    }

    @Override
    protected void onDestroy() {
        mSessionPresenter.unbindView();
        mGamesPresenter.unbindView();
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
        mSessionPresenter.loggout();
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
        mGamesPresenter.searchGames(gameName);
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

    @Override
    public void natigateToLogin() {
        SignInActivity.start(this);
        finish();
    }

}
