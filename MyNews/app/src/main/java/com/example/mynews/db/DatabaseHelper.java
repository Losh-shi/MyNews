package com.example.mynews.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * DatabaseHelper
 *
 * @author ggz
 * @date 2018/5/6
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "database.db";
    private static final int DB_VERSION = 1;

    public static final String TABLE_NAME_CHANNEL = "channel";
    public static final String TABLE_NAME_NEWS = "news";

    private static final String CREATE_TABLE_CHANNEL =
            "create table if not exists " + TABLE_NAME_CHANNEL
                    + " ( _id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "id INTEGER, "
                    + "name TEXT, "
                    + "orderNum INTEGER, "
                    + "selected INTEGER )";

    private static final String CREATE_TABLE_NEWS =
            "create table if not exists " + TABLE_NAME_NEWS
                    + " ( _id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "channelId INTEGER, "
                    + "pageToken INTEGER, "
                    + "id TEXT, "
                    + "url TEXT, "
                    + "title TEXT, "
                    + "content TEXT, "
                    + "pubName TEXT, "
                    + "pubDate TEXT, "
                    + "imageUrls TEXT, "
                    + "isFavourite INTEGER )";


    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CHANNEL);
        db.execSQL(CREATE_TABLE_NEWS);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_NAME_CHANNEL);
        db.execSQL("drop table if exists " + TABLE_NAME_NEWS);
        onCreate(db);
    }
}
