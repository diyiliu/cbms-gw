package com.tianze.server;

import com.tianze.handel.codec.ByteDecode;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;


/**
 * Description:
 * Author: Wolf
 * Created:Wolf-(2015-08-31 14:29)
 * Version: 1.0
 * Updated:
 */
public class TcpServer implements IServer {
    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private EventLoopGroup acceptorGroup;
    private EventLoopGroup ioGroup;
    private EventLoopGroup shareGroup;
    private int PORT;
    private ServerBootstrap bootstrap;

    private ChannelInboundHandlerAdapter handel;

    public void setHandel(ChannelInboundHandlerAdapter handel) {
        this.handel = handel;
    }

    public void setAcceptorGroup(EventLoopGroup acceptorGroup) {
        this.acceptorGroup = acceptorGroup;
    }

    public void setIoGroup(EventLoopGroup ioGroup) {
        this.ioGroup = ioGroup;
    }

    public void setShareGroup(EventLoopGroup shareGroup) {
        this.shareGroup = shareGroup;
    }

    public ServerBootstrap getBootstrap() {
        return bootstrap;
    }

    public void setBootstrap(ServerBootstrap bootstrap) {
        this.bootstrap = bootstrap;
    }

    public void setPORT(int PORT) {
        this.PORT = PORT;
    }

    @Override
    public void init() {
        //绑定处理group accept 和IO 可以共用一个

        //处理接收事件（类似线程池）
        //EventLoopGroup acceptorGroup = new NioEventLoopGroup();
        //处理accept的I/O事件
        //EventLoopGroup ioGroup = new NioEventLoopGroup();


        bootstrap.group(acceptorGroup, ioGroup)
                .channel(NioServerSocketChannel.class)//绑定处理的通道
                .handler(new LoggingHandler(LogLevel.INFO))//绑定日志
                .childOption(ChannelOption.SO_KEEPALIVE, true)//保持长连接？
                .childOption(ChannelOption.TCP_NODELAY, true)//立即发送
                        //输入连接指示（对连接的请求）的最大队列长度被设置为 backlog 参数。如果队列满时收到连接指示，则拒绝该连接。
                .option(ChannelOption.SO_BACKLOG, 100)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childHandler(new ChannelInitializer<SocketChannel>() {//I/O事件处理的相关绑定
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast("decode", new ByteDecode());
                        pipeline.addLast("heartBeat", new IdleStateHandler(30, 30, 30, TimeUnit.SECONDS));
                        pipeline.addLast("readHandel", handel);
                    }
                });
        this.start();
    }


    @Override
    public void start() {
        bootstrap.bind(PORT);
        /*try {
            bootstrap.bind(PORT).channel().closeFuture().sync();
        } catch (InterruptedException e) {
            LOGGER.error("server start error:", e);
            //e.printStackTrace();
        } finally {
            LOGGER.error("");
            acceptorGroup.shutdownGracefully();
            ioGroup.shutdownGracefully();
        }*/
    }

}
