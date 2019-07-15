Writing Big Data

Writing big chunks of data efficiently is a special problem in asynchronous frameworks because of the possibility
of network saturation. Because the write operations are non-blocking, they return on completion and notify the
ChannelFuture even if all the data hasn’t been written out. When this occurs, if you don’t stop writing you risk running
out of memory. So when writing large masses of data, you need to be prepared to handle cases where a slow connection to a
remote peer can cause delays in freeing memory. Let’s consider the case of writing the contents of a file to the network.

ChunkedFile	Fetches data from a file chunk by chunk, for use when your platform doesn’t support zero-copy or you need to transform the data
ChunkedNioFile	Similar to ChunkedFile except that it uses FileChannel
ChunkedStream	Transfers content chunk by chunk from an InputStream
ChunkedNioStream	Transfers content chunk by chunk from a ReadableByteChanne