package com.aitekteam.developer.playmovie.adapters;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aitekteam.developer.playmovie.Item;
import com.aitekteam.developer.playmovie.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private ArrayList<Item> items;
    private MovieHandler handler;

    public MovieAdapter() {
        items = new ArrayList<>();
        handler = null;
    }

    public void setItems(ArrayList<Item> items, MovieHandler handler) {
        this.items = items;
        this.handler = handler;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie_main, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = items.get(position);

        holder.titleItemMovieFull.setText(item.getTitle());
        holder.dateItemMovieFull.setText(item.getDate());
        if (item.getOverview().length() < 100) holder.overviewItemMovieFull.setText(item.getOverview());
        else holder.overviewItemMovieFull.setText(item.getOverview().substring(0, 100));
        Glide.with(holder.itemView).load("https://image.tmdb.org/t/p/w342" + item.getCover()).into(holder.coverItemMovie200);

        if (TextUtils.isEmpty(item.getOverview())) holder.overviewItemMovieFull.setText(R.string.msg_subtitle_not_found);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView titleItemMovieFull, dateItemMovieFull, overviewItemMovieFull;
        ImageView coverItemMovie200;

        ViewHolder(View view) {
            super(view);
            titleItemMovieFull = view.findViewById(R.id.title_item_movie_full);
            dateItemMovieFull = view.findViewById(R.id.date_item_movie_full);
            overviewItemMovieFull = view.findViewById(R.id.overview_item_movie_full);
            coverItemMovie200 = view.findViewById(R.id.cover_item_movie_200);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            handler.onItemClick(getAdapterPosition());
        }
    }

    public interface MovieHandler {
        void onItemClick(int position);
    }
}
