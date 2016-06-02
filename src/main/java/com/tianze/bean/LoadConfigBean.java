package com.tianze.bean;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Description:
 * Author: Wolf
 * Created:Wolf-(2015-09-28 09:39)
 * Version: 1.0
 * Updated:
 */
@Component
public class LoadConfigBean {
    @Value("${my.outAddress}")
    private String outIP = "";
    @Value("${my.inAddress}")
    private String inIp = "";

    @Value("${my.outPort}")
    private String outPort = "";
    @Value("${my.inPort}")
    private String inPort = "";

    @Value("${my.maxOnline}")
    private int maxOnline = 3000;

    public LoadConfigBean() {

    }

    public String getOutIP() {
        return outIP;
    }

    public void setOutIP(String outIP) {
        this.outIP = outIP;
    }

    public String getInIp() {
        return inIp;
    }

    public void setInIp(String inIp) {
        this.inIp = inIp;
    }

    public String getOutPort() {
        return outPort;
    }

    public void setOutPort(String outPort) {
        this.outPort = outPort;
    }

    public String getInPort() {
        return inPort;
    }

    public void setInPort(String inPort) {
        this.inPort = inPort;
    }

    public int getMaxOnline() {
        return maxOnline;
    }

    public void setMaxOnline(int maxOnline) {
        this.maxOnline = maxOnline;
    }
}
