package com.example.onlinevoting.models;

public class ElectionModel {
    String date, key;

    public ElectionModel() {
    }

    public ElectionModel(String date, String key) {
        this.date = date;
        this.key = key;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
