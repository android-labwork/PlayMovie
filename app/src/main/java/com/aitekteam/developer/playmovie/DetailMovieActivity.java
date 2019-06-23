package com.aitekteam.developer.playmovie;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aitekteam.developer.playmovie.datas.DBItem;
import com.aitekteam.developer.playmovie.services.StackWidgetService;
import com.bumptech.glide.Glide;

public class DetailMovieActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String EXTRA_OBJECT = "extra_object";
    private ImageView backgroundDetailMovieFull, coverDetailMovie256;
    private TextView titleDetailMovieFull, dateDetailMovieFull, overviewDetailMovieFull;
    private Item items;
    private int argument = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);

        try {
            items = getIntent().getParcelableExtra(EXTRA_OBJECT);
            argument = getIntent().getIntExtra("typeRequest", 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        prepare();
        setData(items);

        getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.getItem(0);
        MenuItem item2 = menu.getItem(1);

        if (argument == 0) {
            item.setVisible(true);
            item2.setVisible(false);
        }
        else {
            item.setVisible(false);
            item2.setVisible(true);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.favorite_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        if (item.getItemId() == R.id.favorite) {
            final ContentValues values = new ContentValues();
            values.put(DBItem.DBItemColumns._TITLE, this.items.getTitle());
            values.put(DBItem.DBItemColumns._OVERVIEW, this.items.getOverview());
            values.put(DBItem.DBItemColumns._DATE, this.items.getDate());
            values.put(DBItem.DBItemColumns._COVER, this.items.getCover());
            values.put(DBItem.DBItemColumns._TYPE, this.items.getType());

            Uri newUri = getContentResolver().insert(DBItem.DBItemColumns.CONTENT_URI, values);
            if (newUri == null)
                Toast.makeText(this, R.string.msg_insert_failed, Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, R.string.msg_insert_success, Toast.LENGTH_SHORT).show();
            argument = 1;
            invalidateOptionsMenu();

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            ComponentName thisWidget = new ComponentName(this, PlayMovieWidge.class);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.stack_view);
            return true;
        }
        else if (item.getItemId() == R.id.delete){
            dialogAction(getResources().getString(R.string.msg_delete),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getContentResolver().delete(DBItem.DBItemColumns.CONTENT_URI,
                                    DBItem.DBItemColumns._TITLE + "=?",
                                    new String[]{items.getTitle()});
                            argument = 0;
                            Toast.makeText(DetailMovieActivity.this, R.string.msg_delete_success, Toast.LENGTH_SHORT).show();
                            invalidateOptionsMenu();
                        }
                    },
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {}
                    });

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            ComponentName thisWidget = new ComponentName(this, PlayMovieWidge.class);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.stack_view);
            return true;
        }
        else if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        else return super.onOptionsItemSelected(item);
    }

    private void dialogAction(String message, DialogInterface.OnClickListener accept,
                              DialogInterface.OnClickListener discard) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.action_yes, accept);
        builder.setNegativeButton(R.string.action_no, discard);

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void prepare() {
        backgroundDetailMovieFull = findViewById(R.id.background_detail_movie_full);
        coverDetailMovie256 = findViewById(R.id.cover_detail_movie_256);
        titleDetailMovieFull = findViewById(R.id.title_detail_movie_full);
        dateDetailMovieFull = findViewById(R.id.date_detail_movie_full);
        overviewDetailMovieFull = findViewById(R.id.overview_detail_movie_full);
    }

    private void setData(Item item) {
        Glide.with(this).load("https://image.tmdb.org/t/p/w500" + item.getCover()).into(backgroundDetailMovieFull);
        Glide.with(this).load("https://image.tmdb.org/t/p/w342" + item.getCover()).into(coverDetailMovie256);
        titleDetailMovieFull.setText(item.getTitle());
        dateDetailMovieFull.setText(item.getDate());
        overviewDetailMovieFull.setText(item.getOverview());
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
                this,
                DBItem.DBItemColumns.CONTENT_URI,
                projection,
                DBItem.DBItemColumns._TITLE+ "=?",
                new String[] {String.valueOf(items.getTitle())},
                null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if (data != null && data.getCount() > 0) argument = 1;
        else argument = 0;

        invalidateOptionsMenu();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {}
}
