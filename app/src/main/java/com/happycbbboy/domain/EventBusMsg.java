package com.happycbbboy.domain;

public enum EventBusMsg {
    SUBMIT("SUBMIT"),
    OPEN_PROXY_CONFIG("OPEN_PROXY_CONFIG"),
    START_VPN("START_VPN"),
    ;
    private String msg;

    EventBusMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
