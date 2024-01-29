package com.happycbbboy.domain;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.happycbbboy.domain.adapter.ChatItemConverter;

@Entity
@TypeConverters(ChatItemConverter.class)
public class ProxyConfig {
    @PrimaryKey(autoGenerate = true)
    Integer id;
    @ColumnInfo(name = "name")
    String name;
    @ColumnInfo(name = "proxy_conf")
    String proxyConf;
    @ColumnInfo(name = "route")
    Integer routeProxyId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProxyConf() {
        return proxyConf;
    }

    public void setProxyConf(String proxyConf) {
        this.proxyConf = proxyConf;
    }

    public Integer getRouteProxyId() {
        return routeProxyId;
    }

    public void setRouteProxyId(Integer routeProxyId) {
        this.routeProxyId = routeProxyId;
    }
}
