package com.example.mynews.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bumptech.glide.Glide;
import com.example.mynews.activity.MainActivity;
import com.example.mynews.R;
import com.example.mynews.bean.Location;
import com.example.mynews.bean.Weather;
import com.example.mynews.bean.weather.Air;
import com.example.mynews.bean.weather.Forecast;
import com.example.mynews.bean.weather.Lifestyle;
import com.example.mynews.util.GetJsonDataUtil;
import com.example.mynews.http.WeatherHttp;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.example.mynews.activity.MainActivity.KEY_AIR;
import static com.example.mynews.activity.MainActivity.KEY_WEATHER;

/**
 * WeatherFragment
 *
 * @author ggz
 * @date 2018/1/14
 */

public class WeatherFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "WeatherFragment";

    View mView;
    MainActivity mActivity;

    /**
     * 侧滑菜单栏
     */
    private DrawerLayout mDrawerLayout;

    /**
     * 下拉刷新控件
     */
    private RefreshLayout mRefreshLayout;

    /**
     * 省-市-县 数据
     */
    private ArrayList<Location> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();

    /**
     * PickerView 控件参数
     */
    private int mOptions1 = 0;
    private int mOptions2 = 0;
    private int mOptions3 = 0;
    private String province, city, county;

    /**
     * Weather 实体类对象
     */
    private Weather mWeather;
    private String mWeatherJson;

    private Air mAir;
    private String mAirJson;


    /**
     * 数据显示控件
     */
    private TextView mWeatherCountyNameTv;
    private TextView mWeatherTmpTv;
    private TextView mWeatherUpdateTimeTv;
    private TextView mWeatherCondTv;
    private TextView mWeatherWindTv;
    private TextView mWeatherAqiTv;
    private TextView mWeatherPm25Tv;
    private TextView mWeatherSuggestTv;
    private LinearLayout mWeatherForecastLl;
    private ImageView mWeatherCondIv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_weather, container, false);
        mActivity = (MainActivity) getActivity();

        initView(mView);

        initData();

        return mView;
    }

    /**
     * 初始化控件
     */
    private void initView(View v) {
        // ToDo 背景图片，必应每日一图
        ImageView weatherBgIv = v.findViewById(R.id.iv_weather_bg);
        Glide.with(this).load(R.drawable.weather_bg).into(weatherBgIv);

        // 左侧菜单栏
        mDrawerLayout = mActivity.findViewById(R.id.drawer_layout);
        ImageView weatherCategoryIv = v.findViewById(R.id.iv_weather_category);
        weatherCategoryIv.setOnClickListener(this);

        // 地区切换
        TextView weatherSwitchoverTv = v.findViewById(R.id.tv_weather_switchover);
        weatherSwitchoverTv.setOnClickListener(this);

        // 下拉刷新
        mRefreshLayout = v.findViewById(R.id.smart_refresh_layout_weather);
        mRefreshLayout.setOnRefreshListener(mOnRefreshListener);

        // 数据显示控件
        mWeatherCountyNameTv = v.findViewById(R.id.tv_weather_county);
        mWeatherCondIv = v.findViewById(R.id.iv_weather_cond);
        mWeatherCondTv = v.findViewById(R.id.tv_weather_cond);
        mWeatherTmpTv = v.findViewById(R.id.tv_weather_degree);
        mWeatherUpdateTimeTv = v.findViewById(R.id.tv_weather_updatetime);
        mWeatherWindTv = v.findViewById(R.id.tv_weather_wind);
        mWeatherAqiTv = v.findViewById(R.id.tv_weather_aqi);
        mWeatherPm25Tv = v.findViewById(R.id.tv_weather_pm25);
        mWeatherForecastLl = v.findViewById(R.id.ll_weather_forecast);
        mWeatherSuggestTv = v.findViewById(R.id.tv_weather_suggest);
    }

    /**
     * 点击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_weather_category:
                // 打开左侧导航页
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.tv_weather_switchover:
                // 切换地区 showPickerView
                showPickerView();
                break;
            default:
        }
    }

    /**
     * 初始化数据
     */
    private void initData() {
        // 从 SharedPreferences 读取 weather 数据
        SharedPreferences sp = mActivity.getPreferences(Context.MODE_PRIVATE);
        mWeatherJson = sp.getString(KEY_WEATHER, null);
        if (mWeatherJson != null) {
            mWeather = new Gson().fromJson(mWeatherJson, Weather.class);
            String status = mWeather.getStatus();
            if (status.equals("ok")) {
                updateUIData();
                province = mWeather.getBasic().getProvince();
                city = mWeather.getBasic().getCity();
                county = mWeather.getBasic().getCounty();
            }
        } else {
            province = "广东省";
            city = "广州市";
            county = "花都区";
            mRefreshLayout.autoRefresh();
        }

        mAirJson = sp.getString(KEY_AIR, null);
        if (mAirJson != null) {
            mAir = new Gson().fromJson(mAirJson, Air.class);
            mWeatherAqiTv.setText(mAir.getAqi());
            mWeatherPm25Tv.setText(mAir.getPm25());
        }

        // 初始化 PickerView
        String pickerViewOptions = sp.getString("pickerViewOptions", null);
        if (pickerViewOptions != null) {
            String[] options = pickerViewOptions.split(",");
            mOptions1 = Integer.parseInt(options[0]);
            mOptions2 = Integer.parseInt(options[1]);
            mOptions3 = Integer.parseInt(options[2]);
        }
        initPickerViewData();
    }

    /**
     * 下拉刷新监听器
     */
    private OnRefreshListener mOnRefreshListener = new OnRefreshListener() {
        @Override
        public void onRefresh(RefreshLayout refreshlayout) {
            // 和风 Api 平台地址
            String address = WeatherHttp.WEATHER_API + county + WeatherHttp.KEY;
            Log.d(TAG, "address " + address);

            // OkHttp 请求
            WeatherHttp.sendRequestWithOkHttp(address, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();

                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mRefreshLayout.finishRefresh(false);
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String body = response.body().string();

                    try {
                        JSONObject jsonObject = new JSONObject(body);
                        String heWeather = jsonObject.getString("HeWeather6");

                        JSONArray jsonArray = new JSONArray(heWeather);
                        String jsonStr = jsonArray.getJSONObject(0).toString();
                        // 用于 sp 存储
                        mWeatherJson = jsonStr;
                        Log.d(TAG, jsonStr + "\n");

                        // Gson 生成实体类
                        mWeather = new Gson().fromJson(jsonStr, Weather.class);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // 如果数据正确
                            String status = mWeather.getStatus();
                            if (status.equals("ok")) {
                                // 存入 SharedPreferences
                                SharedPreferences.Editor editor =
                                        mActivity.getPreferences(Context.MODE_PRIVATE).edit();
                                editor.putString(KEY_WEATHER, mWeatherJson);
                                editor.apply();

                                // 更新 UI 控件
                                updateUIData();
                                mRefreshLayout.finishRefresh(true);

                            } else {
                                // 数据错误
                                mRefreshLayout.finishRefresh(false);
                                Toast.makeText(mActivity, "数据错误", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });


            // 获取空气质量信息
            String address2 = WeatherHttp.WEATHER_AIR_API + city + WeatherHttp.KEY;
            Log.d(TAG, "address2 " + address2);
            WeatherHttp.sendRequestWithOkHttp(address2, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mWeatherAqiTv.setText("-");
                            mWeatherPm25Tv.setText("-");
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String body = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(body);
                        String heWeather = jsonObject.getString("HeWeather6");

                        JSONArray jsonArray = new JSONArray(heWeather);
                        String jsonStr = jsonArray.getJSONObject(0).toString();

                        jsonObject = new JSONObject(jsonStr);
                        String status = jsonObject.getString("status");
                        if (status.equals("ok")) {
                            String airNowCity = jsonObject.getString("air_now_city");
                            mAirJson = airNowCity;
                            mAir = new Gson().fromJson(airNowCity, Air.class);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mAirJson != null) {
                                mWeatherAqiTv.setText(mAir.getAqi());
                                mWeatherPm25Tv.setText(mAir.getPm25());

                                SharedPreferences.Editor editor =
                                        mActivity.getPreferences(Context.MODE_PRIVATE).edit();
                                editor.putString(KEY_AIR, mAirJson);
                                editor.apply();
                            }
                        }
                    });
                }
            });
        }
    };

    /**
     * 更新 UI 控件
     */
    private void updateUIData() {
        mWeatherCountyNameTv.setText(mWeather.getBasic().getCounty());
        mWeatherUpdateTimeTv.setText(mWeather.getUpdate().getLoc());
        String tmpStr = mWeather.getNow().getTmp() + "℃";
        mWeatherTmpTv.setText(tmpStr);
        mWeatherCondTv.setText(mWeather.getNow().getCondTxt());
        String windStr = mWeather.getNow().getWindDir() + " " + mWeather.getNow().getWindSc() + "级";
        mWeatherWindTv.setText(windStr);
        // 天气图标
        String icon = "file:///android_asset/cond_icon_heweather/"
                + mWeather.getNow().getCondCode() + ".png";
        Glide.with(WeatherFragment.this).load(icon).into(mWeatherCondIv);

        mWeatherForecastLl.removeAllViews();
        List<Forecast> list = mWeather.getForecastList();
        for (Forecast forecast : list) {
            View view = LayoutInflater.from(mActivity)
                    .inflate(R.layout.forecast_item, mWeatherForecastLl, false);
            TextView fcItemDateTv = view.findViewById(R.id.tv_fc_item_date);
            TextView fcItemCondTv = view.findViewById(R.id.tv_fc_item_cond);
            TextView fcItemMinTmpTv = view.findViewById(R.id.tv_fc_item_min_tmp);
            TextView fcItemMaxTmpTv = view.findViewById(R.id.tv_fc_item_max_tmp);
            fcItemDateTv.setText(forecast.getDate());
            String condTxt = "白天 : " + forecast.getCondTxtD()
                    + "\n晚上 : " + forecast.getCondTxtN();
            fcItemCondTv.setText(condTxt);
            fcItemMinTmpTv.setText(forecast.getTmpMin());
            fcItemMaxTmpTv.setText(forecast.getTmpMax());
            mWeatherForecastLl.addView(view);
        }

        List<Lifestyle> list1 = mWeather.getLifestyleList();
        if (list1 != null && list1.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (Lifestyle lifestyle : list1) {
                sb.append(lifestyle.getSwitchType());
                sb.append(" : ");
                sb.append(lifestyle.getBrf());
                sb.append("\n");
                sb.append(lifestyle.getTxt());
                sb.append("\n\n");
            }
            mWeatherSuggestTv.setText(sb.toString());
        }
    }


    /**
     * 初始化 省-市-县 数据
     */
    private void initPickerViewData() {

        // 获取 assets 目录下的 json 文件数据
        String jsonData = new GetJsonDataUtil().getJson(mActivity, "province.json");

        ArrayList<Location> provinceList = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(jsonData);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                Location entity = gson.fromJson(data.optJSONObject(i).toString(), Location.class);
                provinceList.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */

        // 添加省份数据
        options1Items = provinceList;

        // 遍历省份
        for (int i = 0; i < provinceList.size(); i++) {
            // 该省的城市列表（第二级）
            ArrayList<String> cityList = new ArrayList<>();
            // 该省的所有地区列表（第三极）
            ArrayList<ArrayList<String>> allCountyList = new ArrayList<>();

            // 遍历该省份的所有城市
            for (int j = 0; j < provinceList.get(i).city.size(); j++) {
                String cityName = provinceList.get(i).city.get(j).name;
                cityList.add(cityName);

                // 该城市的所有地区列表
                ArrayList<String> countyList = new ArrayList<>();

                // 如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (provinceList.get(i).city.get(j).area == null
                        || provinceList.get(i).city.get(j).area.size() == 0) {
                    countyList.add("");
                } else {
                    // 该城市对应地区所有数据
                    for (int k = 0; k < provinceList.get(i).city.get(j).area.size(); k++) {
                        String countyName = provinceList.get(i).city.get(j).area.get(k);
                        // 添加该城市所有地区数据
                        countyList.add(countyName);
                    }
                }
                // 添加该省所有地区数据
                allCountyList.add(countyList);
            }

            // 添加城市数据
            options2Items.add(cityList);

            // 添加区县数据
            options3Items.add(allCountyList);
        }
    }

    /**
     * 初始化 PickerView
     */
    private void showPickerView() {

        OptionsPickerView pvOptions = new OptionsPickerView.Builder(
                mActivity, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                mOptions1 = options1;
                mOptions2 = options2;
                mOptions3 = options3;

                province = options1Items.get(options1).getPickerViewText();
                city = options2Items.get(options1).get(options2);
                county = options3Items.get(options1).get(options2).get(options3);
                String tx = province + " " + city + " " + county;
                Toast.makeText(mActivity, tx, Toast.LENGTH_SHORT).show();
                Log.d(TAG, tx);

                mRefreshLayout.autoRefresh();

                String options = mOptions1 + "," + mOptions2 + "," + mOptions3;
                SharedPreferences.Editor editor = mActivity.getPreferences(Context.MODE_PRIVATE).edit();
                editor.putString("pickerViewOptions", options);
                editor.apply();
            }
        })
                .setTitleText("城市选择")
                .setDividerColor(Color.GRAY)
                .setTextColorCenter(Color.BLACK)
                .setSubCalSize(18)
                .setContentTextSize(20)
                .setSelectOptions(mOptions1, mOptions2, mOptions3)
                .build();

        //一级选择器
//        pvOptions.setPicker(options1Items)
        //二级选择器
//        pvOptions.setPicker(options1Items, options2Items)
        // 三级选择器
        pvOptions.setPicker(options1Items, options2Items, options3Items);
        pvOptions.show();
    }

}
