package com.liicon.talk.utils;

import android.util.Log;

/**
 * Created by wei on 15-11-10.
 */
public class LogUtils {

    private static String APP_TAG = "com.liicon.im";

    public static  final void i(String tag, String msg) {
        Log.i(APP_TAG, tag + ", " + msg);
    }

    public static  final void e(String tag, String msg) {
        Log.e(APP_TAG, tag + ", " + msg);
    }
}
