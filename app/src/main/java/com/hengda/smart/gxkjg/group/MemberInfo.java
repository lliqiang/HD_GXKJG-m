package com.hengda.smart.gxkjg.group;

import java.io.Serializable;

/**
 * 作者：Tailyou
 * 时间：2016/3/11 11:06
 * 邮箱：tailyou@163.com
 * 描述：群组成员
 */
public class MemberInfo implements Serializable {



    private String user_login;
    private int axis_x;
    private int axis_y;
    private String map_id;
    private String auto_num;
    private String nicename;
    private String map_name;

    public String getMap_name() {
        return map_name;
    }

    public void setMap_name(String map_name) {
        this.map_name = map_name;
    }

    public String getUser_login() {
        return user_login;
    }

    public void setUser_login(String user_login) {
        this.user_login = user_login;
    }

    public int getAxis_x() {
        return axis_x;
    }

    public void setAxis_x(int axis_x) {
        this.axis_x = axis_x;
    }

    public int getAxis_y() {
        return axis_y;
    }

    public void setAxis_y(int axis_y) {
        this.axis_y = axis_y;
    }

    public String getMap_id() {
        return map_id;
    }

    public void setMap_id(String map_id) {
        this.map_id = map_id;
    }

    public String getAuto_num() {
        return auto_num;
    }

    public void setAuto_num(String auto_num) {
        this.auto_num = auto_num;
    }

    public String getNicename() {
        return nicename;
    }

    public void setNicename(String nicename) {
        this.nicename = nicename;
    }

    @Override
    public String toString() {
        return "MemberInfo{" +
                "user_login='" + user_login + '\'' +
                ", axis_x='" + axis_x + '\'' +
                ", axis_y='" + axis_y + '\'' +
                ", map_id='" + map_id + '\'' +
                ", auto_num='" + auto_num + '\'' +
                ", nicename='" + nicename + '\'' +
                ", map_name='" + map_name + '\'' +
                '}';
    }
}
