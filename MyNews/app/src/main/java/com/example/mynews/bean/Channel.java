package com.example.mynews.bean;

/**
 * Channel
 *
 * @author ggz
 * @date 2017/12/27
 */

public class Channel {

    /**
     * 新闻频道 ID
     */
    private int id;

    /**
     * 新闻频道名字
     */
    private String name;

    /**
     * 排列顺序
     */
    private int orderNum;

    /**
     * 所属栏目
     */
    private int selected;


    public Channel(int id, String name, int orderNum, int selected) {
        this.id = id;
        this.name = name;
        this.orderNum = orderNum;
        this.selected = selected;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }
}
