package com.example.mynews.http;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * WeatherHttp
 *
 * @author ggz
 * @date 2018/2/25
 */
public class WeatherHttp {

    public static final String WEATHER_API = "https://free-api.heweather.com/s6/weather?location=";
    public static final String WEATHER_AIR_API = "https://free-api.heweather.com/s6/air/now?location=";
    
    public static final String KEY = "&key=e885c1a34d104ea996c1dc29f1c0014a";

    public static void sendRequestWithOkHttp(String address, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .build();
        client.newCall(request).enqueue(callback);
    }
}
