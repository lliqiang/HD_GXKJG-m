package com.hengda.smart.guangxitech;


import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;


public class GetDataActivity extends UnityPlayerActivity {
    private long mExitTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void showToast(final String msg) {
        EventBus.getDefault().post(msg);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitBy2Click();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    public void exitBy2Click() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Toast.makeText(this, "再次点击退出", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            mUnityPlayer.quit();

        }
    }

}
