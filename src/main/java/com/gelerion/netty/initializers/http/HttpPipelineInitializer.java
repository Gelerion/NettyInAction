package com.gelerion.netty.initializers.http;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

/*
HTTP is based on a request/response pattern: the client sends an HTTP request to the server, and the server sends
back an HTTP response. Netty provides a variety of encoders and decoders to simplify working with this protocol
 */
public class HttpPipelineInitializer extends ChannelInitializer<Channel> {
    private final boolean client;

    public HttpPipelineInitializer(boolean client) {
        this.client = client;
    }

    //HTTP request component parts
    /*
    --------------------------------------------------------------------------------
                              FullHttpRequest
    | HttpRequest | + | HttpContent | + | HttpContent | + ... + | LastHttpContent |
    --------------------------------------------------------------------------------
     */

    //HTTP response component parts
    /*
    --------------------------------------------------------------------------------
                              FullHttpResponse
    | HttpResponse | + | HttpContent | + | HttpContent | + ... + | LastHttpContent |
    --------------------------------------------------------------------------------
     */
    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        if (client) {
            pipeline.addLast("decoder", new HttpResponseDecoder()); // -> handle responses from the server
            pipeline.addLast("encoder", new HttpRequestEncoder());  // -> to send requests to the server
        } else {
            pipeline.addLast("decoder", new HttpRequestDecoder());
            pipeline.addLast("encoder", new HttpResponseEncoder());
        }

    }
}
