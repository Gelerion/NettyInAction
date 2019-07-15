package com.gelerion.netty.buffers;

import io.netty.buffer.*;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

@SuppressWarnings("all")
public class NettyBuffers {
    /*
    These are some of the advantages of the ByteBuf API:
     - It’s extensible to user-defined buffer types.
     - Transparent zero-copy is achieved by a built-in composite buffer type.
     - Capacity is expanded on demand (as with the JDK StringBuilder).
     - Switching between reader and writer modes doesn’t require calling ByteBuffer’s flip() method.
     - Reading and writing employ distinct indices.
     - Method chaining is supported.
     - Reference counting is supported.
     - Pooling is supported.
     */

    /*
    How it works?
    ByteBuf maintains two distinct indices: one for reading and one for writing. When you read from a ByteBuf,
    its readerIndex is incremented by the number of bytes read. Similarly, when you write to a ByteBuf, its writerIndex is incremented
     */

    public static void main(String[] args) {
        // Heap Buffer ====================
        ByteBuf heapBuf = UnpooledByteBufAllocator.DEFAULT.heapBuffer();
        if (heapBuf.hasArray()) { //checks whether ByteBuf has a backing array
            byte[] array = heapBuf.array(); //if so gets a reference to the array
            int offset = heapBuf.arrayOffset() + heapBuf.readerIndex(); //calculates the offset of the first byte
            int length = heapBuf.readableBytes(); //gets the number of readable bytes
            //handleArray(array, offset, length)
        }

        // Direct buffer ====================
        /*
        Direct buffer is another ByteBuf pattern. We expect that memory allocated for object creation will always come
        from the heap, but it doesn’t have to—the ByteBuffer class that was introduced in JDK 1.4 with NIO allows a JVM
        implementation to allocate memory via native calls. This aims to avoid copying the buffer’s contents to
        (or from) an intermediate buffer before (or after) each invocation of a native I/O operation.

        The Javadoc for ByteBuffer states explicitly, “The contents of direct buffers will reside outside of the
        normal garbage-collected heap.” This explains why direct buffers are ideal for network data transfer. If
        your data were contained in a heap-allocated buffer, the JVM would, in fact, copy your buffer to a direct
        buffer internally before sending it through the socket.
         */

        //The primary disadvantage of direct buffers is that they’re somewhat more expensive to allocate and release
        //than are heap-based buffers. You may also encounter another drawback if you’re working with legacy code:
        //because the data isn’t on the heap, you may have to make a copy, as shown next.
        ByteBuf directBuf = UnpooledByteBufAllocator.DEFAULT.directBuffer();
        if (!directBuf.hasArray()) { //checks if ByteBuf isn't backed by an array. If not this is a direct buffer.
            int length = directBuf.readableBytes();
            byte[] array = new byte[length];
            directBuf.getBytes(directBuf.readerIndex(), array); //copies bytes into the array
            //handleArray(array, 0, length)
        }

        // Composite buffers ====================
        //provides a virtual representation of multiple buffers as a single, merged buffer
        /*
        To illustrate, let’s consider a message composed of two parts, header and body, to be transmitted via HTTP.
        The two parts are produced by different application modules and assembled when the message is sent out.
        The application has the option of reusing the same message body for multiple messages. When this happens,
        a new header is created for each message.
         */
        CompositeByteBuf compositeBuf = Unpooled.compositeBuffer();
        ByteBuf headerBuf = Unpooled.directBuffer(); //can be backing or direct
        ByteBuf bodyBuf = Unpooled.directBuffer(); //can be backing or direct
        compositeBuf.addComponents(headerBuf, bodyBuf);

        compositeBuf.removeComponent(0); //remove the header
        for (ByteBuf buf : compositeBuf) {
            System.out.println(buf.toString()); //loops over all the ByteBuf instances
        }

        // ByteBufAllocator ====================
        //You can obtain a reference to a ByteBufAllocator either from a Channel (each of which can have a distinct
        //instance) or through the ChannelHandlerContext that is bound to a ChannelHandler.
        Channel channel = null;
        ByteBufAllocator allocator = channel.alloc();

        ChannelHandlerContext ctx = null;
        ByteBufAllocator allocator1 = ctx.alloc();

        //!
        /*
        Netty provides two implementations of ByteBufAllocator: PooledByteBufAllocator and UnpooledByteBufAllocator.
        The former pools ByteBuf instances to improve performance and minimize memory fragmentation. This implementation
        uses an efficient approach to memory allocation known as jemalloc that has been adopted by a number of modern OSes.
        The latter implementation doesn’t pool ByteBuf instances and returns a new instance every time it’s called.

        http://people.freebsd.org/~jasone/jemalloc/bsdcan2006/jemalloc.pdf
        https://medium.com/iskakaushik/eli5-jemalloc-e9bd412abd70
         */
    }
}


































