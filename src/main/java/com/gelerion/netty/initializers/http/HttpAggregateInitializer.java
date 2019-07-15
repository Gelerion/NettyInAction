package com.gelerion.netty.initializers.http;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

/*
After the initializer has installed the handlers in the ChannelPipeline you can operate on the different
HttpObject messages. But because HTTP requests and responses can be composed of many parts, you’ll need to
aggregate them to form complete messages. To eliminate this cumbersome task, Netty provides an aggregator that
merges message parts into FullHttpRequest and FullHttpResponse messages. This way you always see the full message contents.
 */
public class HttpAggregateInitializer extends ChannelInitializer<Channel> {
    private final boolean client;

    public HttpAggregateInitializer(boolean client) {
        this.client = client;
    }

    // There’s a slight cost to this operation because the message segments need to be buffered until complete messages
    // can be forwarded to the next ChannelInboundHandler. The trade-off is that you don’t need to worry about
    // message fragmentation

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        if (client) {
            pipeline.addLast("codec", new HttpClientCodec());
        }
        else {
            pipeline.addLast("codec", new HttpServerCodec());
        }

        int maxMessageSizeInBytes = 512 * 1024; //512Kb
        pipeline.addLast("aggregator", new HttpObjectAggregator(maxMessageSizeInBytes));

    }
}
