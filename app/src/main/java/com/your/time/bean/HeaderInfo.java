package com.your.time.bean;

import java.util.ArrayList;

public class HeaderInfo {

    private String name;
    private ArrayList<DetailInfo> list = new ArrayList<DetailInfo>();

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public ArrayList<DetailInfo> getList() {
        return list;
    }
    public void setList(ArrayList<DetailInfo> list) {
        this.list = list;
    }
}