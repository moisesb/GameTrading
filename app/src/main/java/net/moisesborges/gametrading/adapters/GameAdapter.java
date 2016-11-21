package net.moisesborges.gametrading.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.moisesborges.gametrading.R;
import net.moisesborges.gametrading.games.GamesActivity;
import net.moisesborges.gametrading.model.Game;
import net.moisesborges.gametrading.model.Image;
import net.moisesborges.gametrading.model.Platform;

import java.util.List;

/**
 * Created by moises.anjos on 31/10/2016.
 */

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.ViewHolder> {

    private final AdapterItemCallback<Game> mCallback;
    private List<Game> mGames;

    public GameAdapter(@NonNull List<Game> games, AdapterItemCallback<Game> callback) {
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
        final List<Platform> platforms = game.getPlatforms();
        holder.mPlatformTextView.setText(platforms.get(0).getAbbreviation());
        final Image image = game.getImage();
        if (image != null) {
            Picasso.with(holder.itemView.getContext())
                    .load(image.getImageUrl())
                    .resize(80,80)
                    .into(holder.mCoverImageView);
        }
    }

    @Override
    public int getItemCount() {
        return mGames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final TextView mNameTextView;
        final ImageView mCoverImageView;
        final TextView mPlatformTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mNameTextView = (TextView) itemView.findViewById(R.id.game_name_text_view);
            mCoverImageView = (ImageView) itemView.findViewById(R.id.game_cover_image_view);
            mPlatformTextView = (TextView) itemView.findViewById(R.id.platform_text_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            final int pos = getAdapterPosition();
            final Game game = mGames.get(pos);
            mCallback.onClick(game);
        }
    }
}
