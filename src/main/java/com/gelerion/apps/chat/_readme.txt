Our example application will demonstrate real-time functionality by using the WebSocket protocol to implement
a browser-based chat application such as you may have encountered in the text-messaging feature of Facebook.
We’ll take it further by enabling multiple users to communicate with each other simultaneously.

1. A Client sends a message
2. The message is broadcast to all other connected clients

                  WebSockets
| Client | ----- | Hi There | ----> |
                 |          |       |
| Client | <---- | Hi There | ----- | Server
                 |          |       |
| Client | <---- | Hi There | ----- |

1. Client connects to the server and is part of the client
2. Chat message are exchanged via WebSockets
3. Messages are sent bidirectionally
4. The server handles all the clients

 == Adding WebSocket support
A mechanism known as the upgrade handshake is used to switch from standard HTTP or HTTPS protocol to WebSocket.
Thus, an application that uses WebSocket will always start with HTTP/S and then perform the upgrade.
When precisely this happens is specific to the application; it may be at startup or when a specific URL has been requested.

Our application adopts the following convention: If the URL requested ends with /ws we’ll upgrade the protocol to WebSocket.
Otherwise the server will use basic HTTP/S. After the connection has been upgraded, all data will be transmitted using WebSocket.