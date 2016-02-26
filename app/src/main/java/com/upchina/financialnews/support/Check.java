package com.upchina.financialnews.support;


import android.content.Intent;
import android.content.pm.PackageManager;

import com.upchina.financialnews.UpChinaFinancialNewsApplication;

public class Check {
    public static boolean isIntentSafe(Intent intent) {
        return preparePackageManager().queryIntentActivities(intent, 0).size() > 0;
    }



    private static PackageManager preparePackageManager() {
        return UpChinaFinancialNewsApplication.getInstance().getPackageManager();
    }
}
