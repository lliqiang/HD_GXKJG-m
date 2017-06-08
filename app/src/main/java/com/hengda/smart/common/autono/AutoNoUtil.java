package com.hengda.smart.common.autono;

import org.altbeacon.beacon.Beacon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 作者：Tailyou （祝文飞）
 * 时间：2016/4/20 09:38
 * 邮箱：tailyou@163.com
 * 描述：
 */
public class AutoNoUtil {
    static final int LINKED_LIST_SIZE = 5;
    static final int BEACON_NO_NUM_THRESHOLD = 3;
    static LinkedList<Integer> linkedAutoNoList = new LinkedList<>();
    static List<Beacon> beaconList = new ArrayList<>();


    /**
     * 添加 Beacons
     *
     * @param beacons
     */
    public static void addBeacons(List<Beacon> beacons) {
        beaconList.addAll(beacons);
    }


    /**
     * 找出最好的 AutoNo (多个)
     *
     * @return
     */
    public static List<Integer> getBestAutoNos() {
        List<Integer> autoNos = new ArrayList<>();

        HashMap<Integer, Integer> countMap = new HashMap<>();
        final HashMap<Integer, Double> distanceMap = new HashMap<>();
        for (Beacon beacon : beaconList) {
            int autoNo = beacon.getId2().toInt();
            int count = countMap.containsKey(autoNo) ? countMap.get(autoNo) : 0;
            double distance = distanceMap.containsKey(autoNo) ? distanceMap.get(autoNo) : 0;
            count = count + 1;
            distance = distance + beacon.getDistance();
            countMap.put(autoNo, count);
            distanceMap.put(autoNo, distance);
        }

        List<Map.Entry<Integer, Integer>> counts = new ArrayList<>(countMap.entrySet());
        Collections.sort(counts, (lhs, rhs) -> {
            if (rhs.getValue() == lhs.getValue()) {
                return distanceMap.get(rhs.getKey()) < distanceMap.get(lhs.getKey()) ? 1 : -1;
            } else {
                return (rhs.getValue() - lhs.getValue());
            }
        });

        for (Map.Entry<Integer, Integer> map : counts) {
            autoNos.add(map.getKey());
            if (autoNos.size() == 3)
                break;
        }

        beaconList.clear();
        return autoNos;
    }


    /**
     * 添加 AutoNo
     *
     * @param autoNo
     */
    public static void addAutoNo(int autoNo) {
        if (linkedAutoNoList.size() == LINKED_LIST_SIZE)
            linkedAutoNoList.removeFirst();
        linkedAutoNoList.addLast(autoNo);
    }


    /**
     * 找出最好的 AutoNo (一个)
     *
     * @return
     */
    public static int getBestAutoNo() {
        int bestAutoNo = 0;
        if (linkedAutoNoList.size() > BEACON_NO_NUM_THRESHOLD) {
            HashMap<Integer, Integer> countMap = new HashMap<>();
            for (Integer integer : linkedAutoNoList) {
                int count = countMap.containsKey(integer) ? countMap.get(integer) : 0;
                count++;
                if (count == BEACON_NO_NUM_THRESHOLD) {
                    bestAutoNo = integer;
                    linkedAutoNoList.clear();
                    break;
                } else {
                    countMap.put(integer, count);
                }
            }
        }
        return bestAutoNo;
    }

}
