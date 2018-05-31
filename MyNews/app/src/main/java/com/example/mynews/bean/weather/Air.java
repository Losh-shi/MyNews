package com.example.mynews.bean.weather;

/**
 * Basic
 *
 * @author ggz
 * @date 2018/2/26
 */
public class Air {

    private String aqi;

    private String pm25;

    public Air(String aqi, String pm25) {
        this.aqi = aqi;
        this.pm25 = pm25;
    }

    public String getAqi() {
        return aqi;
    }

    public void setAqi(String aqi) {
        this.aqi = aqi;
    }

    public String getPm25() {
        return pm25;
    }

    public void setPm25(String pm25) {
        this.pm25 = pm25;
    }
}
