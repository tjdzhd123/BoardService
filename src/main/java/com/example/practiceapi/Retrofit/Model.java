package com.example.practiceapi.Retrofit;

import com.google.gson.annotations.SerializedName;

import java.sql.Timestamp;
public class Model {
    private int seq;
    private String title;
    @SerializedName("content")
    private String text;
    private String id_frt;
    private Timestamp dt_frt;
    private String id_lst;
    private Timestamp dt_lst;
    private String dt;
    //Post
    public Model( String title, String text, String id_frt) {
        this.title = title;
        this.text = text;
        this.id_frt = id_frt;
    }
    public int getSeq() {
        return seq;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String getId_frt() {
        return id_frt;
    }

    public Timestamp getDt_frt() {
        return dt_frt;
    }

    public String getId_lst() {
        return id_lst;
    }

    public Timestamp getDt_lst() {
        return dt_lst;
    }

    public String getDt() {return dt;}
}
