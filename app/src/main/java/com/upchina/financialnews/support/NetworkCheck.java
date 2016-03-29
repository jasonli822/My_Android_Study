package com.upchina.financialnews.support;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.upchina.financialnews.DemoApplication;

public class NetworkCheck {
    private NetworkCheck() {

    }

    public static boolean isConnectingToInternet() {
        if (isWifi() || isMobileNetwork()) {
            return true;
        }

        return false;
    }


    public static boolean isWifi() {
        ConnectivityManager connectMgr = (ConnectivityManager)  DemoApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectMgr.getActiveNetworkInfo();
        if (info != null && info.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    public static boolean isMobileNetwork() {
        ConnectivityManager connectMgr = (ConnectivityManager)  DemoApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectMgr.getActiveNetworkInfo();
        if (info != null && info.getType() == ConnectivityManager.TYPE_MOBILE) {
            return true;
        }

        return false;
    }

    public static boolean is2G() {
        //TODO
        return false;
    }

    public static boolean is3G() {
        //TODO
        return false;
    }
}
