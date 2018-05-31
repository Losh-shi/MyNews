package com.example.mynews.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.mynews.bean.News;
import com.example.mynews.db.DatabaseHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * NewsManager
 *
 * @author ggz
 * @date 2018/2/5
 */

public class NewsManager {
    private static final String TAG = "NewsManager";

    private Context mContext;


    public NewsManager(Context context) {
        this.mContext = context;
    }


    /**
     * 查询数据库的新闻数据
     */
    public List<News> selectDB(String selection) {

        List<News> newsList = new ArrayList<>();

        // 连接数据库
        DatabaseHelper databaseHelper = new DatabaseHelper(mContext);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = null;

        try {
            // 参数：table, columns, selection, selectionArgs, groupBy, having, orderBy, (limit)
            cursor = db.query(DatabaseHelper.TABLE_NAME_NEWS, null, selection, null, null, null, null);

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    int channelId = cursor.getInt(cursor.getColumnIndex("channelId"));
                    int pageToken = cursor.getInt(cursor.getColumnIndex("pageToken"));
                    String id = cursor.getString(cursor.getColumnIndex("id"));
                    String url = cursor.getString(cursor.getColumnIndex("url"));
                    String title = cursor.getString(cursor.getColumnIndex("title"));
                    String content = cursor.getString(cursor.getColumnIndex("content"));
                    String pubName = cursor.getString(cursor.getColumnIndex("pubName"));
                    String pubDate = cursor.getString(cursor.getColumnIndex("pubDate"));
                    String imageUrls = cursor.getString(cursor.getColumnIndex("imageUrls"));
                    int isFavourite = cursor.getInt(cursor.getColumnIndex("isFavourite"));

                    News news = new News(channelId, pageToken, id, url, title, content, pubName, pubDate);

                    if (!imageUrls.equals("null")) {
                        Gson gson = new Gson();
                        List<String> imageList = gson.fromJson(
                                imageUrls,
                                new TypeToken<List<String>>() {
                                }.getType());
                        news.setImageList(imageList);
                    }

                    if (isFavourite == 1) {
                        news.setIsFavourite(true);
                    } else {
                        news.setIsFavourite(false);
                    }

                    newsList.add(news);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            // 关闭数据库
            databaseHelper.close();
            db.close();
        }

        return newsList;
    }

    /**
     * 插入数据库
     */
    public void insertDB(List<News> list) {

        // 连接数据库
        DatabaseHelper databaseHelper = new DatabaseHelper(mContext);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        for (News news : list) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("channelId", news.getChannelId());
            contentValues.put("pageToken", news.getPageToken());
            contentValues.put("id", news.getId());
            contentValues.put("url", news.getUrl());
            contentValues.put("title", news.getTitle());
            contentValues.put("content", news.getContent());
            contentValues.put("pubName", news.getPubName());
            contentValues.put("pubDate", news.getPubDate());
            if (news.getImageList().size() <= 0) {
                contentValues.put("imageUrls", "null");
            } else {
                Gson gson = new Gson();
                String imageUrls = gson.toJson(news.getImageList());
                contentValues.put("imageUrls", imageUrls);
            }
            if (news.getIsFavourite()) {
                contentValues.put("isFavourite", 1);
            } else {
                contentValues.put("isFavourite", 0);
            }

            db.insert(DatabaseHelper.TABLE_NAME_NEWS, null, contentValues);
            contentValues.clear();
        }

        // 关闭数据库
        databaseHelper.close();
        db.close();
    }

    /**
     * 更新数据
     */
    public void updateDB(News news) {
        // 连接数据库
        DatabaseHelper databaseHelper = new DatabaseHelper(mContext);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("channelId", news.getChannelId());
        contentValues.put("pageToken", news.getPageToken());
        contentValues.put("id", news.getId());
        contentValues.put("url", news.getUrl());
        contentValues.put("title", news.getTitle());
        contentValues.put("content", news.getContent());
        contentValues.put("pubName", news.getPubName());
        contentValues.put("pubDate", news.getPubDate());
        if (news.getImageList().size() <= 0) {
            contentValues.put("imageUrls", "null");
        } else {
            Gson gson = new Gson();
            String imageUrls = gson.toJson(news.getImageList());
            contentValues.put("imageUrls", imageUrls);
        }
        if (news.getIsFavourite()) {
            contentValues.put("isFavourite", 1);
        } else {
            contentValues.put("isFavourite", 0);
        }

        // 修改数据，参数：table, contentValues, whereClause, whereArgs
        int err = db.update(DatabaseHelper.TABLE_NAME_NEWS, contentValues,
                "id = ?", new String[]{String.valueOf(news.getId())});
        // 如果没有数据，插入数据
        if (err == 0) {
            db.insert(DatabaseHelper.TABLE_NAME_NEWS, null, contentValues);
        }
        contentValues.clear();

        // 关闭数据库
        databaseHelper.close();
        db.close();
    }

    /**
     * 删除数据
     */
    public void deleteDB(News news) {
        // 连接数据库
        DatabaseHelper databaseHelper = new DatabaseHelper(mContext);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        db.delete(DatabaseHelper.TABLE_NAME_NEWS,
                "id = ?",
                new String[]{String.valueOf(news.getId())});

        // 关闭数据库
        databaseHelper.close();
        db.close();
    }
}
