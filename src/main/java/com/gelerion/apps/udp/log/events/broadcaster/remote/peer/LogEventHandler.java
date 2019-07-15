package com.gelerion.apps.udp.log.events.broadcaster.remote.peer;

import com.gelerion.apps.udp.log.events.broadcaster.model.LogEvent;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class LogEventHandler extends SimpleChannelInboundHandler<LogEvent> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LogEvent event) throws Exception {
        String msg = event.getReceivedTimestamp() +
                " [" +
                event.getSource().toString() +
                "] [" +
                event.getLogfile() +
                "] : " +
                event.getMsg();

        System.out.println(msg);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
