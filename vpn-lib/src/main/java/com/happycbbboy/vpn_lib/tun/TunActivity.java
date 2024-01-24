package com.happycbbboy.vpn_lib.tun;

import android.app.Activity;
import android.content.Intent;
import android.net.VpnService;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.happycbbboy.vpn_lib.VPNOptions;
import com.happycbbboy.vpn_lib.domain.Constants;


public class TunActivity extends Activity {
//    private static boolean isFirstOpenVpn = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = VpnService.prepare(this);
//        initWindow();
        if (intent != null) {
            startActivityForResult(intent, 199);
        }else {
            startTunnel((VPNOptions)getIntent().getExtras().get(Constants.CONFIG));
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 199) {
            if (resultCode == -1) {
                startTunnel((VPNOptions)getIntent().getExtras().get(Constants.CONFIG));
            }
        }
        finish();
    }

    private void initWindow() {
        Window window = getWindow();
        window.setGravity(Gravity.START | Gravity.TOP);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.x = 0;
        layoutParams.y = 0;
        layoutParams.height = 1;
        layoutParams.width = 1;
        window.setAttributes(layoutParams);
    }

    private void startTunnel(VPNOptions config) {
        Intent intent = new Intent(this, TunVpnService.class);
        intent.putExtra(Constants.CONFIG, config);
        this.startService(intent);
    }
}
