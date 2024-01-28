/*
 * Copyright (C) 2011 The Android Open Source Project
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


import android.app.NotificationManager;
import android.content.Intent;
import android.net.VpnService;
import android.os.ParcelFileDescriptor;
import android.util.Pair;

import com.happycbbboy.vpn_lib.VPNOptions;
import com.happycbbboy.vpn_lib.domain.Constants;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

public class TunVpnService extends VpnService {
    private static volatile TunVpnService INSTANCE;

    Thread thread;
    NotificationManager notificationManager;

    private final AtomicReference<Thread> mConnectingThread = new AtomicReference<>();

    private final AtomicReference<Connection> mConnection = new AtomicReference<>();

    public static TunVpnService getInstance() {
        if (INSTANCE == null) {
            synchronized (TunVpnService.class) {
                if (INSTANCE == null) {
                    INSTANCE = new TunVpnService();
                }
            }
        }
        return INSTANCE;
    }

    private static class Connection extends Pair<Thread, ParcelFileDescriptor> {
        public Connection(Thread thread, ParcelFileDescriptor pfd) {
            super(thread, pfd);
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        VPNOptions vpnParam = (VPNOptions) intent.getExtras().get(Constants.CONFIG);

        // Replace any existing connecting thread with the  new one.
        CbbboyVpnConnection connection = CbbboyVpnConnection.getInstance(this,vpnParam);
        thread = new Thread(connection, "cbbboy vpn service thread");

        final Thread oldThread =  mConnectingThread.getAndSet(thread);
        if (oldThread != null) {
            oldThread.interrupt();
        }

        // Handler to mark as connected once onEstablish is called.
//        connection.setConfigureIntent(mConfigureIntent);
        connection.setOnEstablishListener(tunInterface -> {
//            mConnectingThread.compareAndSet(thread, null);
            setConnection(new Connection(thread, tunInterface));
        });
        thread.start();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopVpn();
    }


    private void closeThread() {
        final Thread oldThread = mConnectingThread.getAndSet(thread);
        if (oldThread != null) {
            oldThread.interrupt();
        }
        thread = null;
    }

    private void setConnection(final Connection connection) {
        final Connection oldConnection = mConnection.getAndSet(connection);
        if (oldConnection != null) {
            try {
                if (oldConnection.first != null) {
                    oldConnection.first.interrupt();
                }
                if (oldConnection.second != null) {
                    oldConnection.second.close();
                }

            } catch (IOException e) {
//                SDKController.postMessage(LOG_INFO.getCode(), "Closing VPN interface,err:" + e.getMessage());
                e.printStackTrace();
            }
        }
    }


    private void stopVpn() {
        closeThread();
        setConnection(null);
        this.stopForeground(true);
        stopSelf();
    }




}
