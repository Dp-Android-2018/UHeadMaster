package com.dp.uheadmaster.models;

/**
 * Created by DELL on 28/10/2017.
 */

public class Languages {

    private String key;
    private String lang;

    public Languages(String key, String lang) {
        this.key = key;
        this.lang = lang;
    }


    public void setKey(String key) {
        this.key = key;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }


    public String getKey() {
        return key;
    }

    public String getLang() {
        return lang;
    }
}
