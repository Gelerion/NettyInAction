package com.gelerion.apps.chat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private final ChannelGroup group;

    public TextWebSocketFrameHandler(ChannelGroup group) {
        this.group = group;
    }


    /*
    If the event indicates that handshake was successful, removes the HttpRequestHandler
    from the ChannelPipeline because no further HTTP messages will be received
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            ctx.pipeline().remove(HttpRequestHandler.class);
            //notifies all connected WS clients that new Client has connected
            group.writeAndFlush(new TextWebSocketFrame("Client " + ctx.channel() + " joined"));
            //adds the new WebSocketChannel to the ChannelGroup so it will receive all messages
            group.add(ctx.channel());
            return;
        }

        super.userEventTriggered(ctx, evt);
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        //increments the reference count of the message and writes it to all connected clients in the ChannelGroup
        group.writeAndFlush(msg.retain());
    }
}
