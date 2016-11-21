package net.moisesborges.gametrading.gamesearch;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.moisesborges.gametrading.R;
import net.moisesborges.gametrading.adapters.AdapterItemCallback;
import net.moisesborges.gametrading.adapters.GameAdapter;
import net.moisesborges.gametrading.gamedetails.GameDetailsActivity;
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
    SearchView mSearchView;

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
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mSubscription.isUnsubscribed()) {
            subscribeToSearch();
        }
        mPresenter.bindView(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.game_search_menu, menu);
        return true;
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

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        final Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            final String query = intent.getStringExtra(SearchManager.QUERY);
            mSubject.onNext(query);
        }

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

        subscribeToSearch();
    }

    private void subscribeToSearch() {
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
        GameDetailsActivity.start(this, game);
    }
}
