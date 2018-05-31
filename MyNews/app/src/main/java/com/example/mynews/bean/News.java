package com.example.mynews.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * News
 *
 * @author ggz
 * @date 2018/1/14
 */

public class News implements Serializable {

    /**
     * 新闻类别
     */
    private int channelId;

    /**
     * 新闻组号
     */
    private int pageToken;

    /**
     * 新闻 id
     */
    private String id;

    /**
     * 新闻 url
     */
    private String url;

    /**
     * 新闻标题
     */
    private String title;

    /**
     * 新闻内容
     */
    private String content;

    /**
     * 新闻发布方
     */
    private String pubName;

    /**
     * 新闻发布时间
     */
    private String pubDate;

    /**
     * 图片列表的 url 地址
     */
    private List<String> imageList = new ArrayList<>();

    /**
     * 是否为个人收藏
     */
    private boolean isFavourite = false;


    public News(int channelId, int pageToken, String id, String url, String title,
                String content, String pubName, String pubDate) {
        this.channelId = channelId;
        this.pageToken = pageToken;
        this.id = id;
        this.url = url;
        this.title = title;
        this.content = content;
        this.pubName = pubName;
        this.pubDate = pubDate;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public int getPageToken() {
        return pageToken;
    }

    public void setPageToken(int pageToken) {
        this.pageToken = pageToken;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPubName() {
        return pubName;
    }

    public void setPubName(String pubName) {
        this.pubName = pubName;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public List getImageList() {
        return imageList;
    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
    }

    public boolean getIsFavourite() {
        return isFavourite;
    }

    public void setIsFavourite(boolean favourite) {
        this.isFavourite = favourite;
    }

}
