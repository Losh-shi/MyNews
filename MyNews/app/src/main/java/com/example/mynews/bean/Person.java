package com.example.mynews.bean;

import cn.bmob.v3.BmobObject;

/**
 * Person
 *
 * @author ggz
 * @date 2018/5/3
 */
public class Person extends BmobObject {

    private String account;
    private String password;
    private String phoneNumber;
    private String name;
    private String favourite;

    public Person() {
        super();
    }

    public Person(String account, String password, String phoneNumber, String name) {
        this.account = account;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.name = name;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFavourite() {
        return favourite;
    }

    public void setFavourite(String favourite) {
        this.favourite = favourite;
    }
}
