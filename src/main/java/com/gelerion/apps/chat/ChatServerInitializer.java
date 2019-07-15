package com.gelerion.apps.chat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class ChatServerInitializer extends ChannelInitializer<Channel> {
    private final ChannelGroup group;

    public ChatServerInitializer(ChannelGroup group) {
        this.group = group;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        /*
          when the upgrade is completed, the WebSocketServerProtocolHandler replaces the HttpRequestDecoder
          with a WebSocketFrameDecoder and the HttpResponseEncoder with a WebSocketFrameEncoder.
          to maximize performance it will then remove any ChannelHandlers that aren’t required for WebSocket connections.
          these would include the HttpObjectAggregator and HttpRequestHandler
         */

        ChannelPipeline pipeline = ch.pipeline();
        //Decodes bytes to HttpRequest, HttpContent, and LastHttpContent.
        //Encodes HttpRequest, HttpContent, and LastHttpContent to bytes.
        pipeline.addLast(new HttpServerCodec());
        //Writes the contents of a file.
        pipeline.addLast(new ChunkedWriteHandler());
        //Aggregates an HttpMessage and its following HttpContents into a single FullHttpRequest or FullHttpResponse
        //(depending on whether it’s being used to handle requests or responses).
        //With this installed, the next ChannelHandler in the pipeline will receive only full HTTP requests.
        pipeline.addLast(new HttpObjectAggregator(64 * 1024));
        //HttpRequestHandler	Handles FullHttpRequests (those not sent to a /ws URI).
        pipeline.addLast(new HttpRequestHandler("/ws"));
        //As required by the WebSocket specification, handles the WebSocket upgrade handshake,
        //PingWebSocket-Frames, PongWebSocketFrames, and CloseWebSocketFrames.
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
        //Handles TextWebSocketFrames and handshake completion events
        pipeline.addLast(new TextWebSocketFrameHandler(group));
    }
}
