package com.hengda.smart.common.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public abstract class NetStateMonitor extends BroadcastReceiver {

    public abstract void onConnected();

    public abstract void onDisconnected();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (NetUtil.isConnected(context)) {
            onConnected();
        } else {
            onDisconnected();
        }
    }

}
