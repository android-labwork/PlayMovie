package com.aitekteam.developer.playmovie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.SearchView;
import android.widget.Toast;

import com.aitekteam.developer.playmovie.adapters.MovieAdapter;

import java.util.ArrayList;
import java.util.Locale;

public class SearchMovieActivity extends AppCompatActivity {

    private MovieAdapter movieAdapter;
    private SearchMovieViewModel movieViewModel;
    private SharedPreferences sharedPreferences;
    private ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_movie);

        RecyclerView listMainMovieFull = findViewById(R.id.list_main_movie_full);
        listMainMovieFull.setLayoutManager(new LinearLayoutManager(this));

        movieAdapter = new MovieAdapter();
        listMainMovieFull.setAdapter(movieAdapter);
        movieViewModel = ViewModelProviders.of(this).get(SearchMovieViewModel.class);
        sharedPreferences = getSharedPreferences(ChangeLanguageActivity.SHR_PRF, Context.MODE_PRIVATE);

        if (!sharedPreferences.getString(ChangeLanguageActivity.SHR_PRF, "en-US").equals("en-US"))
            changeLanguage("in");
        else changeLanguage("en");

        handleIntent(getIntent());
    }

    private void changeLanguage(String lang) {
        Locale locale = new Locale(lang);
        Resources resources = getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, metrics);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doSearch(query);
        }
    }

    private void doSearch(String query) {
        prepareData();
        movieViewModel.getItems(this, getString(R.string.api_key), sharedPreferences
                .getString(ChangeLanguageActivity.SHR_PRF, "en-US"), query, 1)
                .observe(this, getMovies);;
    }

    private void prepareData() {
        loading = new ProgressDialog(this);
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
                    Intent intent = new Intent(SearchMovieActivity.this, DetailMovieActivity.class);
                    intent.putExtra(DetailMovieActivity.EXTRA_OBJECT, items.get(position));
                    intent.putExtra("typeRequest", 0);
                    startActivity(intent);
                }
            });

            if (items.size() == 0) Toast.makeText(SearchMovieActivity.this, R.string.msg_not_found, Toast.LENGTH_LONG).show();

            movieAdapter.notifyDataSetChanged();
            loading.dismiss();
        }
    };
}
