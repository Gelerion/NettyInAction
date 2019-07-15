package com.gelerion.netty.initializers.http;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContentDecompressor;
import io.netty.handler.codec.http.HttpServerCodec;

/*
When using HTTP, it’s advisable to employ compression to reduce the size of transmitted data as much as possible.
Although compression does have some cost in CPU cycles, it’s generally a good idea, especially for text data.
 */
public class HttpCompression extends ChannelInitializer<Channel> {
    private final boolean client;

    public HttpCompression(boolean client) {
        this.client = client;
    }

    /*
    GET /encrypted-area HTTP/1.1
    Host: www.example.com
    Accept-Encoding: gzip, deflate
     */

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        if (client) {
            pipeline.addLast("codec", new HttpClientCodec());
            pipeline.addLast("decompressor", new HttpContentDecompressor());
        }
        else {
            pipeline.addLast("codec", new HttpServerCodec());
            pipeline.addLast("decompressor", new HttpContentDecompressor());
        }
    }
}
