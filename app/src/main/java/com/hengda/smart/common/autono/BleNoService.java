package com.hengda.smart.common.autono;

import android.content.Intent;
import android.util.Log;


import com.hengda.smart.common.rxbus.RxBus;
import com.hengda.smart.gxkjg.entity.AutoNum;
import com.hengda.zwf.autonolibrary.BleNumService;
import com.hengda.zwf.autonolibrary.beacon.Beacon;
import com.hengda.zwf.autonolibrary.beacon.BeaconHelper;
import com.hengda.zwf.autonolibrary.util.NoUtil;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;


/**
 * 作者：祝文飞（Tailyou）
 * 邮箱：tailyou@163.com
 * 时间：2016/11/9 14:54
 * 描述：蓝牙收号服务，思路：
 * 1、启动蓝牙收号后，将后台扫描得到的 BluetoothDevice device, int rssi, byte[] scanRecord 解析成Beacon；
 * 2、通过接口回调，在收号服务中处理解析得到的Beacon；
 * 3、根据一定条件过滤，将符合条件的Beacon加入List；
 * 4、每个一定时间间隔从List中取AutoNo，取的原则是：按平均rssi排序，较大为优
 */
public class BleNoService extends BleNumService {

    private static final long TIME_INTERVAL = 8L;    //取号时间间隔
    private static final int RSSI_THRESHOLD = -94;   //有效Rssi阈值
    private NoUtil noUtil;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        noUtil = NoUtil.getInstance();
        Observable.interval(1L, 3L, TimeUnit.SECONDS)
                .subscribe(aLong -> {
                    List<Integer> bestBeaconNums = noUtil.getBestBleNums();
                    if (bestBeaconNums.size() > 0) {
                        //TODO 此处通过 EventBus 或 RxBus 发送收到的多模号
                        EventBus.getDefault().post(new AutoNum(bestBeaconNums.get(0)));
                        Log.i("autoEvent", "autoEvent:----------------" + bestBeaconNums.get(0));
                        bestBeaconNums.clear();
                    }
                }, error -> {
                    String message = error.getMessage();
                    Logger.e(message);
                });
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        BeaconHelper.getBluetoothAdapter(this).disable();
    }

    /**
     * 在此处进行过滤，将符合条件的Beacon加到List
     *
     * @author 祝文飞（Tailyou）
     * @time 2017/1/23 8:49
     */
    @Override
    public void onBeaconReceive(Beacon beacon) {
        if (beacon.getRssi() > RSSI_THRESHOLD) {

            noUtil.addBeacon(beacon);
        }
    }

    /**
     * 启动WiFi收号服务
     *
     * @author 祝文飞（Tailyou）
     * @time 2017/1/23 8:45
     */
    @Override
    public void startWifiNoService() {

    }

    /**
     * 停止WiFi收号服务
     *
     * @author 祝文飞（Tailyou）
     * @time 2017/1/23 8:45
     */
    @Override
    public void stopWifiNoService() {

    }

}
