package com.aitekteam.developer.playmovie.datas;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import java.io.Serializable;

public class DBItem {
    public static final String CONTENT_AUTHORITY = "com.aitekteam.developer.playmovie";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_URI = "playmovies";
    public static final class DBItemColumns implements BaseColumns, Serializable {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_URI);
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_URI;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_URI;
        public static final String TABLE_NAME = "playmovies";
        public static final String _ID = BaseColumns._ID;
        public static final String _TITLE = "title";
        public static final String _DATE = "date";
        public static final String _OVERVIEW = "overview";
        public static final String _COVER = "cover";
        public static final String _TYPE = "type";
    }
}
