package com.tianze.handel.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Description:
 * Author: Wolf
 * Created:Wolf-(2015-08-31 15:34)
 * Version: 1.0
 * Updated:
 */
public class ByteDecode extends ByteToMessageDecoder {
    private Logger LOGGER = LoggerFactory.getLogger(ByteDecode.class);
    private static final int HEAD_LENGTH = 2;
    /**
     * Decode the from one {@link ByteBuf} to an other. This method will be called till either the input
     * {@link ByteBuf} has nothing to read when return from this method or till nothing was read from the input
     * {@link ByteBuf}.
     *
     * @param ctx the {@link ChannelHandlerContext} which this {@link ByteToMessageDecoder} belongs to
     * @param in  the {@link ByteBuf} from which to read data
     * @param out the {@link List} to which decoded messages should be added
     * @throws Exception is thrown if an error accour
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if(in.readableBytes() < HEAD_LENGTH){
            //LOGGER.info("message len lt 2 byte");
            return ;
        }
        in.markReaderIndex();
        int len = in.readUnsignedShort();
        if(in.readableBytes()<(len - HEAD_LENGTH)){
            //LOGGER.info("message len not enough!");
            in.resetReaderIndex();
            return ;
        }
        in.resetReaderIndex();
        byte[] messages = new byte[len];
        in.readBytes(messages);
        ByteBuf bf = ctx.alloc().buffer(len);
        bf.writeBytes(messages);
        out.add(bf);
    }
}
