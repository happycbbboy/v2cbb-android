/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.happycbbboy.vpn_lib.tun;


import android.app.PendingIntent;
import android.content.Intent;
import android.net.VpnService;
import android.os.ParcelFileDescriptor;

import com.happycbbboy.vpn_lib.VPNOptions;
import com.happycbbboy.vpn_lib.manager.Notify;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import sdk.Param;
import sdk.Sdk;

public class CbbboyVpnConnection implements Runnable {
    private static volatile CbbboyVpnConnection INSTANCE;
    private VPNOptions options;

    public static CbbboyVpnConnection getInstance( VpnService service, VPNOptions param) {
        if (INSTANCE == null) {
            synchronized (TunVpnService.class) {
                if (INSTANCE == null) {
                    INSTANCE = new CbbboyVpnConnection(service);
                }
            }
        }
        INSTANCE.options = param;
        return INSTANCE;
    }


    /**
     * Callback interface to let the {@link VpnService} know about new connections
     * and update the foreground notification with connection status.
     */
    public interface OnEstablishListener {
        void onEstablish(ParcelFileDescriptor tunInterface);
    }

    /**
     * Maximum packet size is constrained by the MTU, which is given as a signed short.
     */
    private static final int MAX_PACKET_SIZE = Short.MAX_VALUE;


    private VpnService vpnService;

    ParcelFileDescriptor iface = null;
    FileOutputStream out = null;
    private PendingIntent mConfigureIntent;
    private OnEstablishListener mOnEstablishListener;


    // Allowed/Disallowed packages for VPN usage
    public CbbboyVpnConnection( final VpnService service) {
        this.vpnService = service;
    }

    /**
     * Optionally, set an intent to configure the VPN. This is {@code null} by default.
     */
    public void setConfigureIntent(PendingIntent intent) {
        mConfigureIntent = intent;
    }

    public void setOnEstablishListener(OnEstablishListener listener) {
        mOnEstablishListener = listener;
    }

    @Override
    public void run() {
        if (iface != null) {
            try {
                iface.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            iface = null;
        }
        if (out != null) {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            out = null;
        }
        synchronized (this) {
            // Create a DatagramChannel as the VPN tunnel.
            try {
                // 启动sdk
                // init对象
                Param param = new Param() {
                    @Override
                    public void callBackPacket(byte[] bytes) {
                        try {
                            out.write(bytes);
                        } catch (Exception e) {
                            e.printStackTrace();
                            out = null;
                        }
                    }

                    @Override
                    public String getParam() {
                        return options.getProxyConf();
                    }

                    @Override
                    public void logBack(String s) {
                        options.LogBack(s);
                    }

                    @Override
                    public boolean protect(long l) {
                        return vpnService.protect((int) l);
                    }
                };

                Sdk.stop();
                Sdk.start(param);
                iface = configure();
                FileInputStream in = new FileInputStream(iface.getFileDescriptor());
                out = new FileOutputStream(iface.getFileDescriptor());

                Intent vpnSuccessIntent = new Intent(Notify.VPN_CONNECT_ACTION);
                vpnSuccessIntent.putExtra(Notify.PARAM_KEY,new Notify(Notify.SUCCESS,"VPN_SERVICE","vpn 启动成功"));
                vpnService.sendBroadcast(vpnSuccessIntent);

                ByteBuffer packet = ByteBuffer.allocate(MAX_PACKET_SIZE);
                while (true) {
                    int length = in.read(packet.array());
                    if (length > 0) {
                        packet.limit(length);
                        Sdk.inputPacket(packet.array());
                        packet.clear();
                    }
                }
            } catch (Exception e) {
                Intent vpnSuccessIntent = new Intent(Notify.VPN_CONNECT_ACTION);
                vpnSuccessIntent.putExtra(Notify.PARAM_KEY,new Notify(Notify.ERROR,"VPN_SERVICE",e.getMessage(), e.getMessage()));
                vpnService.sendBroadcast(vpnSuccessIntent);
            } finally {
                if (iface != null) {
                    try {
                        iface.close();
                        if (out != null) {
                            out.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private ParcelFileDescriptor configure() throws IllegalArgumentException {
        // 配置路由
        VpnService.Builder builder = vpnService.new Builder();
        builder.setMtu(1500);
        builder.addRoute("0.0.0.0", 0);
        builder.addAddress("10.0.0.1", 32);

        builder.addDnsServer("8.8.8.8");
        builder.addDnsServer("114.114.114.114");
        builder.setSession("cbbboy vpn").setConfigureIntent(mConfigureIntent);

        // Create a new interface using the builder and save the parameters.
        final ParcelFileDescriptor vpnInterface;
        synchronized (vpnService) {
            vpnInterface = builder.establish();
            if (mOnEstablishListener != null) {
                mOnEstablishListener.onEstablish(vpnInterface);
            }
        }
        return vpnInterface;
    }

}
