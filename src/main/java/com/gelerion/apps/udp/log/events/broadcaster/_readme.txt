Netty UDP classes used in broadcaster

interface AddressedEnvelope<M, A extends SocketAddress>
 Defines a message that wraps another message with sender and recipient addresses. M is the message type; A is the address type.

class DefaultAddressedEnvelope<M, A extends SocketAddress> implementsAddressedEnvelope<M, A>
 Provides a default implementation of interface AddressedEnvelope.

class DatagramPacket extends DefaultAddressedEnvelope<ByteBuf, InetSocketAddress> implements ByteBufHolder
 Extends DefaultAddressedEnvelope to use ByteBuf as the message data container.

interface DatagramChannel extends Channel
 Extends Netty’s Channel abstraction to support UDP multicast group management.

class NioDatagramChannel extends AbstractNioMessageChannel implements DatagramChannel
 Defines a Channel type that can send and receive AddressedEnvelope messages.

Pipeline
                         Channel Pipeline
 {Remote peer} < \    | ------------------------------------------------------
 {Remote peer} < ---- | [DatagramPacket] <-- [LogEventEncoder] <-- [LogEvent]
 {Remote peer} < /    | ----------------------------------------------- | ----
                                                                        ^
                                                                        |
                                                                   [Local File]