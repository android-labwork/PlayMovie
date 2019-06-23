package com.aitekteam.developer.playmovie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.aitekteam.developer.playmovie.adapters.MoviePager;
import com.google.android.material.tabs.TabLayout;

import java.util.Locale;

public class FavoriteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        SharedPreferences sharedPreferences = getSharedPreferences(ChangeLanguageActivity.SHR_PRF, Context.MODE_PRIVATE);
        if (!sharedPreferences.getString(ChangeLanguageActivity.SHR_PRF, "en-US").equals("en-US"))
            changeLanguage("in");
        else changeLanguage("en");

        ViewPager pagerMainMovieFull = findViewById(R.id.pager_main_movie_full);
        TabLayout tabMainMovieFull = findViewById(R.id.tab_main_movie_full);

        Bundle bundle = new Bundle();
        bundle.putInt("typeRequest", 1);

        pagerMainMovieFull.setAdapter(new MoviePager(getSupportFragmentManager(), this, bundle));
        tabMainMovieFull.setupWithViewPager(pagerMainMovieFull);
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
