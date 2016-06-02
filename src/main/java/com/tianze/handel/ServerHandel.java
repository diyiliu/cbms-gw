package com.tianze.handel;

import com.tianze.bean.GpsInfoBean;
import com.tianze.cache.ICache;
import com.tianze.factory.AbstractProFactory;
import com.tianze.protocol.AbstractBaseParser;
import com.tianze.utils.CommonUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.net.InetSocketAddress;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Description:
 * Author: Wolf
 * Created:Wolf-(2015-08-31 15:36)
 * Version: 1.0
 * Updated:
 */
@ChannelHandler.Sharable
public class ServerHandel extends ChannelInboundHandlerAdapter {
    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    public static final Map VEHICLES = new ConcurrentHashMap<String, Integer>();

    @Resource(name = "cbFactory")
    private AbstractProFactory factory;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info("open...");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf bf = (ByteBuf) msg;
        bf.markReaderIndex();
        int infoLen = bf.readUnsignedShort();
        int protocolVersion = bf.readUnsignedByte();
        int comeForm = bf.readUnsignedByte();
        byte[] gpsTimeBytes = new byte[6];
        bf.readBytes(gpsTimeBytes);
        Date gpsDate = CommonUtils.getGpsTime(gpsTimeBytes);
        int infoType = bf.readUnsignedByte();
        int cmdId = bf.readUnsignedByte();
        byte[] messages = new byte[bf.readableBytes()];
        bf.readBytes(messages);
        AbstractBaseParser parser = (AbstractBaseParser) factory.getCmd(cmdId);
        if (null == parser) {
            LOGGER.error("不存在{}指令ID的解析方法！", Integer.toHexString(cmdId));
        }
        GpsInfoBean bean = new GpsInfoBean(infoLen, protocolVersion, comeForm, gpsDate, infoType, cmdId, messages);
        parser.parser(bean, ctx);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {

        //LOGGER.info("read over...");
        super.channelReadComplete(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        String address = ctx.channel().remoteAddress().toString();
        //心跳处理
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                //读超时
                //LOGGER.warn("READER_IDLE 读超时");
            } else if (event.state() == IdleState.WRITER_IDLE) {
                //写超时
                //LOGGER.warn("WRITER_IDLE 写超时");
            } else if (event.state() == IdleState.ALL_IDLE) {
                //总超时

                //LOGGER.warn("ALL IDLE");
            }
        }
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //ctx.channel().closeFuture().sync();
        if (cause instanceof java.io.IOException) {
            String address = ctx.channel().remoteAddress().toString();
            //cause.printStackTrace();
            LOGGER.error(address + " Socket连接异常:", cause.getMessage());
            ctx.close();
        } else {
            LOGGER.error("错误:", cause);
            super.exceptionCaught(ctx , cause);
        }
    }
}
