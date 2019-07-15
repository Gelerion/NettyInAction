package com.gelerion.netty.echo.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import static java.nio.charset.StandardCharsets.UTF_8;

@ChannelHandler.Sharable
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    //invoked when a connection has been established
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //when notified that the channel is active, sends a message
        ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!", UTF_8));
    }

    /*
    This method is called whenever data is received. Note that the message sent by the server may be received in chunks.
    That is, if the server sends 5 bytes, there’s no guarantee that all 5 bytes will be received at once. Even for
    such a small amount of data, the channelRead0() method could be called twice, first with a ByteBuf (Netty’s byte container)
    holding 3 bytes, and second with a ByteBuf holding 2 bytes. As a stream-oriented protocol, TCP guarantees that the bytes
    will be received in the order in which they were sent by the server.
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        //logs a dump of the received message
        System.out.println("Client received: " + msg.toString(UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
