package com.happycbbboy.domain;

public enum EventBusMsg {
    OPEN_PROXY_CONFIG("OPEN_PROXY_CONFIG"),
    OPEN_ROUTE_CONFIG("OPEN_ROUTE_CONFIG"),
    PROXY_SETTING_SUBMIT("PROXY_SETTING_SUBMIT"),
    ROUTE_SETTING_SUBMIT("ROUTE_SETTING_SUBMIT"),
    ;
    private String msg;

    EventBusMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
