package com.aitekteam.developer.playmovie;

import android.content.Context;
import android.util.Log;

import com.aitekteam.developer.playmovie.interfaces.NetworkAPI;
import com.aitekteam.developer.playmovie.models.Movies;
import com.aitekteam.developer.playmovie.models.TVShows;
import com.aitekteam.developer.playmovie.services.HttpsHandler;

import java.util.ArrayList;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchTVShowViewModel extends ViewModel implements Callback<TVShows> {
    private MutableLiveData<ArrayList<Item>> items;
    private Context context;public MutableLiveData<ArrayList<Item>> getItems(Context context, String API, String language, String query, int page) {
        this.context = context;
        if (items == null) {
            items = new MutableLiveData<>();
        }
        Retrofit retrofitInstance = new Retrofit
                .Builder()
                .baseUrl("https://api.themoviedb.org/3/search/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(HttpsHandler.getInstance().build())
                .build();

        NetworkAPI api = retrofitInstance.create(NetworkAPI.class);
        Call<TVShows> call = api.searchTVShows(API, language, query, page);

        call.enqueue(this);
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
        } else Log.d("SearchTVShow", "Body Error");
    }

    @Override
    public void onFailure(Call<TVShows> call, Throwable t) {
        Log.d("SearchTVShow", "Request Failed");
    }
}
