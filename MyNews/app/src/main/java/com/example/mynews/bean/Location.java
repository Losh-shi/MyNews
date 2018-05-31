package com.example.mynews.bean;

import com.bigkoo.pickerview.model.IPickerViewData;

import java.util.List;

/**
 * Location
 *
 * @author ggz
 * @date 2018/3/2
 */

public class Location implements IPickerViewData{
    /**
     * name : 省份
     * city : [{"name":"北京市","area":["东城区","西城区","崇文区","宣武区","朝阳区"]}]
     */
    public String name;
    public List<CityBean> city;

    public static class CityBean {
        /**
         * name : 城市
         * area : ["东城区","西城区","崇文区","昌平区"]
         */
        public String name;
        public List<String> area;
    }

    @Override
    public String getPickerViewText() {
        return this.name;
    }
}
