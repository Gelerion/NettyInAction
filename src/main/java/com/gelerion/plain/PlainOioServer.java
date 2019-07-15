package com.gelerion.plain;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.nio.charset.StandardCharsets.UTF_8;

public class PlainOioServer {

    public static void main(String[] args) throws IOException {
        final PlainOioServer server = new PlainOioServer();
        server.serve(8080);
    }

    void serve(int port) throws IOException {
        final ExecutorService pool = Executors.newFixedThreadPool(2);

        final ServerSocket socket = new ServerSocket(port);
        while (!Thread.currentThread().isInterrupted()) {
            System.out.println("Listening on " + port);
            try (Socket clientSocket = socket.accept()) {
                System.out.println("Accepted connection from " + clientSocket);
                pool.submit(() -> {
                    try (OutputStream out = clientSocket.getOutputStream()) {
                        out.write("Hi".getBytes(UTF_8));
                        out.flush();
                        clientSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }

}
