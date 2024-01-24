package com.happycbbboy.vpn_lib;

import android.app.Application;
import android.content.Intent;

import androidx.annotation.Keep;

import com.happycbbboy.vpn_lib.domain.Constants;
import com.happycbbboy.vpn_lib.tun.TunActivity;


public class VPNInterface {
    @Keep
    public static synchronized boolean Start(Application application, VPNOptions options) {
        Intent intent = new Intent(application, TunActivity.class);
        intent.putExtra(Constants.CONFIG, options);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        application.startActivity(intent);

        return true;
    }

    @Keep
    public static synchronized void Stop() {
    }
}
