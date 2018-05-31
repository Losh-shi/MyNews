package com.example.mynews.bean.weather;

/**
 * Lifestyle
 *
 * @author ggz
 * @date 2018/2/25
 */

public class Lifestyle {

    private String type;

    private String brf;

    private String txt;


    public Lifestyle() {
        super();
    }

    public String getSwitchType() {

        String typeName;

        switch (type) {
            case "comf":
                typeName = "舒适度";
                break;
            case "cw":
                typeName = "洗车";
                break;
            case "drsg":
                typeName = "穿衣";
                break;
            case "flu":
                typeName = "感冒";
                break;
            case "sport":
                typeName = "运动";
                break;
            case "trav":
                typeName = "旅游";
                break;
            case "uv":
                typeName = "紫外线";
                break;
            case "air":
                typeName = "空气污染";
                break;
            case "ac":
                typeName = "空调指数";
                break;
            case "ag":
                typeName = "过敏指数";
                break;
            case "gl":
                typeName = "太阳镜指数";
                break;
            case "mu":
                typeName = "化妆";
                break;
            case "airc":
                typeName = "晾晒指数";
                break;
            case "ptfc":
                typeName = "交通";
                break;
            case "fsh":
                typeName = "钓鱼指数";
                break;
            case "spi":
                typeName = "防晒";
                break;
            default:
                typeName = "";
                break;
        }
        return typeName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBrf() {
        return brf;
    }

    public void setBrf(String brf) {
        this.brf = brf;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }
}
