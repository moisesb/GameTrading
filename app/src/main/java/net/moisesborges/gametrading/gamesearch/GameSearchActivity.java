package net.moisesborges.gametrading.gamesearch;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import net.moisesborges.gametrading.R;
import net.moisesborges.gametrading.adapters.AdapterItemCallback;
import net.moisesborges.gametrading.adapters.GameAdapter;
import net.moisesborges.gametrading.model.Game;
import net.moisesborges.gametrading.repositories.GamesRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

// TODO: 03/11/2016 Navigation to Game details
// TODO: 03/11/2016 improve pagination for games results
public class GameSearchActivity extends AppCompatActivity implements GameSearchView{

    private static final int SEARCH_INTERVAL = 300;

    @BindView(R.id.search_view)
    MaterialSearchView mSearchView;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.games_recycler_view)
    RecyclerView mGamesRecyclerView;

    @BindView(R.id.no_games_found_layout)
    View mNoGamesFoundView;

    @BindView(R.id.loading_progress_bar)
    ProgressBar mProgressBar;

    @BindView(R.id.empty_query_layout)
    View mEmptyQueryView;

    @BindView(R.id.game_not_found_message)
    TextView mGameNotFoundMessage;

    private final Subject<String,String> mSubject = new SerializedSubject<>(PublishSubject.<String>create());
    private Subscription mSubscription;

    private GameSearchPresenter mPresenter;

    AdapterItemCallback<Game> mCallback = new AdapterItemCallback<Game>() {
        @Override
        public void onClick(Game selectedGame) {
            mPresenter.openGame(selectedGame);
        }
    };

    private GameAdapter mAdapter = new GameAdapter(new ArrayList<Game>(0), mCallback);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_search);
        ButterKnife.bind(this);
        setupToolbar();
        setupSearchView();
        setupRecyclerView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initPresenter();
    }

    private void initPresenter() {
        mPresenter = new GameSearchPresenter(new GamesRepository());
        mPresenter.bindView(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.game_search_menu, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        mSearchView.setMenuItem(item);

        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() > 0) {
                String searchWrd = matches.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    mSearchView.setQuery(searchWrd, false);
                }
            }

            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (mSearchView.isSearchOpen()) {
            mSearchView.closeSearch();
        }else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStop() {
        if (!mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
        mPresenter.unbindView();
        super.onStop();
    }

    private void setupSearchView() {
        mSearchView.setVoiceSearch(false);
        mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mSubject.onNext(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mSubject.onNext(newText);
                return false;
            }
        });

        mSubscription = mSubject.debounce(SEARCH_INTERVAL, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        mPresenter.searchGames(s);
                    }
                });
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupRecyclerView() {
        mGamesRecyclerView.setHasFixedSize(true);
        mGamesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mGamesRecyclerView.setAdapter(mAdapter);
    }

    public static void start(Context context) {
        Intent intent = new Intent(context,GameSearchActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void setProgress(boolean loading) {
        mProgressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        if (loading) {
            mNoGamesFoundView.setVisibility(View.GONE);
            mEmptyQueryView.setVisibility(View.GONE);
            mGamesRecyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void showGames(List<Game> games) {
        mNoGamesFoundView.setVisibility(View.GONE);
        mEmptyQueryView.setVisibility(View.GONE);
        mGamesRecyclerView.setVisibility(View.VISIBLE);
        mAdapter.replaceData(games);
    }

    @Override
    public void showNoGamesFound(String query) {
        mGamesRecyclerView.setVisibility(View.GONE);
        mEmptyQueryView.setVisibility(View.GONE);
        mNoGamesFoundView.setVisibility(View.VISIBLE);
        final String message = getResources().getString(R.string.no_games_found_message, query);
        mGameNotFoundMessage.setText(message);
    }

    @Override
    public void showEmptyQueryMessage() {
        mGamesRecyclerView.setVisibility(View.GONE);
        mNoGamesFoundView.setVisibility(View.GONE);
        mEmptyQueryView.setVisibility(View.VISIBLE);
    }

    @Override
    public void navigateToGame(Game game) {
        Log.d("GameSearch", "navigateToGame: " + game.getName());
    }
}
