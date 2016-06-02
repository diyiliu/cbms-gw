package com.tianze.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tianze.bean.LoadConfigBean;
import com.tianze.handel.ServerHandel;
import com.tianze.handel.TlbsClientHandel;
import com.tianze.handel.codec.ByteDecode;
import com.tianze.server.IServer;
import com.tianze.utils.CommonUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 * Author: Wolf
 * Created:Wolf-(2015-09-08 15:47)
 * Version: 1.0
 * Updated:
 */
public class TlbsClient implements IClient {
    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private EventLoopGroup ioGroup;
    private int PORT;
    private String HOST;
    private Bootstrap bootstrap;
    private TlbsClient tlbsClient;

    @Resource
    private LoadConfigBean configBean;

    @Resource
    private ObjectMapper objectMapper;


    public static ChannelFuture channelFuture;

    private static final int RECONNECT_TIME = 5;

    public EventLoopGroup getIoGroup() {
        return ioGroup;
    }

    public void setIoGroup(EventLoopGroup ioGroup) {
        this.ioGroup = ioGroup;
    }

    public int getPORT() {
        return PORT;
    }

    public void setPORT(int PORT) {
        this.PORT = PORT;
    }

    public String getHOST() {
        return HOST;
    }

    public void setHOST(String HOST) {
        this.HOST = HOST;
    }

    public Bootstrap getBootstrap() {
        return bootstrap;
    }

    public void setBootstrap(Bootstrap bootstrap) {
        this.bootstrap = bootstrap;
    }

    public TlbsClient() {
        tlbsClient = this;
    }

    @Override
    public void init() {
        bootstrap.group(ioGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline p = ch.pipeline();
                        p.addLast("decode", new ByteDecode());
                        p.addFirst(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                                super.channelInactive(ctx);
                                ctx.channel().eventLoop().schedule(new Runnable() {
                                    @Override
                                    public void run() {
                                        tlbsClient.start();
                                    }
                                }, RECONNECT_TIME, TimeUnit.SECONDS);
                            }
                        });
                        p.addLast("heartBeat", new IdleStateHandler(30, 30, 30, TimeUnit.SECONDS));
                        p.addLast("tlbsHandle", new TlbsClientHandel());
                    }
                });
        this.start();

    }

    @Override
    public void start() {
        // Start the client.
        //try {
        channelFuture  = bootstrap.connect(HOST, PORT).addListener(new ChannelFutureListener() {
            /**
             * Invoked when the operation associated with the {@link io.netty.util.concurrent.Future} has been completed.
             *
             * @param future the source {@link io.netty.util.concurrent.Future} which called this callback
             */
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    LOGGER.info("connect tlbs success");
                    sendLoad();
                } else {
                    LOGGER.error("connect tlbs error,reconnecting.........");
                    future.channel().eventLoop().schedule(new Runnable() {
                        @Override
                        public void run() {
                            tlbsClient.start();
                        }
                    }, RECONNECT_TIME, TimeUnit.SECONDS);
                }
            }
        });

        if (null != channelFuture && channelFuture.isSuccess()) {
            LOGGER.info("connect tlbs success");
            this.sendLoad();
        }
        //f.sync();
        //f.channel().closeFuture().sync();
        /*} catch (InterruptedException e) {
            LOGGER.error("sync error:", e);
        } finally {
            // Shut down the event loop to terminate all threads.
            ioGroup.shutdownGracefully();
        }*/
    }



    private void sendLoad(){
        String onlineStr = "";
        try {
            onlineStr = objectMapper.writeValueAsString(new ConcurrentHashMap(){{
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
            ByteBuf bf = CommonUtils.parkTlbs(buffer.array(), 0x01);
            channelFuture.channel().writeAndFlush(bf);

        } catch (JsonProcessingException e) {
            LOGGER.error("send tlbs online error:", e);
            //e.printStackTrace();
        }

    }
}
