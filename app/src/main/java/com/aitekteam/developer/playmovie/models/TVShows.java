package com.aitekteam.developer.playmovie.models;

import com.aitekteam.developer.playmovie.datas.TVShow;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class TVShows implements Serializable {
    @SerializedName("page")
    private int currentPage;
    @SerializedName("total_pages")
    private int totalPages;
    @SerializedName("results")
    private ArrayList<TVShow> items;

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public ArrayList<TVShow> getItems() {
        return items;
    }

    public void setItems(ArrayList<TVShow> items) {
        this.items = items;
    }
}
