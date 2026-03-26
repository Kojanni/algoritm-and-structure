package org.micro.kojanni.data_compression_algorithms;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface DataCompressionAlgorithm {

    void compress(InputStream input, OutputStream output) throws IOException;

    void decompress(InputStream input, OutputStream output) throws IOException;
}
