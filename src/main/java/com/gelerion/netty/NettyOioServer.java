package com.gelerion.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.oio.OioServerSocketChannel;

import java.net.InetSocketAddress;

import static java.nio.charset.StandardCharsets.UTF_8;

@SuppressWarnings("deprecation")
public class NettyOioServer {

    void serve(int port) throws InterruptedException {
        ByteBuf msg = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("Hi\r\n".getBytes(UTF_8)));

        OioEventLoopGroup eventGroup = new OioEventLoopGroup(); // || NioEventLoopGroup
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(eventGroup)
                    //uses OioEventGroup to allow blocking mode
                    .channel(OioServerSocketChannel.class) // || NioSocketChannel.class
                    .localAddress(new InetSocketAddress(port))
                    //specifies ChannelInitializer that will be called for each accepted connection
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                                @Override
                                public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                    //writes message to client
                                    ctx.writeAndFlush(msg.duplicate()).addListener(ChannelFutureListener.CLOSE);
                                }
                            });
                        }
                    });

            ChannelFuture future = bootstrap.bind().sync();
            future.channel().closeFuture().sync();
        } finally {
            eventGroup.shutdownGracefully().sync();
        }
    }

}
