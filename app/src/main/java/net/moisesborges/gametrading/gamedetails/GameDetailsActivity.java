package net.moisesborges.gametrading.gamedetails;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.moisesborges.gametrading.R;
import net.moisesborges.gametrading.model.Game;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by moises.anjos on 04/11/2016.
 */

public class GameDetailsActivity extends AppCompatActivity implements GameDetailsView {

    private static final String GAME_ARG = "net.moisesborges.gametrading.gamedetails.GameDetailsActivity.mGame";

    @BindView(R.id.game_cover_image_view)
    ImageView mCoverImageView;

    @BindView(R.id.game_description_text_view)
    TextView mDescriptionTextView;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private Game mGame;
    private GameDetailsPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_details);
        ButterKnife.bind(this);

        mGame = getIntent().getParcelableExtra(GAME_ARG);
        setupToolbar();
        setupPresenter();
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        mPresenter.loadGameInfo(mGame);
    }

    @Override
    protected void onStop() {
        super.onStop();

        mPresenter.unbindView();
    }

    private void setupPresenter() {
        mPresenter = new GameDetailsPresenter();
        mPresenter.bindView(this);
    }

    public static void start(Context context, Game game) {
        if (game == null) {
            throw new IllegalArgumentException("Game cannot be null");
        }

        Intent intent = new Intent(context, GameDetailsActivity.class);
        intent.putExtra(GAME_ARG, game);
        context.startActivity(intent);
    }

    @OnClick(R.id.want_game_button)
    @SuppressWarnings("unused")
    void onWantGameClick() {
        mPresenter.changeWantedStatus(mGame);
    }

    @OnClick(R.id.offer_game_button)
    @SuppressWarnings("unused")
    void onOfferGameClick() {
        mPresenter.changeOfferedStatus(mGame);
    }

    @Override
    public void setGameCoverImage(String imageUrl) {
        Picasso.with(this)
                .load(imageUrl)
                .into(mCoverImageView);
    }

    @Override
    public void setDescription(String description) {
        mDescriptionTextView.setText(description);
    }

    @Override
    public void setTitle(String title) {
        getSupportActionBar().setTitle(title);
    }
}
