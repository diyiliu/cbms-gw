package com.tianze.handel.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * Description:
 * Author: Wolf
 * Created:Wolf-(2015-09-01 10:03)
 * Version: 1.0
 * Updated:
 */
public class MessDecode extends MessageToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, Object msg, List out) throws Exception {
        System.out.println("come here");
        System.out.println("mess type:" + msg.getClass().getName());
        System.out.println("end");
    }
}
