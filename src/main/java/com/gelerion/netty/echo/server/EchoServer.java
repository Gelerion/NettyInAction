package com.gelerion.netty.echo.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

public class EchoServer {
    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        new EchoServer(8080).start();
    }

    public void start() throws Exception {
        EchoServerHandler echoServerHandler = new EchoServerHandler();
        //we may use OioServerSocketChannel and OioEventLoopGroup for old blocking io
        //even loop is responsible for handling event processing, such as accepting new connections and reading/writing data
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        try {
            //bootstrap and bind the server
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(eventLoopGroup)
                    //specify the use of an NIO transport Channel
                    .channel(NioServerSocketChannel.class)
                    //sets the socket address using the specified port
                    .localAddress(new InetSocketAddress(port))
                    //adds an echo handler to the channel's pipeline
                    //when a new connection is accepted, a new child Channel will be created, and the ChannelInitializer
                    //will add an instance of your EchoServerHandler to the Channel’s ChannelPipeline.
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            //handler is @Sharable so we can always use the same one
                            ch.pipeline().addLast(echoServerHandler);
                        }
                    });

            //binds the server asynchronously; sync() waits for the bind to complete
            ChannelFuture future = bootstrap.bind().sync();
            future.channel().closeFuture().sync();
        }
        finally {
            eventLoopGroup.shutdownGracefully().sync();
        }

    }
}
