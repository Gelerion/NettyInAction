package com.gelerion.netty.data.transfer;

import io.netty.channel.DefaultFileRegion;
import io.netty.channel.FileRegion;
import io.netty.channel.embedded.EmbeddedChannel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class TransferWithFileRegion {

    /*
    The zero-copy feature of NIO, which eliminates copying steps in moving the contents
    of a file from the file system to the network stack. All of this happens in Netty’s
    core, so all that’s required is that the application use an implementation of interface
    FileRegion, defined in the Netty API documentation as “a region of a file that is sent
    via a Channel that supports zero-copy file transfer.
     */
    public static void main(String[] args) throws FileNotFoundException {
        File file = new File(TransferWithFileRegion.class.getClassLoader().getResource("text.txt").getFile());
        FileInputStream in = new FileInputStream(file);

        FileRegion region = new DefaultFileRegion(in.getChannel(), 0, file.length());

        EmbeddedChannel channel = new EmbeddedChannel();
        channel.writeAndFlush(region).addListener(future -> {
            if (!future.isSuccess()) {
                Throwable cause = future.cause();
                cause.printStackTrace();
            }
        });

        /*
        This example applies only to the direct transmission of a file’s contents, excluding any processing
        of the data by the application. In cases where you need to copy the data from the file system into user memory,
        you can use ChunkedWriteHandler, which provides support for writing a large data stream asynchronously
        without incurring high memory consumption.
         */

    }

}
