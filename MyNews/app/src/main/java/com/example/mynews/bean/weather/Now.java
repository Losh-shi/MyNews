package com.example.mynews.bean.weather;

import com.google.gson.annotations.SerializedName;

/**
 * Now
 *
 * @author ggz
 * @date 2018/2/26
 */

public class Now {

    @SerializedName("cond_code")
    private String condCode;

    @SerializedName("cond_txt")
    private String condTxt;

    private String tmp;

    private String hum;

    @SerializedName("wind_dir")
    private String windDir;

    @SerializedName("wind_sc")
    private String windSc;

    private String vis;


    public Now() {
        super();
    }

    public String getCondCode() {
        return condCode;
    }

    public void setCondCode(String condCode) {
        this.condCode = condCode;
    }

    public String getCondTxt() {
        return condTxt;
    }

    public void setCondTxt(String condTxt) {
        this.condTxt = condTxt;
    }

    public String getTmp() {
        return tmp;
    }

    public void setTmp(String tmp) {
        this.tmp = tmp;
    }

    public String getHum() {
        return hum;
    }

    public void setHum(String hum) {
        this.hum = hum;
    }

    public String getWindDir() {
        return windDir;
    }

    public void setWindDir(String windDir) {
        this.windDir = windDir;
    }

    public String getWindSc() {
        return windSc;
    }

    public void setWindSc(String windSc) {
        this.windSc = windSc;
    }

    public String getVis() {
        return vis;
    }

    public void setVis(String vis) {
        this.vis = vis;
    }
}
