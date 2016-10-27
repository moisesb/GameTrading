package net.moisesborges.gametrading.gamesearch;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import net.moisesborges.gametrading.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GameSearchActivity extends AppCompatActivity {

    @BindView(R.id.search)
    SearchView mSearchView;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_search);
        ButterKnife.bind(this);

        setupToolbar();

        setupSearchView();

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        }
    }

    private void setupSearchView() {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                doMySearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                doMySearch(newText);
                return false;
            }
        });

    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void doMySearch(String query) {
        Log.d("GameSearch", "searching for: " + query);
    }

    public static void start(Context context) {
        Intent intent = new Intent(context,GameSearchActivity.class);
        context.startActivity(intent);
    }
}
