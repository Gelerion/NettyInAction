package com.gelerion.netty.initializers.connection.management;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

import static java.nio.charset.StandardCharsets.ISO_8859_1;

/*
Detecting idle connections and timeouts is essential to freeing resources in a timely manner. This is such a common
task that Netty provides several ChannelHandler implementations just for this purpose
 */
public class IdleStateHandlerInitializer extends ChannelInitializer<Channel> {

    //Fires an IdleStateEvent if the connection idles too long. You can then handle the IdleStateEvent
    //by overriding userEventTriggered() in your ChannelInboundHandler.
    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new IdleStateHandler(0, 0, 60, TimeUnit.SECONDS));

    }

    public static final class HeartbeatHandler extends ChannelDuplexHandler {
        private static final ByteBuf HEARTBEAT_SEQUENCE = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("HEARTBEAT", ISO_8859_1));

        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            if (evt instanceof IdleStateEvent) {
                ctx.writeAndFlush(HEARTBEAT_SEQUENCE.duplicate())
                        .addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            } else {
                //not and IdleStateEvent, so pass it to the next handler
                super.userEventTriggered(ctx, evt);
            }

//            if (evt instanceof IdleStateEvent) {
//              IdleStateEvent e = (IdleStateEvent) evt;
//              if (e.state() == IdleState.READER_IDLE) {
//                  ctx.close();
//              } else if (e.state() == IdleState.WRITER_IDLE) {
//                 ctx.writeAndFlush(new PingMessage());
//              }
//          }
        }
    }
}
