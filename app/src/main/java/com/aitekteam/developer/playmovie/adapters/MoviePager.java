    package com.aitekteam.developer.playmovie.adapters;

import android.content.Context;
import android.os.Bundle;

import com.aitekteam.developer.playmovie.R;
import com.aitekteam.developer.playmovie.fragments.MovieFragment;
import com.aitekteam.developer.playmovie.fragments.TVShowFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class MoviePager extends FragmentPagerAdapter {

    private Context context;
    private Bundle bundle;

    public MoviePager(@NonNull FragmentManager fm, Context context, Bundle bundle) {
        super(fm);
        this.context = context;
        this.bundle = bundle;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        if (position == 1) fragment = TVShowFragment.getInstance();
        else fragment = MovieFragment.getInstance();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = context.getString(R.string.movie_name);
        if (position == 1) title = context.getString(R.string.tv_show_name);
        return title;
    }
}
