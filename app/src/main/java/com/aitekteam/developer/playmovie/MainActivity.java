package com.aitekteam.developer.playmovie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.aitekteam.developer.playmovie.adapters.MoviePager;
import com.aitekteam.developer.playmovie.services.StackWidgetService;
import com.google.android.material.tabs.TabLayout;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPreferences = getSharedPreferences(ChangeLanguageActivity.SHR_PRF, Context.MODE_PRIVATE);
        if (!sharedPreferences.getString(ChangeLanguageActivity.SHR_PRF, "en-US").equals("en-US"))
            changeLanguage("in");
        else changeLanguage("en");

        ViewPager pagerMainMovieFull = findViewById(R.id.pager_main_movie_full);
        TabLayout tabMainMovieFull = findViewById(R.id.tab_main_movie_full);

        Bundle bundle = new Bundle();
        bundle.putInt("typeRequest", 0);

        pagerMainMovieFull.setAdapter(new MoviePager(getSupportFragmentManager(), this, bundle));
        tabMainMovieFull.setupWithViewPager(pagerMainMovieFull);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        ComponentName thisWidget = new ComponentName(this, PlayMovieWidge.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.stack_view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.language_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.settings) {
            Intent mIntent = new Intent(this, ChangeLanguageActivity.class);
            startActivity(mIntent);
            return true;
        }
        else if (item.getItemId() == R.id.favorite) {
            Intent mIntent = new Intent(this, FavoriteActivity.class);
            startActivity(mIntent);
            return true;
        }
        else if (item.getItemId() == R.id.search_movie) {
            Intent mIntent = new Intent(this, SearchMovieActivity.class);
            startActivity(mIntent);
            return true;
        }
        else if (item.getItemId() == R.id.search_tv) {
            Intent mIntent = new Intent(this, SearchTVShowActivity.class);
            startActivity(mIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
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
