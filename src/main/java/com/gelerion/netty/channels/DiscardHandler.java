package com.gelerion.netty.channels;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;

@ChannelHandler.Sharable
public class DiscardHandler extends ChannelInboundHandlerAdapter {

    //When a ChannelInboundHandler implementation overrides channelRead(), it is responsible for explicitly releasing
    //the memory associated with pooled ByteBuf instances. Netty provides a utility method for
    //this purpose, ReferenceCountUtil.release()
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ReferenceCountUtil.release(msg);
    }

    //Netty logs unreleased resources with a WARN-level log message, making it fairly simple to find offending instances
    //in the code. But managing resources in this way can be cumbersome. A simpler alternative is to use SimpleChannelInboundHandler.
    @Sharable
    static class SimpleDiscardHandler extends SimpleChannelInboundHandler<Object> {

        //Because SimpleChannelInboundHandler releases resources automatically, you shouldn’t store references to any
        //messages for later use, as these will become invalid.
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
            //no need to do anything special
        }
    }
}
