package com.hengda.smart.common.autono;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.hengda.smart.gxkjg.entity.AutoNum;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.service.ArmaRssiFilter;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class IBeaconService extends Service implements BeaconConsumer {
    public BeaconManager beaconManager;
    private int maxKey = 0;
    private List<Integer> ints = new ArrayList<>();
    private boolean isAutoPlay;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        BeaconManager.setRssiFilterImplClass(ArmaRssiFilter.class);
        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        beaconManager.setForegroundScanPeriod(1000);
        beaconManager.setForegroundBetweenScanPeriod(1000);
        beaconManager.setBackgroundMode(false);
        beaconManager.bind(this);
        verifyBluetooth();
    }
//打开蓝牙
    private void verifyBluetooth() {
        try {
            if (!beaconManager.checkAvailability()) {
                BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                mBluetoothAdapter.enable();
            }
        } catch (RuntimeException e) {
        }
    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
                    final List<Beacon> tempListBeacons = new ArrayList<Beacon>();
                    for (Beacon beacon : beacons) {
                        tempListBeacons.add(beacon);
                    }
                    //???
                    Collections.sort(tempListBeacons, new ComparatorBeacon());
                    ints.add(tempListBeacons.get(0).getId2().toInt());
                    //!=5???
                    if (ints.size() != 5) {
                        return;
                    } else {
                        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
                        for (int i : ints) {
                            if (null != map.get(i)) {
                                map.put(i, map.get(i) + 1);
                            } else {
                                map.put(i, 1);
                            }
                        }
                        int s = FindMaxValue(map);
                        if (s >= 3 && isAutoPlay) {
                            EventBus.getDefault().post(new AutoNum(maxKey));
                        }
                        ints.remove(0);
                    }
                }
            }
        });
        try {
            beaconManager.startRangingBeaconsInRegion(new Region("UniqueId", null, null, null));
        } catch (RemoteException e) {

        }
    }

    private int FindMaxValue(Map<Integer, Integer> map) {
        Iterator iter = map.entrySet().iterator();
        Map.Entry entry = (Map.Entry) iter.next();
        maxKey = (int) entry.getKey();
        int maxValue = (int) entry.getValue();
        while (iter.hasNext()) {
            entry = (Map.Entry) iter.next();
            int tempK = (int) entry.getKey();
            int tempV = (int) entry.getValue();
            if (maxValue < tempV) {
                maxKey = tempK;
                maxValue = tempV;
            }
        }
        return maxValue;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainEventBus(String str) {
        if (str.equals("true"))
            isAutoPlay = true;
        else
            isAutoPlay = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
        EventBus.getDefault().unregister(this);
    }
}
