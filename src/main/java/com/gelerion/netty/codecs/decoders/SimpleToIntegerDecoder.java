package com.gelerion.netty.codecs.decoders;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/*
Although ByteToMessageDecoder makes this pattern simple to implement, you might find it a bit annoying to have to
verify that the input ByteBuf has enough data for you to call readInt().
ReplayingDecoder, a special decoder that eliminates this step, at the cost of a small amount of overhead.
 */
public class SimpleToIntegerDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() >= 4) {
            out.add(in.readInt());
        }
    }
}
