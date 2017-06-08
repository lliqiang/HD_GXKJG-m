package com.hengda.frame.fileloader.util;

import android.util.Log;

/**
 * Created by shiwei on 2017/1/11.
 */
public class L {
    private static boolean debug = false;

    public static void e(String msg) {
        if (debug) {
            Log.e("OkHttp", msg);
        }
    }

}