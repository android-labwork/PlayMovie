package com.aitekteam.developer.playmovie.adapters;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.aitekteam.developer.playmovie.DetailMovieActivity;
import com.aitekteam.developer.playmovie.PlayMovieWidge;
import com.aitekteam.developer.playmovie.R;
import com.aitekteam.developer.playmovie.datas.DBItem;

import java.net.URL;

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private final Context mContext;
    private Cursor cursor;
    private int appWidgetId;

    public StackRemoteViewsFactory(Context mContext, Intent intent) {
        this.mContext = mContext;
        this.appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        if (cursor != null){
            cursor.close();
        }

        final long identityToken = Binder.clearCallingIdentity();

        // querying ke database
        cursor = this.mContext.getContentResolver().query(
                DBItem.DBItemColumns.CONTENT_URI,
                null,
                null,
                null,
                null);

        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {
        if(cursor != null){
            cursor.close();
        }
    }

    @Override
    public int getCount() {
        if(cursor != null){
            return cursor.getCount(); // get item count when cursor is not null
        } else {
            return 0;
        }
    }

    @Override
    public RemoteViews getViewAt(int position) {

        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
        try {
            URL url = new URL("https://image.tmdb.org/t/p/w342" + getSpecificMovieItem(position));
            rv.setImageViewBitmap(R.id.imageView, BitmapFactory.decodeStream(url.openConnection().getInputStream()));
        }catch (Exception e) {
            e.printStackTrace();
        }

        Bundle extras = new Bundle();
        extras.putInt(PlayMovieWidge.EXTRA_ITEM, position);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return cursor.moveToPosition(position) ? cursor.getLong(0) : position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    private String getSpecificMovieItem(int position){
        if(cursor.moveToPosition(position)){
            return cursor.getString(cursor.getColumnIndex(DBItem.DBItemColumns._COVER));
        } else {
            throw new IllegalStateException("The position is invalid!");
        }
    }
}
