package com.aitekteam.developer.playmovie.interfaces;

import com.aitekteam.developer.playmovie.models.Movies;
import com.aitekteam.developer.playmovie.models.TVShows;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NetworkAPI {
    // Get Movie List
    @GET("movie?")
    Call<Movies> getMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    // Search Movie
    @GET("movie?")
    Call<Movies> searchMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("query") String query,
            @Query("page") int page
    );

    // Get TV Show List
    @GET("tv?")
    Call<TVShows> getTVShows(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    // Search TV Show
    @GET("tv?")
    Call<TVShows> searchTVShows(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("query") String query,
            @Query("page") int page
    );
}
