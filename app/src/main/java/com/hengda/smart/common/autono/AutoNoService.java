package com.hengda.smart.common.autono;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.os.RemoteException;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.service.RunningAverageRssiFilter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 作者：Tailyou （祝文飞）
 * 时间：2016/5/5 13:53
 * 邮箱：tailyou@163.com
 * 描述：
 */
public abstract class AutoNoService extends Service implements BeaconConsumer, PhoneRFID
        .AutoDataChangeListener {

    BeaconManager beaconManager;
    WifiManager wifiManager;
    PhoneRFID phoneRFID;
    NetStateReceiver netStateReceiver;
    List<Integer> autoNoList = new ArrayList<>();

    int postFrequency = 3;
    int needAutoNoCount = 3;
    int bleScanCount;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initBLEOrWiFiManager();
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (beaconManager != null)
            beaconManager.unbind(this);
        if (netStateReceiver != null)
            unregisterReceiver(netStateReceiver);
        if (phoneRFID != null) {
            try {
                phoneRFID.stopDec();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 初始化BLE或者WiFi Manager
     */
    public void initBLEOrWiFiManager() {
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (bluetoothAdapter != null && !bluetoothAdapter.isEnabled()) {
                bluetoothAdapter.enable();
            }
            initBeaconManager();
        } else {
            initWifiManager();
        }
    }


    /**
     * 初始化BeaconManager
     */
    private void initBeaconManager() {
        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215," +
                "i:4-19,i:20-21,i:22-23,p:24-24"));
        beaconManager.setForegroundScanPeriod(800);
        BeaconManager.setRssiFilterImplClass(RunningAverageRssiFilter.class);
        RunningAverageRssiFilter.setSampleExpirationMilliseconds(5000L);
        beaconManager.bind(this);
    }


    /**
     * 初始化WifiManager
     */
    private void initWifiManager() {
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        netStateReceiver = new NetStateReceiver();
        registerReceiver(netStateReceiver, filter);
    }


    /**
     * 初始化RFID
     */
    private void initRFID() {
        phoneRFID = new PhoneRFID();
        phoneRFID.setAutoDataChangeListener(this);
        try {
            phoneRFID.startDec();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * BLE收号
     */
    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setRangeNotifier((collection, region) -> {
            if (collection.size() > 0) {
                List<Beacon> beaconList = new ArrayList<>();
                List<Beacon> validBeaconList = new ArrayList<>();
                beaconList.addAll(collection);
                sortBeacons(beaconList);
                filterBeacons(beaconList, validBeaconList);

                if (validBeaconList.size() > 0) {
                    autoNoList.clear();
                    beaconList.clear();
                    for (Beacon beacon : validBeaconList) {
                        int autoNo = beacon.getId2().toInt();
                        autoNoList.add(autoNo);
                        beaconList.add(beacon);
                        if (autoNoList.size() == needAutoNoCount)
                            break;
                    }

                    /**
                     * 发送AutoNo到地图
                     */
                    AutoNoUtil.addAutoNo(autoNoList.get(0));
                    int bestBeaconNo = AutoNoUtil.getBestAutoNo();
                    if (bestBeaconNo != 0) {
                        postReceivedBestAutoNo(bestBeaconNo);
                    }

                    /**
                     * 发送AutoNos到列表
                     */
                    bleScanCount++;
                    AutoNoUtil.addBeacons(beaconList);
                    if (bleScanCount == postFrequency) {
                        autoNoList = AutoNoUtil.getBestAutoNos();
                        postReceivedAutoNoList(autoNoList);
                        bleScanCount = 0;
                    }
                }
            }
        });
        try {
            beaconManager.startRangingBeaconsInRegion(new Region("UniqueId", null, null, null));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    /**
     * WIFI收号
     */
    private class NetStateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case WifiManager.NETWORK_STATE_CHANGED_ACTION:
                    wifiManager.startScan();
                    break;
                case WifiManager.WIFI_STATE_CHANGED_ACTION:
                    int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager
                            .WIFI_STATE_DISABLED);
                    if (wifiState == WifiManager.WIFI_STATE_DISABLED) {
                        noticeOpenWifi();
                    }
                    break;
                case WifiManager.SCAN_RESULTS_AVAILABLE_ACTION:
                    List<ScanResult> scanResults = wifiManager.getScanResults();
                    List<ScanResult> validScanResults = new ArrayList<>();
                    if (scanResults.size() > 0) {
                        sortScanResults(scanResults);
                        filterScanResults(scanResults, validScanResults);
                        if (validScanResults.size() > 0) {
                            autoNoList.clear();
                            for (ScanResult scanResult : validScanResults) {
                                try {
                                    int autoNo = Integer.parseInt(DESHelper.decrypt(scanResult
                                                    .SSID.substring(2) + "=",
                                            DESHelper.genKeyByBSSID(scanResult.BSSID)));
                                    autoNoList.add(autoNo);
                                    if (autoNoList.size() == needAutoNoCount)
                                        break;
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            if (autoNoList.size() > 0) {
                                /**
                                 * 发送AutoNo到地图
                                 */
                                AutoNoUtil.addAutoNo(autoNoList.get(0));
                                int bestBeaconNo = AutoNoUtil.getBestAutoNo();
                                if (bestBeaconNo != 0) {
                                    postReceivedBestAutoNo(bestBeaconNo);
                                }

                                /**
                                 * 发送AutoNos到列表
                                 */
                                postReceivedAutoNoList(autoNoList);
                            }
                        }
                    }
                    wifiManager.startScan();
                    break;
            }
        }
    }


    /**
     * RFID收号
     *
     * @param autoNum
     */
    @Override
    public void getAutoNum(int autoNum) {
        postReceivedBestAutoNo(autoNum);
    }


    /**
     * Beacons排序-按距离升序
     */
    private void sortBeacons(List<Beacon> beaconList) {
        Collections.sort(beaconList, (lhs, rhs) -> lhs.getDistance() < rhs.getDistance() ? -1 : 1);
    }


    /**
     * ScanResult排序-按level降序
     *
     * @param scanResults WiFi 扫描结果
     */
    private void sortScanResults(List<ScanResult> scanResults) {
        Collections.sort(scanResults, (lhs, rhs) -> lhs.level < rhs.level ? 1 : -1);
    }


    /**
     * 过滤Beacons
     *
     * @param beaconList
     * @param validBeaconList
     */
    public abstract void filterBeacons(List<Beacon> beaconList, List<Beacon> validBeaconList);


    /**
     * 过滤ScanResults
     *
     * @param scanResults
     * @param hdScanResults
     */
    public abstract void filterScanResults(List<ScanResult> scanResults, List<ScanResult>
            hdScanResults);


    /**
     * 发送接收到的AutoNoList
     *
     * @param autoNoList
     */
    public abstract void postReceivedAutoNoList(List<Integer> autoNoList);


    /**
     * 发送接收到的BestAutoNo
     *
     * @param bestAutoNo
     */
    public abstract void postReceivedBestAutoNo(int bestAutoNo);


    /**
     * 通知打开WiFi
     */
    public abstract void noticeOpenWifi();

}
