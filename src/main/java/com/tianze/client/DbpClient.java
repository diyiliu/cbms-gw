package com.tianze.client;

import com.tianze.cache.ICache;
import com.tianze.handel.DbpClientHandel;
import com.tianze.handel.codec.DbpDecode;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 * Author: Wolf
 * Created:Wolf-(2015-09-16 13:57)
 * Version: 1.0
 * Updated:
 */
public class DbpClient implements IClient, Runnable {
    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private EventLoopGroup ioGroup;
    private int PORT;
    private String HOST;
    private Bootstrap bootstrap;
    public static ChannelFuture channelFuture;
    private boolean isConnected = false;
    public static Queue<String> queue = new ConcurrentLinkedQueue<String>();
    private static final int RECONNECT_TIME = 5;
    private static final long witeConnTime = 100;
    private DbpClient dbpClient;

    @Resource
    private ICache monitorCacheProvider;

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

    public DbpClient() {
        dbpClient = this;
        new Thread(dbpClient, "DBP_CLIENT").start();
    }

    @Override
    public void init() {
        bootstrap.group(ioGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline p = ch.pipeline();
                        p.addLast("decode", new DbpDecode());

                        p.addFirst(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                                super.channelInactive(ctx);
                                ctx.channel().eventLoop().schedule(new Runnable() {
                                    @Override
                                    public void run() {
                                        dbpClient.start();
                                    }
                                }, RECONNECT_TIME, TimeUnit.SECONDS);
                            }
                        });
                        p.addLast("heartBeat", new IdleStateHandler(30, 30, 30, TimeUnit.SECONDS));
                        p.addLast("dbpHandle", new DbpClientHandel());
                    }
                });
        this.start();

    }

    @Override
    public void start() {
        // Start the client.
        //try {
        channelFuture = bootstrap.connect(HOST, PORT).addListener(new ChannelFutureListener() {
            /**
             * Invoked when the operation associated with the {@link io.netty.util.concurrent.Future} has been completed.
             *
             * @param future the source {@link io.netty.util.concurrent.Future} which called this callback
             */
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    isConnected = true;
                    LOGGER.info("connect dbp success");
                } else {
                    isConnected = false;
                    LOGGER.error("connect dbp error,reconnecting.........");
                    future.channel().eventLoop().schedule(new Runnable() {
                        @Override
                        public void run() {
                            dbpClient.start();
                        }
                    }, RECONNECT_TIME, TimeUnit.SECONDS);
                }
            }
        });
        if (null != channelFuture && channelFuture.isSuccess()) {
            isConnected = true;
            LOGGER.info("connect dbp success");
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

    @Override
    public void run() {
        int len = 0;
        while (true) {
            if (this.isConnected && null != channelFuture && channelFuture.channel().isActive() && !queue.isEmpty()) {
                try {
                    String str = queue.poll();
                    LOGGER.debug(str);
                    len = str.getBytes("UTF-8").length + 3;
                    ByteBuf buffer = channelFuture.channel().alloc().buffer(len);
                    buffer.writeShort((short) len);
                    buffer.writeByte((byte) 1);
                    buffer.writeBytes(str.getBytes("UTF-8"));
                    channelFuture.channel().writeAndFlush(buffer);
                } catch (Exception e) {
                    LOGGER.error("发送SQL语句错误：" + e);
                }
            } else {
                //logger.info("没有数据，或者未连接上DBP");
                try {
                    Thread.sleep(witeConnTime);
                } catch (InterruptedException e) {
                    LOGGER.error("Thread to be interrupted ", e);
                }

            /*try {
                synchronized (this) {
                    this.wait(witeConnTime);
                }
            } catch (InterruptedException e) {
                logger.error("Thread to be interrupted ", e);
            } catch (Exception e) {
                logger.error("Storage failure", e);
            }*/
            }
        }
    }
}
