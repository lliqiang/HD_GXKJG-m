package com.hengda.smart.gxkjg.event;

import java.util.List;

/**
 * 作者：Tailyou （祝文飞）
 * 时间：2016/5/6 17:06
 * 邮箱：tailyou@163.com
 * 描述：EventBus 发送 AutoNoList，主要用于收多个号的情况
 */
public class AutoNoListEvent {
    List<Integer> autoNos;

    public List<Integer> getAutoNos() {
        return autoNos;
    }

    public AutoNoListEvent(List<Integer> autoNos) {
        this.autoNos = autoNos;
    }
}
