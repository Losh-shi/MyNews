package com.example.mynews.bean.weather;

import com.google.gson.annotations.SerializedName;

/**
 * Basic
 *
 * @author ggz
 * @date 2018/2/26
 */
public class Basic {

    private String cid;

    @SerializedName("location")
    private String county;

    @SerializedName("parent_city")
    private String city;

    @SerializedName("admin_area")
    private String province;

    @SerializedName("cnty")
    private String country;

    private String lat;

    private String lon;

    private String tz;


    public Basic() {
        super();
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getTz() {
        return tz;
    }

    public void setTz(String tz) {
        this.tz = tz;
    }
}
