package com.aitekteam.developer.playmovie.services;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.aitekteam.developer.playmovie.datas.DBItem;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "playmovies.db";
    private static final int DATABASE_VERSION = 1;


    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_TABLE = "CREATE TABLE " + DBItem.DBItemColumns.TABLE_NAME + " (" +
                DBItem.DBItemColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DBItem.DBItemColumns._TITLE + " TEXT NOT NULL, " +
                DBItem.DBItemColumns._DATE + " TEXT NOT NULL, " +
                DBItem.DBItemColumns._OVERVIEW + " TEXT NOT NULL, " +
                DBItem.DBItemColumns._COVER + " TEXT NOT NULL, " +
                DBItem.DBItemColumns._TYPE + " INTEGER NOT NULL);";

        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
