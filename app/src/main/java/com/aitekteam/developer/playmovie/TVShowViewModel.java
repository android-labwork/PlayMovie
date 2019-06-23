package com.aitekteam.developer.playmovie;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import com.aitekteam.developer.playmovie.datas.DBItem;
import com.aitekteam.developer.playmovie.interfaces.NetworkAPI;
import com.aitekteam.developer.playmovie.models.TVShows;
import com.aitekteam.developer.playmovie.services.HttpsHandler;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TVShowViewModel extends ViewModel implements Callback<TVShows> ,
        LoaderManager.LoaderCallbacks<Cursor>{
    private MutableLiveData<ArrayList<Item>> items;
    private Context context;
    private Activity activity;

    public MutableLiveData<ArrayList<Item>> getItems(Context context, Activity activity, int argument, String API, String language, int page) {
        this.context = context;
        this.activity = activity;
        if (items == null) {
            items = new MutableLiveData<>();

            if (argument == 0) {
                Retrofit retrofitInstance = new Retrofit
                        .Builder()
                        .baseUrl("https://api.themoviedb.org/3/discover/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(HttpsHandler.getInstance().build())
                        .build();

                NetworkAPI api = retrofitInstance.create(NetworkAPI.class);
                Call<TVShows> call = api.getTVShows(API, language, page);

                call.enqueue(this);
            }
            else {
                activity.getLoaderManager().initLoader(1, null, this);
            }
        }
        return items;
    }

    private void setItems(ArrayList<Item> items) {
        this.items.postValue(items);
    }

    @Override
    public void onResponse(Call<TVShows> call, Response<TVShows> response) {
        if (response.isSuccessful() && response.body() != null) {
            TVShows data = response.body();
            ArrayList<Item> items = new ArrayList<>();
            for (int i = 0; i < data.getItems().size(); i++) {
                Item item = new Item();
                item.setTitle(data.getItems().get(i).getName());
                item.setDate(data.getItems().get(i).getDate());
                item.setOverview(data.getItems().get(i).getOverview());
                item.setCover(data.getItems().get(i).getPosterPath());
                item.setType(1);
                items.add(item);
            }
            setItems(items);
        } else Log.d("MovieFragment", "Body Error");
    }

    @Override
    public void onFailure(Call<TVShows> call, Throwable t) {
        Log.d("TVShowFragment", "Request Failed");
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String[] projection = {
                DBItem.DBItemColumns._ID,
                DBItem.DBItemColumns._TITLE,
                DBItem.DBItemColumns._OVERVIEW,
                DBItem.DBItemColumns._DATE,
                DBItem.DBItemColumns._COVER,
                DBItem.DBItemColumns._TYPE};

        return new CursorLoader(
                this.context,
                DBItem.DBItemColumns.CONTENT_URI,
                projection,
                DBItem.DBItemColumns._TYPE + "=?",
                new String[] {"1"},
                DBItem.DBItemColumns._ID + " DESC");
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        final ArrayList<Item> items = new ArrayList<>();

        if (data != null && data.getCount() > 0) {
            while (data.moveToNext()) {
                Item item = new Item();
                item.setTitle(data.getString(data.getColumnIndex(DBItem.DBItemColumns._TITLE)));
                item.setDate(data.getString(data.getColumnIndex(DBItem.DBItemColumns._DATE)));
                item.setOverview(data.getString(data.getColumnIndex(DBItem.DBItemColumns._OVERVIEW)));
                item.setCover(data.getString(data.getColumnIndex(DBItem.DBItemColumns._COVER)));
                item.setType(data.getInt(data.getColumnIndex(DBItem.DBItemColumns._TYPE)));
                items.add(item);
            }
        }

        setItems(items);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        Log.d("BAB", "Error");
    }
}
