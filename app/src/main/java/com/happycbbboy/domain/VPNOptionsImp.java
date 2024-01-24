package com.happycbbboy.domain;

import android.util.Log;

import com.happycbbboy.vpn_lib.VPNOptions;

public class VPNOptionsImp extends VPNOptions {
    @Override
    public void LogBack(String s) {
        Log.d("VPN-SERVICE",s);
    }
}
