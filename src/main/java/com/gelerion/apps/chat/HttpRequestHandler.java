package com.gelerion.apps.chat;


import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedNioFile;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * This component will serve the page that provides access to the chat room and display messages sent by connected clients
 */
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private final String wsUri;
    private static final File INDEX;

    static {
//        URL location = HttpRequestHandler.class.getProtectionDomain().getCodeSource().getLocation();
        try {
            URI location = HttpRequestHandler.class.getClassLoader().getResource("index.html").toURI();
//            String path = location.toURI() + "index.html";
            String path = location.getPath();
            path = !path.contains("file:") ? path : path.substring(5);
            INDEX = new File(path);
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Unable to locate index.html", e);
        }
    }

    public HttpRequestHandler(String wsUri) {
        this.wsUri = wsUri;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        //if a WebSocket upgrade is requested increments the reference count (retain)
        //and passes it to the next ChannelInboundHandler
        if (wsUri.equalsIgnoreCase(request.uri())) {
            //the call to retain() is needed because after channelRead() completes, it will call release() on the FullHttpRequest to release its resources.
            ctx.fireChannelRead(request.retain());
            return;
        }

        if (HttpUtil.is100ContinueExpected(request)) {
            send100Continue(ctx);
        }

        RandomAccessFile file = new RandomAccessFile(INDEX, "r");

        DefaultHttpResponse response = new DefaultHttpResponse(request.protocolVersion(), HttpResponseStatus.OK);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");

        //if keepalive is requested adds the required headers
        if (HttpUtil.isKeepAlive(request)) {
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, file.length());
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        }

        //writes the HttpResponse to the client
        ctx.write(response);

        //writes index.html to the client
        //if neither encryption nor compression is required, the greatest efficiency can be achieved by storing the
        //contents of index.html in a DefaultFileRegion. This will utilize zero-copy to perform the transmission.
        //For this reason you check to see if there is an SslHandler in the ChannelPipeline.
        //Alternatively, you use ChunkedNioFile.
        if (ctx.pipeline().get(SslHandler.class) == null) {
            ctx.write(new DefaultFileRegion(file.getChannel(), 0, file.length()));
        } else {
            ctx.write(new ChunkedNioFile(file.getChannel()));
        }


        //write and flush last http content
        ChannelFuture future = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);

        if (!HttpUtil.isKeepAlive(request)) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    private static void send100Continue(ChannelHandlerContext ctx) {
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE);
        ctx.writeAndFlush(response);
    }
}
