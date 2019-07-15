Netty’s core components
 * Channels - an open connection to an entity such as a hardware device, a file, a network socket, or a program
   component that is capable of performing one or more distinct I/O operations, for example reading or writing
 * Callbacks
 * Futures - Netty provides its own implementation, ChannelFuture, for use when an asynchronous operation is executed
 * Events and handlers

Netty’s networking abstraction:
 * Channel — sockets
 * EventLoop — control flow, multithreading, concurrency
 * ChannelFuture — asynchronous notification

 Channel
-------------------------------
Basic I/O operations (bind(), connect(), read(), and write()) depend on primitives supplied by the underlying
network transport. In Java-based networking, the fundamental construct is class Socket. Netty’s Channel interface
provides an API that greatly reduces the complexity of working directly with Sockets.

 EventLoop
-------------------------------
The EventLoop defines Netty’s core abstraction for handling events that occur during the lifetime of a connection
 1. Create Channel
 2. Register EventLoop with Channel
 3. Process I/O with EventLoop during Entire lifetime
Channel -> Use EventLoop out of EvenLoopGroup

Relationships are:
 1. An EventLoopGroup contains one or more EventLoops
 2. An EventLoopGroup is bound to a single Thread for its lifetime
 3. All I/O events processed by an EvenLoop are handled on its dedicated Thread
 4. A Channel is registered for its lifetime with a single EventLoop
 5. A single EventLoop may be assigned to one or more Channels.

 ChannelHandler
-------------------------------
From the application developer’s standpoint, the primary component of Netty is the ChannelHandler, which serves as
the container for all application logic that applies to handling inbound and outbound data. This is possible because
ChannelHandler methods are triggered by network events. In fact, a ChannelHandler can be dedicated to almost any kind
of action, such as converting data from one format to another or handling exceptions thrown during processing.

 Diagrams
-------------------------------

|Comparable| |AttributeMap|
      ^          ^
       \        /
       | Channel |

  --> (Tag interface) | ServerChannel   | extends Channel
  --> (class)         | AbstractChannel |

Used in:
 ChannelPipeline
 ChannelConfig

 Transports
-------------------------------

 Name    | Package                     | Description
-------------------------------------------------------------------------------------------------------------------------
NIO	     | io.netty.channel.socket.nio | Uses the java.nio.channels package as a foundation—a selector-based approach.
-------------------------------------------------------------------------------------------------------------------------
Epoll    | io.netty.channel.epoll	   | Uses JNI for epoll() and non-blocking IO. This transport supports features
         |                             | available only on Linux, such as SO_REUSEPORT, and is faster than the NIO transport as well as fully non-blocking.
-------------------------------------------------------------------------------------------------------------------------
OIO	     | io.netty.channel.socket.oio | Uses the java.net package as a foundation—uses blocking streams.
-------------------------------------------------------------------------------------------------------------------------
Local    | io.netty.channel.local	   | A local transport that can be used to communicate in the VM via pipes.
-------------------------------------------------------------------------------------------------------------------------
Embedded | io.netty.channel.embedded   | An embedded transport, which allows using ChannelHandlers without a true
         |                             | network-based transport. This can be quite useful for testing your ChannelHandler implementations.
-------------------------------------------------------------------------------------------------------------------------