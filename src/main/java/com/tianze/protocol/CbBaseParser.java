package com.tianze.protocol;

import com.tianze.bean.GpsInfoBean;
import com.tianze.utils.CommonUtils;
import com.tianze.utils.EnumConfig;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.Date;

/**
 * Description:
 * Author: Wolf
 * Created:Wolf-(2015-09-02 15:35)
 * Version: 1.0
 * Updated:
 */
public abstract class CbBaseParser extends AbstractBaseParser<Integer> {

    protected void responseGpsClient(ChannelHandlerContext conn, GpsInfoBean bean, byte[] content) {
        short length = (short) (EnumConfig.CommonConfig.MAIN_LENGTH + content.length);
        ByteBuf bf = conn.alloc().buffer(length);
        bf.writeShort(length);
        bf.writeByte(EnumConfig.CommonConfig.PROTOCOL_VERSION);
        bf.writeByte(bean.getComeFrom());
        bf.writeBytes(CommonUtils.getDateBytes(new Date()));
        bf.writeByte(EnumConfig.CommonConfig.INFO_TYPE_GPS);
        bf.writeBytes(content);
        //conn.writeAndFlush(bf);
        conn.channel().writeAndFlush(bf);
    }
}
