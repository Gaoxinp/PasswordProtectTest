package com.example.model;

/**
 * Created by 高信朋 on 2017/8/6.
 * 这是密码记录的java bean
 */

public class Item {
    private String name;
    private String url;
    private String account;
    private String password;
    private String comment;

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getName() {

        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getAccount() {
        return account;
    }

    public String getPassword() {
        return password;
    }

    public String getComment() {
        return comment;
    }
}
