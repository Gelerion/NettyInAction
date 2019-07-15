package com.gelerion.apps.udp.log.events.broadcaster.remote.peer;

import com.gelerion.apps.udp.log.events.broadcaster.model.LogEvent;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class LogEventDecoder extends MessageToMessageDecoder<DatagramPacket> {

    @Override
    protected void decode(ChannelHandlerContext ctx, DatagramPacket packet, List<Object> out) throws Exception {
        ByteBuf content = packet.content();
        int idx = content.indexOf(0, content.readableBytes(), LogEvent.SEPARATOR);

        String filename = content.slice(0, idx).toString(StandardCharsets.UTF_8);
        String logMsg = content.slice(idx + 1, (content.readableBytes() - (idx + 1))).toString(StandardCharsets.UTF_8);

        LogEvent event = new LogEvent(packet.sender(), System.currentTimeMillis(), filename, logMsg);
        out.add(event);
    }
}
