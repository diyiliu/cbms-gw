package com.tianze.handel.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Description:
 * Author: Wolf
 * Created:Wolf-(2015-09-16 16:36)
 * Version: 1.0
 * Updated:
 */
public class DbpDecode extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        in.markReaderIndex();
        // 数据包长度
        int datalen = in.readShort();
        if (in.readableBytes() < (datalen - 2)) {
            in.resetReaderIndex();
            return;
        }
        in.resetReaderIndex();
        byte[] messages = new byte[datalen];
        in.readBytes(messages);
        ByteBuf bf = ctx.alloc().buffer(datalen);
        bf.writeBytes(messages);
        out.add(bf);

    }
}
