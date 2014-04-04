package com.eavg.sampleapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Eric on 3/25/14.
 */
public class Goal {

    private int id;
    private String uri;
    private String title;
    private String description;
    private String icon;
    private String language;
    @SerializedName("items_count")
    private int itemsCount;
    @SerializedName("sentences_count")
    private int sentencesCount;

    public int getId() {
        return id;
    }

    public String getUri() {
        return uri;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getIcon() {
        return icon;
    }

    public int getItemsCount() {
        return itemsCount;
    }

    public int getSentencesCount() {
        return sentencesCount;
    }

    public String getLanguage() {
        return language;
    }
}
