package com.gelerion.netty.initializers.delimited.protocols;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.handler.codec.LineBasedFrameDecoder;

public class LineBasedHandlerInitializer extends ChannelInitializer<Channel> {

    /*
    If you’re working with frames delimited by something other than line endings, you can use the
    DelimiterBasedFrameDecoder in a similar fashion, specifying the specific delimiter sequence to the constructor.
     */

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //forwards extracted frames to the next handler
        pipeline.addLast(new LineBasedFrameDecoder(64 * 1024));
    }

    public static final class FrameHandler extends SimpleChannelInboundHandler<ByteBuf> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
            //do something with the data extracted from the frame
        }
    }
}
