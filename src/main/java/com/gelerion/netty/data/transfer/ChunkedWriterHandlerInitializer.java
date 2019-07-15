package com.gelerion.netty.data.transfer;

import io.netty.channel.*;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedStream;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.io.File;
import java.io.FileInputStream;

public class ChunkedWriterHandlerInitializer extends ChannelInitializer<Channel> {
    private final File file;
    private final SslContext sslContext;

    public ChunkedWriterHandlerInitializer(File file, SslContext sslContext) {
        this.file = file;
        this.sslContext = sslContext;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new SslHandler(sslContext.newEngine(ch.alloc())));
        pipeline.addLast(new ChunkedWriteHandler());

        //starts to write the file data once the connection is established
        pipeline.addLast(new WriteStreamHandler());
    }

    public final class WriteStreamHandler extends ChannelInboundHandlerAdapter {

        //writes the file data using ChunkedInput when the connection is established
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            super.channelActive(ctx);
            ctx.writeAndFlush(new ChunkedStream(new FileInputStream(file)));
            //ChunkedWriteHandler:
            //queue.add(new PendingWrite(msg, promise));
            //doFlush { queue.poll(; ... }
        }
    }

}
