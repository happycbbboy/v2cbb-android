package com.happycbbboy.vpn_lib.manager;

import java.io.Serializable;

public class Notify implements Serializable {
    public final  static String VPN_CONNECT_ACTION= "com.happycbbboy.vpn_lib.VPN_CONNECT_ACTION";
    public final  static String PARAM_KEY= "VPN_CONNECT_ACTION_NOTIFY";

    public final static Integer SUCCESS= 0;
    public final static Integer ERROR= 10;

    private Integer code;
    private String title;
    private String msg;
    private String error;

    public Notify(Integer code, String title, String msg, String error) {
        this.code = code;
        this.title = title;
        this.msg = msg;
        this.error = error;
    }
    public Notify(Integer code, String title, String msg) {
        this.code = code;
        this.title = title;
        this.msg = msg;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
