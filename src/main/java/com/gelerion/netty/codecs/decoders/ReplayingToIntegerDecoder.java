package com.gelerion.netty.codecs.decoders;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

//The parameter S specifies the type to be used for state management, where Void indicates that none is to be performed.
public class ReplayingToIntegerDecoder extends ReplayingDecoder<Void> {

    /*
    As before, ints extracted from the ByteBuf are added to the List. If insufficient bytes are available, this
    implementation of readInt() throws an Error that will be caught and handled in the base class. The decode()
    method will be called again when more data is ready for reading.
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        //ReplayingDecoder is slightly slower than ByteToMessageDecoder.
        out.add(in.readInt());
    }
}
