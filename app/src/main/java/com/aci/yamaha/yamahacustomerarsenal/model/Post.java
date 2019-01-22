package com.aci.yamaha.yamahacustomerarsenal.model;

/**
 * Created by aburasel on 10/4/2017.
 */

public class Post {
    int id;
    String value;

    public Post(int id, String value) {
        this.id = id;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
