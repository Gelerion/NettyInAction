Netty’s OpenSSL/SSLEngine implementation
Netty also provides an SSLEngine implementation that uses the OpenSSL toolkit (www.openssl.org). This class,
OpenSslEngine, offers better performance than the SSLEngine implementation supplied by the JDK.

Netty applications (clients and servers) can be configured to use OpenSslEngine by default if the OpenSSL libraries
are available. If not, Netty will fall back to the JDK implementation. For detailed instructions on configuring
OpenSSL support, please see the Netty documentation at

   http://netty.io/wiki/forked-tomcat-native.html#wiki-h2-1.

Note that the SSL API and data flow are identical whether you use the JDK’s SSLEngine or Netty’s OpenSslEngine.