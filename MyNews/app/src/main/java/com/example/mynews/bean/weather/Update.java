package com.example.mynews.bean.weather;

import com.google.gson.annotations.SerializedName;

/**
 * Update
 *
 * @author ggz
 * @date 2018/2/26
 */

public class Update {

    private String loc;

    private String utc;


    public Update() {
        super();
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public String getUtc() {
        return utc;
    }

    public void setUtc(String utc) {
        this.utc = utc;
    }
}
