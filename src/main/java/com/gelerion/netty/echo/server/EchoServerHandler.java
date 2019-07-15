package com.gelerion.netty.echo.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import static java.nio.charset.StandardCharsets.UTF_8;

//Indicates that a ChannelHandler can be safely shared by multiple channels
@ChannelHandler.Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {
    //ChannelInboundHandlerAdapter has a straightforward API, and each of its methods can be overridden to
    //hook into the event lifecycle at the appropriate point

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        System.out.println("Server received: " + in.toString(UTF_8));
        //writes the received msg to the sender without flushing the outbound message
        ctx.write(in);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //flushes pending messages to the remote peer and closes the channel
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }

    /*
    Every Channel has an associated ChannelPipeline, which holds a chain of ChannelHandler instances.
    By default, a handler will forward the invocation of a handler method to the next one in the chain.
    Therefore, if exceptionCaught() is not implemented somewhere along the chain, exceptions received will travel
    to the end of the ChannelPipeline and will be logged. For this reason, your application should supply at least
    one ChannelHandler that implements exceptionCaught()
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
