package com.level500.ub.zebracomics;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import java.util.List;

public class Titles {

    private String name;
    private String description;
    private String id;
    private String thumbnail;


    public Titles(Context context, List<Titles> titlesList) {
    }

    public Titles(String name, String description, String thumbnail, String id) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {return description; }

    public  String getId() { return id; }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

}
