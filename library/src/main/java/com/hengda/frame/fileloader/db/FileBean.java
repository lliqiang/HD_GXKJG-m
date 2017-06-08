package com.hengda.frame.fileloader.db;


/**
 * Created by shiwei on 2017/1/11.
 */
public class FileBean {
    private String name;
    private String date;

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "FileBean{" +
                "name='" + name + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
