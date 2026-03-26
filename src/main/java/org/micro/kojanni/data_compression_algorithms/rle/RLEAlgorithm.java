package org.micro.kojanni.data_compression_algorithms.rle;

import org.micro.kojanni.data_compression_algorithms.DataCompressionAlgorithm;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Run-Length Encoding — кодирование длин серий.
 */
public class RLEAlgorithm implements DataCompressionAlgorithm {

    private static final int MAX_RUN_LENGTH = 0xFFFF;

    @Override
    public void compress(InputStream input, OutputStream output) throws IOException {
        int current = input.read();
        if (current == -1) {
            return;
        }

        int runLength = 1;
        int next;
        while ((next = input.read()) != -1) {
            if (next == current && runLength < MAX_RUN_LENGTH) {
                runLength++;
            } else {
                writeRun(output, runLength, current);
                current = next;
                runLength = 1;
            }
        }

        writeRun(output, runLength, current);
    }

    @Override
    public void decompress(InputStream input, OutputStream output) throws IOException {
        byte[] lengthBuffer = new byte[2];
        while (true) {
            int read = input.read(lengthBuffer);
            if (read == -1) {
                return;
            }
            if (read != 2) {
                throw new EOFException("Неожиданный конец потока при чтении длины повтора");
            }

            int runLength = ((lengthBuffer[0] & 0xFF) << 8) | (lengthBuffer[1] & 0xFF);
            int value = input.read();
            if (value == -1) {
                throw new EOFException("Неожиданный конец потока при чтении значения повторяющегося байта");
            }

            for (int i = 0; i < runLength; i++) {
                output.write(value);
            }
        }
    }

    private void writeRun(OutputStream output, int runLength, int value) throws IOException {
        output.write((runLength >>> 8) & 0xFF);
        output.write(runLength & 0xFF);
        output.write(value);
    }
}
