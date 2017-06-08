package com.hengda.smart.gxkjg.entity;

import java.util.List;

/**
 * Created by lenovo on 2016/12/6.
 */

public class Model {
    List<Exhibit> list;

    public Model(List<Exhibit> list) {
        this.list = list;
    }

    public List<Exhibit> getList() {
        return list;
    }

    public void setList(List<Exhibit> list) {
        this.list = list;
    }
}
