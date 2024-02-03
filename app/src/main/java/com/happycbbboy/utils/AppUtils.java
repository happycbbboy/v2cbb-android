package com.happycbbboy.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class AppUtils {
    static PackageManager packageManager;
    static List<PackageInfo> packagesCache;

    public static List<AppInfo> getAllInstalledApps(Context context, List<String> includePackage, List<String> excludePackage) {
        if ( packageManager == null) {
            packageManager = context.getPackageManager();
        }

        if ( packagesCache == null) {
            packagesCache = packageManager.getInstalledPackages(PackageManager.GET_SERVICES);
        }
        // 获取所有已安装应用的信息
        List<AppInfo> appInfoList = new ArrayList<>(packagesCache.size());

        for (PackageInfo packageInfo : packagesCache) {
            String appName = packageInfo.applicationInfo.loadLabel(packageManager).toString();
            String packageName = packageInfo.packageName;
            Drawable appIcon = packageInfo.applicationInfo.loadIcon(packageManager);
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                continue;
            }
            AppInfo appInfo = new AppInfo(appName, packageName, appIcon);
            if (includePackage.contains(packageName)) {
                appInfo.setPolicy(AppInfo.TUNNEL);
                appInfoList.add(0, appInfo);
                continue;
            }
            if (excludePackage.contains(packageName)) {
                appInfo.setPolicy(AppInfo.FREEE);
                appInfoList.add(Math.min(appInfoList.size(), includePackage.size()), appInfo);
                continue;
            }
            appInfoList.add(appInfoList.size(), appInfo);
        }


        Log.i("RouteConfManagerFragment", "appInfoList len:" + appInfoList.size());
        return appInfoList;
    }

    public static class AppInfo {
        public final static int NORMAL = 0;
        public final static int FREEE = 1;
        public final static int TUNNEL = 2;
        private String appName;
        private String packageName;
        private Drawable appIcon;
        private int policy = NORMAL;

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

        public int getPolicy() {
            return policy;
        }

        public void setPolicy(int policy) {
            this.policy = policy;
        }
    }
}
/*
<uses-permission android:name="android.permission.GET_TASKS" />
 */