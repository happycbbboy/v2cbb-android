package com.happycbbboy.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

public class AppUtils {

    public static List<AppInfo> getAllInstalledApps(Context context) {
        List<AppInfo> appInfoList = new ArrayList<>();
        PackageManager packageManager = context.getPackageManager();

        // 获取所有已安装应用的信息
        List<PackageInfo> packages = packageManager.getInstalledPackages(PackageManager.GET_META_DATA);
        for (PackageInfo packageInfo : packages) {
            String appName = packageInfo.applicationInfo.loadLabel(packageManager).toString();
            String packageName = packageInfo.packageName;
            Drawable appIcon = packageInfo.applicationInfo.loadIcon(packageManager);

            AppInfo appInfo = new AppInfo(appName, packageName, appIcon);
            appInfoList.add(appInfo);
        }

        return appInfoList;
    }

    public static class AppInfo {
        private String appName;
        private String packageName;
        private Drawable appIcon;

        public AppInfo(String appName, String packageName, Drawable appIcon) {
            this.appName = appName;
            this.packageName = packageName;
            this.appIcon = appIcon;
        }

        public String getAppName() {
            return appName;
        }

        public String getPackageName() {
            return packageName;
        }

        public Drawable getAppIcon() {
            return appIcon;
        }
    }
}
/*
<uses-permission android:name="android.permission.GET_TASKS" />
 */