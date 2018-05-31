package com.example.mynews.activity;

import android.app.Activity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * ActivityCollector
 *
 * @author ggz
 * @date 2017/12/4
 */
public class ActivityCollector {
    private static final String TAG = "ActivityCollector";

    private static List<Activity> activityList = new ArrayList<>();

    public static void addActivity(Activity activity){
        Log.d(TAG, " -- addActivity() " + activity);
        activityList.add(activity);
    }

    public static void removeActivity(Activity activity){
        Log.d(TAG, " -- removeActivity() " + activity);
        activityList.remove(activity);
    }

    public static void finishAll(){
        Log.d(TAG, " -- finishAll()");
        for (Activity activity : activityList){
            if(!activity.isFinishing()){
                activity.finish();
                Log.d(TAG, "  activity.finish() " + activity);
            }
        }
//        System.exit(0);
    }
}
