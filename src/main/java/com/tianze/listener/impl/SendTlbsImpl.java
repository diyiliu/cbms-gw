package com.tianze.listener.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tianze.bean.LoadConfigBean;
import com.tianze.client.TlbsClient;
import com.tianze.handel.ServerHandel;
import com.tianze.listener.AbstractResponse;
import com.tianze.utils.CommonUtils;
import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description:
 * Author: Wolf
 * Created:Wolf-(2015-09-22 15:58)
 * Version: 1.0
 * Updated:
 */
@Service
public class SendTlbsImpl extends AbstractResponse {

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    @Value("${tlbs.checkTime}")
    private long CHECK_TIME = 5*60*1000;
    /*@Value("#{configProperties['my.outAddress']}")
    private String OUT_IP = "";
    @Value("#{configProperties['my.inAddress']}")
    private String IN_IP = "";

    @Value("#{configProperties['my.outPort']}")
    private String OUT_PORT = "";
    @Value("#{configProperties['my.inPort']}")
    private String IN_PORT = "";

    @Value("#{configProperties['my.maxOnline']}")
    private int maxOnline = 3000;*/
    @Resource
    private LoadConfigBean configBean;

    @Resource
    private ObjectMapper objectMapper;

    @Override
    public long getCheckTime() {
        return this.CHECK_TIME;
    }

    @Override
    public void dealMessage() {
        if(TlbsClient.channelFuture.isSuccess()){
            try {
                String onlineStr = objectMapper.writeValueAsString(new ConcurrentHashMap(){{
                    this.put("onLineVtNum" , ServerHandel.VEHICLES.size());
                    this.put("maxVtNum" , configBean.getMaxOnline());
                    this.put("outAddress" ,configBean.getOutIP() );
                    this.put("inAddress" ,configBean.getInIp() );
                    this.put("outPort" ,configBean.getOutPort() );
                    this.put("inPort" ,configBean.getInPort());

                }});
                byte[] onlineBytes = onlineStr.getBytes();
                ByteBuffer buffer = ByteBuffer.allocate(5+onlineBytes.length);
                buffer.put((byte)0x02);
                buffer.putInt(onlineBytes.length);
                buffer.put(onlineBytes);

                ByteBuf bf = CommonUtils.parkTlbs(buffer.array() ,0x01);
                //bf.writeBytes(onlineBytes);
                TlbsClient.channelFuture.channel().writeAndFlush(bf);
            } catch (JsonProcessingException e) {
                LOGGER.error("send tlbs online error:", e);
            }
        }
    }
}
