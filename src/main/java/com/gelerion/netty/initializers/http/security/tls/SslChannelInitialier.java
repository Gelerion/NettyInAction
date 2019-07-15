package com.gelerion.netty.initializers.http.security.tls;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;

public class SslChannelInitialier extends ChannelInitializer<Channel> {
    private final SslContext context;
    private final boolean startTls;

    public SslChannelInitialier(SslContext context, boolean startTls) {
        this.context = context;
        this.startTls = startTls; //if true the first message written is not encrypted
    }

    /*
    1. Encrypted data is intercepted by the SslHandler
    2. The SslHandler decrypts the data and directs it inbound
    3. Outbound data is passed through the SslHandler
    4. The SslHandler encrypts the data and passes it outbound


     Encrypted        |-----------------|  Decrypted
    ------------>     |   SslHandler    | ------>
          Encrypted   |                 |    Plain
    OUT <------------ |                 | <----------
                      |-----------------|

     */

    @Override
    protected void initChannel(Channel ch) throws Exception {
        //for each SslHandler instance, obtains a new SSLEngine from the SslContext using the ByteBufAllocator of the Channel
        SSLEngine engine = context.newEngine(ch.alloc());
        ch.pipeline().addFirst("ssl", new SslHandler(engine, startTls));
    }
}
