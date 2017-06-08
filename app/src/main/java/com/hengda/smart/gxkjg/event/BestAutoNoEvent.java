package com.hengda.smart.gxkjg.event;

/**
 * 作者：Tailyou （祝文飞）
 * 时间：2016/5/6 17:06
 * 邮箱：tailyou@163.com
 * 描述：EventBus 发送 AutoNo 主要用于地图界面或收单个号的情况
 */
public class BestAutoNoEvent {
    int autoNo;

    public int getAutoNo() {
        return autoNo;
    }

    public BestAutoNoEvent(int autoNo) {
        this.autoNo = autoNo;
    }
}
