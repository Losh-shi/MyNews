package com.example.mynews.bean;

import com.example.mynews.bean.weather.Basic;
import com.example.mynews.bean.weather.Forecast;
import com.example.mynews.bean.weather.Lifestyle;
import com.example.mynews.bean.weather.Now;
import com.example.mynews.bean.weather.Update;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Weather
 *
 * @author ggz
 * @date 2018/2/24
 */

public class Weather {

    private String status;

    private Basic basic;

    private Update update;

    private Now now;

    @SerializedName("daily_forecast")
    private List<Forecast> forecastList;

    @SerializedName("lifestyle")
    private List<Lifestyle> lifestyleList;


    public Weather() {
        super();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Basic getBasic() {
        return basic;
    }

    public void setBasic(Basic basic) {
        this.basic = basic;
    }

    public Update getUpdate() {
        return update;
    }

    public void setUpdate(Update update) {
        this.update = update;
    }

    public Now getNow() {
        return now;
    }

    public void setNow(Now now) {
        this.now = now;
    }

    public List<Forecast> getForecastList() {
        return forecastList;
    }

    public void setForecastList(List<Forecast> forecastList) {
        this.forecastList = forecastList;
    }

    public List<Lifestyle> getLifestyleList() {
        return lifestyleList;
    }

    public void setLifestyleList(List<Lifestyle> lifestyleList) {
        this.lifestyleList = lifestyleList;
    }
}
