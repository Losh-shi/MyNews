package com.example.mynews.http;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * NewsHttp
 *
 * @author ggz
 * @date 2018/2/2
 */

public class NewsHttp {

    private static final String NEWS_URL = "http://api01.bitspaceman.com:8000/news/toutiao?";

    private static final String KEY = "apikey=avj7v4Ia94S6T7L5niV24hBhYKErdE4ryWBzZfzSCUm8axLw3Dj6pFOxzsjbxIsF";

    private static final String KW = "&kw=";

    private static final String PAGE_TOKEN = "&pageToken=";

    private static final String SOCIETY = "&catid=news_society";
    private static final String ENTERTAINMENT = "&catid=news_entertainment";
    private static final String TECH = "&catid=news_tech";
    private static final String CAR = "&catid=news_car";
    private static final String SPORTS = "&catid=news_sports";
    private static final String FINANCE = "&catid=news_finance";
    private static final String MILITARY = "&catid=news_military";
    private static final String WORLD = "&catid=news_world";
    private static final String HEALTH = "&catid=news_health";
    private static final String HOUSE = "&catid=news_house";
    private static final String FASHION = "&catid=news_fashion";
    private static final String BABY = "&catid=news_baby";
    private static final String HISTORY = "&catid=news_history";
    private static final String FUNNY = "&catid=funny";
    private static final String DIGITAL = "&catid=digital";
    private static final String FOOD = "&catid=news_food";
    private static final String REGIMEN = "&catid=news_regimen";
    private static final String MOVIE = "&catid=movie";
    private static final String CELLPHONE = "&catid=cellphone";
    private static final String TRAVEL = "&catid=news_travel";
    private static final String EMOTION = "&catid=emotion";
    private static final String HOME = "&catid=news_home";
    private static final String EDU = "&catid=news_edu";
    private static final String AGRICULTURE = "&catid=news_agriculture";
    private static final String PREGNANCY = "&catid=pregnancy";
    private static final String CULTURE = "&catid=news_culture";
    private static final String GAME = "&catid=news_game";
    private static final String STOCK = "&catid=stock";
    private static final String SCIENCE = "&catid=science_all";
    private static final String COMIC = "&catid=news_comic";
    private static final String STORY = "&catid=news_story";
    private static final String ASTROLOGY = "&catid=news_astrology";


    public static void sendRequestWithOkHttp(String address, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static String getUrl(int channelId, String arg1, String arg2) {
        String url;
        switch (channelId) {
            case 1:
                url = NEWS_URL + KEY + SOCIETY + PAGE_TOKEN + arg1;
                break;
            case 2:
                url = NEWS_URL + KEY + ENTERTAINMENT + PAGE_TOKEN + arg1;
                break;
            case 3:
                url = NEWS_URL + KEY + TECH + PAGE_TOKEN + arg1;
                break;
            case 4:
                url = NEWS_URL + KEY + CAR + PAGE_TOKEN + arg1;
                break;
            case 5:
                url = NEWS_URL + KEY + SPORTS + PAGE_TOKEN + arg1;
                break;
            case 6:
                url = NEWS_URL + KEY + FINANCE + PAGE_TOKEN + arg1;
                break;
            case 7:
                url = NEWS_URL + KEY + MILITARY + PAGE_TOKEN + arg1;
                break;
            case 8:
                url = NEWS_URL + KEY + WORLD + PAGE_TOKEN + arg1;
                break;
            case 9:
                url = NEWS_URL + KEY + HEALTH + PAGE_TOKEN + arg1;
                break;
            case 10:
                url = NEWS_URL + KEY + HOUSE + PAGE_TOKEN + arg1;
                break;
            case 11:
                url = NEWS_URL + KEY + FASHION + PAGE_TOKEN + arg1;
                break;
            case 12:
                url = NEWS_URL + KEY + BABY + PAGE_TOKEN + arg1;
                break;
            case 13:
                url = NEWS_URL + KEY + HISTORY + PAGE_TOKEN + arg1;
                break;
            case 14:
                url = NEWS_URL + KEY + FUNNY + PAGE_TOKEN + arg1;
                break;
            case 15:
                url = NEWS_URL + KEY + DIGITAL + PAGE_TOKEN + arg1;
                break;
            case 16:
                url = NEWS_URL + KEY + FOOD + PAGE_TOKEN + arg1;
                break;
            case 17:
                url = NEWS_URL + KEY + REGIMEN + PAGE_TOKEN + arg1;
                break;
            case 18:
                url = NEWS_URL + KEY + MOVIE + PAGE_TOKEN + arg1;
                break;
            case 19:
                url = NEWS_URL + KEY + CELLPHONE + PAGE_TOKEN + arg1;
                break;
            case 20:
                url = NEWS_URL + KEY + TRAVEL + PAGE_TOKEN + arg1;
                break;
            case 21:
                url = NEWS_URL + KEY + EMOTION + PAGE_TOKEN + arg1;
                break;
            case 22:
                url = NEWS_URL + KEY + HOME + PAGE_TOKEN + arg1;
                break;
            case 23:
                url = NEWS_URL + KEY + EDU + PAGE_TOKEN + arg1;
                break;
            case 24:
                url = NEWS_URL + KEY + AGRICULTURE + PAGE_TOKEN + arg1;
                break;
            case 25:
                url = NEWS_URL + KEY + PREGNANCY + PAGE_TOKEN + arg1;
                break;
            case 26:
                url = NEWS_URL + KEY + CULTURE + PAGE_TOKEN + arg1;
                break;
            case 27:
                url = NEWS_URL + KEY + GAME + PAGE_TOKEN + arg1;
                break;
            case 28:
                url = NEWS_URL + KEY + STOCK + PAGE_TOKEN + arg1;
                break;
            case 29:
                url = NEWS_URL + KEY + SCIENCE + PAGE_TOKEN + arg1;
                break;
            case 30:
                url = NEWS_URL + KEY + COMIC + PAGE_TOKEN + arg1;
                break;
            case 31:
                url = NEWS_URL + KEY + STORY + PAGE_TOKEN + arg1;
                break;
            case 32:
                url = NEWS_URL + KEY + ASTROLOGY + PAGE_TOKEN + arg1;
                break;
            case 33:
                url = NEWS_URL + KEY + KW + arg1 + PAGE_TOKEN + arg2;
                break;
            default:
                url = null;
                break;
        }
        return url;
    }

}
