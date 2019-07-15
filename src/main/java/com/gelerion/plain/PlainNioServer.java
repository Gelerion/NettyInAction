package com.gelerion.plain;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import static java.nio.charset.StandardCharsets.UTF_8;

public class PlainNioServer {

    //Reactor pattern
    //http://kasunpanorama.blogspot.com/2015/04/understanding-reactor-pattern-with-java.html
    public static void main(String[] args) {

    }

    void serve(int port) throws IOException {
        try (ServerSocketChannel serverChannel = ServerSocketChannel.open()) {
            serverChannel.configureBlocking(false);

            try (ServerSocket serverSocket = serverChannel.socket()) {
                InetSocketAddress address = new InetSocketAddress(port);
                //binds the server to the selcted port
                serverSocket.bind(address);

                //opens the Selector for handling channels
                Selector selector = Selector.open();

                //registers the ServerSocket with the Selector to accept connections
                serverChannel.register(selector, SelectionKey.OP_ACCEPT);
                ByteBuffer msg = ByteBuffer.wrap("Hi".getBytes(UTF_8));

                while (!Thread.currentThread().isInterrupted()) {
                    try { selector.select(); } catch (IOException e) { e.printStackTrace(); break; }

                    //obtains all SelectionKey instances that received events
                    Set<SelectionKey> readyKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = readyKeys.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        iterator.remove();

                        try {
                            //checks if the event is a new connection ready to be accepted
                            if (key.isAcceptable()) {
                                ServerSocketChannel server = (ServerSocketChannel) key.channel();
                                SocketChannel client = server.accept();
                                client.configureBlocking(false);
                                client.register(selector, SelectionKey.OP_WRITE | SelectionKey.OP_READ, msg.duplicate());
                                System.out.println("Accepted connection from " + client);
                            }

                            //checks if socket is ready for writing
                            if (key.isWritable()) {
                                SocketChannel client = (SocketChannel) key.channel();
                                ByteBuffer buffer = (ByteBuffer) key.attachment();
                                while (buffer.hasRemaining()) {
                                    //writes data to the connected client
                                    if (client.write(buffer) == 0) break;
                                }
                                client.close();
                            }

                        } catch (IOException ex) {
                            key.cancel();
                        }
                    }
                }

            }
        }
    }

}