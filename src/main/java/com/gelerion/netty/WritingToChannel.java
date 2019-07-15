package com.gelerion.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

import static java.nio.charset.StandardCharsets.UTF_8;

@SuppressWarnings("all")
public class WritingToChannel {

    public static void main(String[] args) {
        //Consider the common task of writing data and flushing it to the remote peer.
        //Netty’s Channel implementations are thread-safe, so you can store a reference to a Channel
        //and use it whenever you need to write something to the remote peer, even when many threads are in use.
        Channel channel = null;
        ByteBuf msg = Unpooled.copiedBuffer("data".getBytes(UTF_8));
        ChannelFuture future = channel.writeAndFlush(msg);

        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    System.out.println("Write successful");
                } else {
                    System.err.println("Write error");
                    future.cause().printStackTrace();
                }
            }
        });

    }

}
