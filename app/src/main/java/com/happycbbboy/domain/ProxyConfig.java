package com.happycbbboy.domain;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.happycbbboy.domain.adapter.ChatItemConverter;

import java.util.List;

@Entity
@TypeConverters(ChatItemConverter.class)
public class ProxyConfig {
    @PrimaryKey(autoGenerate = true)
    public Integer id;
    @ColumnInfo(name = "name")
    String name;
    @ColumnInfo(name = "proxy_conf")
    String proxyConf;
    @ColumnInfo(name = "route")
    List<String> route;

    @ColumnInfo(name = "include_package")
    List<String> includePackage;
    @ColumnInfo(name = "exclude_package")
    List<String> excludePackage;

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

    public List<String> getRoute() {
        return route;
    }

    public void setRoute(List<String> route) {
        this.route = route;
    }

    public List<String> getIncludePackage() {
        return includePackage;
    }

    public void setIncludePackage(List<String> includePackage) {
        this.includePackage = includePackage;
    }

    public List<String> getExcludePackage() {
        return excludePackage;
    }

    public void setExcludePackage(List<String> excludePackage) {
        this.excludePackage = excludePackage;
    }
}
