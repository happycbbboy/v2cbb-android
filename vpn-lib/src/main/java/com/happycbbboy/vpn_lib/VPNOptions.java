package com.happycbbboy.vpn_lib;

import java.io.Serializable;
import java.util.List;

public abstract class VPNOptions implements Serializable {
    private String proxyConf;
    private List<String> route;
    private List<String> includePackage;
    private List<String> excludePackage;

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

    public abstract void LogBack(String s);

}
