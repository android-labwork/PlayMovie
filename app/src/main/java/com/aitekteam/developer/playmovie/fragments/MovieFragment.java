package com.aitekteam.developer.playmovie.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.aitekteam.developer.playmovie.ChangeLanguageActivity;
import com.aitekteam.developer.playmovie.DetailMovieActivity;
import com.aitekteam.developer.playmovie.Item;
import com.aitekteam.developer.playmovie.MovieViewModel;
import com.aitekteam.developer.playmovie.adapters.MovieAdapter;
import com.aitekteam.developer.playmovie.R;

import java.util.ArrayList;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MovieFragment extends Fragment {

    public static MovieFragment getInstance() {
        return new MovieFragment();
    }

    private MovieAdapter movieAdapter;
    private ProgressDialog loading;
    private int argument = 0;
    private SharedPreferences sharedPreferences;
    private MovieViewModel mainViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie, container, false);

        RecyclerView listMainMovieFull = view.findViewById(R.id.list_main_movie_full);
        listMainMovieFull.setLayoutManager(new LinearLayoutManager(getContext()));
        movieAdapter = new MovieAdapter();
        listMainMovieFull.setAdapter(movieAdapter);
        mainViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        sharedPreferences = getContext().getSharedPreferences(ChangeLanguageActivity.SHR_PRF, Context.MODE_PRIVATE);

        if (!sharedPreferences.getString(ChangeLanguageActivity.SHR_PRF, "en-US").equals("en-US"))
            changeLanguage("in");
        else changeLanguage("en");

        if (movieAdapter.getItemCount() == 0)
            prepareData();
        int page = 1;

        if (getArguments() != null){
            argument = getArguments().getInt("typeRequest");
            mainViewModel.getItems(getContext(), getActivity(), argument, getResources()
                .getString(R.string.api_key), sharedPreferences
                .getString(ChangeLanguageActivity.SHR_PRF, "en-US"), page).observe(this, getMovies);
        }

        return view;
    }

    private void prepareData() {
        loading = new ProgressDialog(getContext());
        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loading.setMessage("Harap tunggu sebentar...");
        loading.show();
    }

    private Observer<ArrayList<Item>> getMovies = new Observer<ArrayList<Item>>() {
        @Override
        public void onChanged(final ArrayList<Item> items) {
            movieAdapter.setItems(items, new MovieAdapter.MovieHandler() {
                @Override
                public void onItemClick(int position) {
                    Intent intent = new Intent(getContext(), DetailMovieActivity.class);
                    intent.putExtra(DetailMovieActivity.EXTRA_OBJECT, items.get(position));
                    intent.putExtra("typeRequest", argument);
                    startActivity(intent);
                }
            });

            if (items.size() == 0) Toast.makeText(getContext(), R.string.msg_not_found, Toast.LENGTH_LONG).show();

            movieAdapter.notifyDataSetChanged();
            loading.dismiss();
        }
    };

    @Override
    public void onResume() {
        mainViewModel.getItems(getContext(), getActivity(), argument, getResources()
                .getString(R.string.api_key), sharedPreferences
                .getString(ChangeLanguageActivity.SHR_PRF, "en-US"), 1).observe(this, getMovies);
        super.onResume();
    }

    private void changeLanguage(String lang) {
        Locale locale = new Locale(lang);
        Resources resources = getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, metrics);
    }
}
