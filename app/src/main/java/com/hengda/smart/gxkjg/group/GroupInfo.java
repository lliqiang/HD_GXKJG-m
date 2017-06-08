package com.hengda.smart.gxkjg.group;

/**
 * 作者：Tailyou （祝文飞）
 * 时间：2016/6/16 14:07
 * 邮箱：tailyou@163.com
 * 描述：
 */
public class GroupInfo {


    private String group;
    private String user_nickname;
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getGroup() {
        return group;
    }

    public String getUser_nickname() {
        return user_nickname;

    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setUser_nickname(String user_nickname) {
        this.user_nickname = user_nickname;
    }

    @Override
    public String toString() {
        return "GroupInfo{" +
                "group='" + group + '\'' +
                ", user_nickname='" + user_nickname + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
