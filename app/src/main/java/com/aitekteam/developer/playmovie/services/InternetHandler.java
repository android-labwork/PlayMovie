package com.aitekteam.developer.playmovie.services;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class InternetHandler {
    public static boolean isConnect(Context context) {
        ConnectivityManager conn =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return conn.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState()
                == NetworkInfo.State.CONNECTED ||
                conn.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState()
                        == NetworkInfo.State.CONNECTED;
    }
}
