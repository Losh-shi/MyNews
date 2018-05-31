package com.example.mynews.bean.weather;

import com.google.gson.annotations.SerializedName;

/**
 * Forecast
 *
 * @author ggz
 * @date 2018/2/25
 */

public class Forecast {

    private String date;

    @SerializedName("cond_txt_d")
    private String condTxtD;

    @SerializedName("cond_txt_n")
    private String condTxtN;

    @SerializedName("tmp_min")
    private String tmpMin;

    @SerializedName("tmp_max")
    private String tmpMax;


    public Forecast() {
        super();
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCondTxtD() {
        return condTxtD;
    }

    public void setCondTxtD(String condTxtD) {
        this.condTxtD = condTxtD;
    }

    public String getCondTxtN() {
        return condTxtN;
    }

    public void setCondTxtN(String condTxtN) {
        this.condTxtN = condTxtN;
    }

    public String getTmpMin() {
        return tmpMin;
    }

    public void setTmpMin(String tmpMin) {
        this.tmpMin = tmpMin;
    }

    public String getTmpMax() {
        return tmpMax;
    }

    public void setTmpMax(String tmpMax) {
        this.tmpMax = tmpMax;
    }
}
