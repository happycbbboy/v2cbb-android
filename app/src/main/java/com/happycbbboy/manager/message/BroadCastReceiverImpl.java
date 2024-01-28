package com.happycbbboy.manager.message;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.happycbbboy.vpn_lib.manager.Notify;

import java.util.Objects;


public class BroadCastReceiverImpl extends BroadcastReceiver {
    private Context context;

    public BroadCastReceiverImpl() {
    }

    public BroadCastReceiverImpl(Context context) {
        this.context = context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Notify vpnParam = (Notify) intent.getExtras().get(Notify.PARAM_KEY);
        if (vpnParam != null) {
            Toast.makeText(context, vpnParam.getTitle() + ":" + vpnParam.getMsg(), Toast.LENGTH_SHORT).show();
            if (!Objects.equals(vpnParam.getCode(), Notify.SUCCESS)) {
                Log.e("VPN_SERVICE", vpnParam.getError());
            }
        }
    }
}
