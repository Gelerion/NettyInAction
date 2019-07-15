Delimited protocols

Delimited message protocols use defined characters to mark the beginning or end of a message or message segment,
often called a frame. This is true of many protocols formally defined by RFC documents, such as SMTP, POP3, IMAP, and Telnet.
And, of course, private organizations often have their own proprietary formats. Whatever protocol you work with, the
decoders listed in table 11.5 will help you to define custom decoders that can extract frames delimited by any sequence of tokens.

DelimiterBasedFrameDecoder - A generic decoder that extracts frames using any user-provided delimiter
LineBasedFrameDecoder - A decoder that extracts frames delimited by the line-endings \n or \r\n. This decoder is faster than DelimiterBasedFrameDecoder.

Byte stream                     Frames
------------------      ----------- -----------
| ABC\r\nDEF\r\n | ---> | ABC\r\n | | DEF\r\n |
------------------      ----------- -----------

Length-based protocols
A length-based protocol defines a frame by encoding its length in a header segment of the frame,
rather than by marking its end with a special delimiter.

FixedLengthFrameDecoder - Extracts frames of a fixed size, specified when the constructor is called.
LengthFieldBasedFrameDecoder - Extracts frames based on a length value encoded in a field in the frame header;
                               the offset and length of the field are specified in the constructor.

Byte stream                     Frames
------------------      ----------- -----------
|  32 bytes      | ---> | 8 bytes | | 8 bytes |  ...
------------------      ----------- -----------

Length based, shows an example where the length field in the header is at offset 0 and has a length of 2 bytes:

Byte stream                          After decode (12 bytes)
---------------------------      ----------------------
| Length | Actual Content | ---> |   Actual Content   |
| 0x000C | "HELLO. WORLD" |      |   "HELLO. WORLD"   |
---------------------------      ----------------------
Length is encoded in the first 2 bytes of the frame
The last 12 bytes have the contents