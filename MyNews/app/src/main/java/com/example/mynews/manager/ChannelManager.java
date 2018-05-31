package com.example.mynews.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.mynews.bean.Channel;
import com.example.mynews.db.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * ChannelManager
 *
 * @author ggz
 * @date 2017/12/27
 */

public class ChannelManager {
    private static final String TAG = "ChannelManager";
    private static final boolean IS_LOG = true;

    private static List<Channel> defaultUserChannelList;
    private static List<Channel> defaultOtherChannelList;

    private Context mContext;


    public ChannelManager(Context context) {
        super();
        this.mContext = context;

        initDefaultChannelList();

    }

    /**
     * 初始化默认新闻频道
     */
    private void initDefaultChannelList() {
        if (defaultUserChannelList == null) {
            defaultUserChannelList = new ArrayList<>();
            // 参数：id name orderNum selected
            defaultUserChannelList.add(new Channel(1, "社会", 0, 1));
            defaultUserChannelList.add(new Channel(2, "娱乐", 1, 1));
            defaultUserChannelList.add(new Channel(3, "科技", 2, 1));
            defaultUserChannelList.add(new Channel(4, "汽车", 3, 1));
            defaultUserChannelList.add(new Channel(5, "体育", 4, 1));
            defaultUserChannelList.add(new Channel(6, "财经", 5, 1));
            defaultUserChannelList.add(new Channel(7, "军事", 6, 1));
            defaultUserChannelList.add(new Channel(8, "国际", 7, 1));
            defaultUserChannelList.add(new Channel(9, "健康", 8, 1));
            defaultUserChannelList.add(new Channel(10, "房产", 9, 1));
            defaultUserChannelList.add(new Channel(11, "时尚", 10, 1));
            defaultUserChannelList.add(new Channel(12, "育儿", 11, 1));
            defaultUserChannelList.add(new Channel(13, "历史", 12, 1));
        }

        if (defaultOtherChannelList == null) {
            defaultOtherChannelList = new ArrayList<>();
            defaultOtherChannelList.add(new Channel(14, "搞笑", 0, 0));
            defaultOtherChannelList.add(new Channel(15, "数码", 1, 0));
            defaultOtherChannelList.add(new Channel(16, "美食", 2, 0));
            defaultOtherChannelList.add(new Channel(17, "养生", 3, 0));
            defaultOtherChannelList.add(new Channel(18, "电影", 4, 0));
            defaultOtherChannelList.add(new Channel(19, "手机", 5, 0));
            defaultOtherChannelList.add(new Channel(20, "旅游", 6, 0));
            defaultOtherChannelList.add(new Channel(21, "情感", 7, 0));
            defaultOtherChannelList.add(new Channel(22, "家居", 8, 0));
            defaultOtherChannelList.add(new Channel(23, "教育", 9, 0));
            defaultOtherChannelList.add(new Channel(24, "农业", 10, 0));
            defaultOtherChannelList.add(new Channel(25, "孕产", 11, 0));
            defaultOtherChannelList.add(new Channel(26, "文化", 12, 0));
            defaultOtherChannelList.add(new Channel(27, "游戏", 13, 0));
            defaultOtherChannelList.add(new Channel(28, "股票", 14, 0));
            defaultOtherChannelList.add(new Channel(29, "科学", 15, 0));
            defaultOtherChannelList.add(new Channel(30, "动漫", 16, 0));
            defaultOtherChannelList.add(new Channel(31, "故事", 17, 0));
            defaultOtherChannelList.add(new Channel(32, "星座", 18, 0));
        }
    }


    /**
     * 获取我的频道
     */
    public List<Channel> getUserChannelList() {
        // 查询数据库中的新闻频道列表
        List<Channel> userChannelList = selectDB("selected = 1");

        // 获取查询结果。如果有数据，返回 userChannelList
        if (userChannelList.size() > 0) {
            return userChannelList;
        }

        // 没有数据，向数据库存入默认列表，返回 defaultUserChannelList
        insertDB(defaultUserChannelList);
        return defaultUserChannelList;
    }

    /**
     * 获取其他频道
     */
    public List<Channel> getOtherChannelList() {
        List<Channel> otherChannelList = selectDB("selected = 0");

        if (otherChannelList.size() > 0) {
            return otherChannelList;
        }

        insertDB(defaultOtherChannelList);
        return defaultOtherChannelList;
    }


    /**
     * 更新数据库
     */
    public void updateDB(List<Channel> list, int selected) {

        // 连接数据库
        DatabaseHelper databaseHelper = new DatabaseHelper(mContext);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        for (int i = 0; i < list.size(); i++) {
            Channel channel = list.get(i);
            channel.setOrderNum(i);
            channel.setSelected(selected);

            ContentValues contentValues = new ContentValues();
            contentValues.put("orderNum", i);
            contentValues.put("selected", selected);

            // 参数：table, contentValues, whereClause, whereArgs
            db.update(DatabaseHelper.TABLE_NAME_CHANNEL,
                    contentValues,
                    "id = ?",
                    new String[]{String.valueOf(channel.getId())});
            contentValues.clear();
        }

        // 关闭数据库
        databaseHelper.close();
        db.close();
    }


    /**
     * 插入数据库
     */
    private void insertDB(List<Channel> list) {
        // 连接数据库
        DatabaseHelper databaseHelper = new DatabaseHelper(mContext);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        for (Channel channel : list) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("id", channel.getId());
            contentValues.put("name", channel.getName());
            contentValues.put("orderNum", channel.getOrderNum());
            contentValues.put("selected", channel.getSelected());

            db.insert(DatabaseHelper.TABLE_NAME_CHANNEL, null, contentValues);
            contentValues.clear();
        }

        // 关闭数据库
        databaseHelper.close();
        db.close();
    }

    /**
     * 查询数据库的新闻频道数据
     */
    private List<Channel> selectDB(String selection) {

        List<Channel> channelList = new ArrayList<>();

        DatabaseHelper databaseHelper = new DatabaseHelper(mContext);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = null;

        try {
            // 参数：table, columns, selection, selectionArgs, groupBy, having, orderBy, (limit)
            cursor = db.query(DatabaseHelper.TABLE_NAME_CHANNEL,
                    null,
                    selection,
                    null,
                    null,
                    null,
                    "orderNum");
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(cursor.getColumnIndex("id"));
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    int orderNum = cursor.getInt(cursor.getColumnIndex("orderNum"));
                    int selected = cursor.getInt(cursor.getColumnIndex("selected"));

                    if (IS_LOG) {
                        Log.d(TAG, "id " + id + " name " + name + " orderNum " + orderNum
                                + " selected " + selected);
                    }

                    Channel channel = new Channel(id, name, orderNum, selected);
                    channelList.add(channel);
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

        return channelList;
    }
}
