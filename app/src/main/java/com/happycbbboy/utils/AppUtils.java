package com.happycbbboy.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class AppUtils {
    static PackageManager packageManager;
//    static List<PackageInfo> packagesCache;
    static List<ApplicationInfo> packagesCache;
//    static List<ResolveInfo> packagesCache;
    static List<AppInfo> appInfoList;

    public static synchronized List<AppInfo> getAllInstalledApps(Context context, String keyWord, List<String> includePackage) {
        if (packageManager == null) {
            packageManager = context.getPackageManager();
        }

        if (packagesCache == null || packagesCache.size() != 0) {
//            Intent intent = new Intent(Intent.ACTION_MAIN, null);
//            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            packagesCache = packageManager.getInstalledApplications(0);
//            packagesCache = packageManager.queryIntentActivities(intent,0);
        }
        ArrayList<AppInfo> resAppInfoList = new ArrayList<>();

        // 获取所有已安装应用的信息
        if (appInfoList == null || appInfoList.size() == 0) {
            appInfoList = new ArrayList<>(packagesCache.size());
//            for (ResolveInfo packageInfo : packagesCache) {
                for (ApplicationInfo packageInfo : packagesCache) {
                Drawable appIcon = packageInfo.loadIcon(packageManager);
                String appName = packageInfo.loadLabel(packageManager).toString();
                String packageName = packageInfo.packageName;
//                String packageName = packageInfo.activityInfo.packageName;

                AppInfo appInfo = new AppInfo(appName, packageName, appIcon);
                appInfoList.add(appInfoList.size(), appInfo);

/*                if (keyWord != null && !"".equals(keyWord) && !packageName.contains(keyWord) && !appName.contains(keyWord)) {
                    continue;
                }*/
                if (includePackage.contains(packageName)) {
                    appInfo.setCheck(true);
//                    appInfoList.add(0, appInfo);
                    resAppInfoList.add(0, appInfo);
                    continue;
                }
                appInfo.setCheck(false);
                resAppInfoList.add(appInfo);
            }
        } else {
            for (AppInfo appInfo : appInfoList) {
/*                if (keyWord != null && !"".equals(keyWord) && !appInfo.getPackageName().contains(keyWord) && !appInfo.getAppName().contains(keyWord)) {
                    continue;
                }*/
                if (includePackage.contains(appInfo.getPackageName())) {
                    appInfo.setCheck(true);
                    resAppInfoList.add(0,appInfo);
                    continue;
                }
                appInfo.setCheck(false);
                resAppInfoList.add(appInfo);
            }
        }


        Log.i("RouteConfManagerFragment", "appInfoList len:" + appInfoList.size());
        return resAppInfoList;
    }

    public static class AppInfo {
        private String appName;
        private String packageName;
        private Drawable appIcon;
        private boolean check;
//        private boolean iniTial  = false;
//        private AppItermAdapter.ViewHolder appItermViewHolder;

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

        public Boolean getCheck() {
            return check;
        }

      /*  public AppItermAdapter.ViewHolder getAppItermViewHolder() {
            return appItermViewHolder;
        }

        public void setAppItermViewHolder(AppItermAdapter.ViewHolder appItermViewHolder) {
            this.appItermViewHolder = appItermViewHolder;
        }

        public Boolean getIniTial() {
            return iniTial;
        }

        public void setIniTial(boolean iniTial) {
            this.iniTial = iniTial;
        }*/

        public void setCheck(Boolean check) {
            this.check = check;
        }

        @Override
        public String toString() {
            return "AppInfo{" +
                    "appName='" + appName + '\'' +
                    ", packageName='" + packageName + '\'' +
                    ", appIcon=" + appIcon +
                    ", check=" + check +
                    '}';
        }
    }
}
/*
<uses-permission android:name="android.permission.GET_TASKS" />
 */