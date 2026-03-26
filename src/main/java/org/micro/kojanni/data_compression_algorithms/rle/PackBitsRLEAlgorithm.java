package org.micro.kojanni.data_compression_algorithms.rle;

import org.micro.kojanni.data_compression_algorithms.DataCompressionAlgorithm;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Улучшенный RLE с кодированием серий и литералов через знаковый байт длины.
 * Положительные значения (1..127) означают серию повторов, за которой следует один байт значения.
 * Отрицательные значения (-1..-128) означают отрицательные для серии одиночных с.
 */
public class PackBitsRLEAlgorithm implements DataCompressionAlgorithm {

    private static final int MAX_REPEAT_LENGTH = 127;
    private static final int MAX_LITERAL_LENGTH = 128;

    @Override
    public void compress(InputStream input, OutputStream output) throws IOException {
        int current = input.read();
        if (current == -1) {
            return;
        }

        ByteArrayOutputStream literalBuffer = new ByteArrayOutputStream();
        int runLength = 1;

        while (true) {
            int next = input.read();
            
            if (next == current && runLength < MAX_REPEAT_LENGTH) {
                runLength++;
                continue;
            }

            // Серия закончилась или достигнут лимит
            if (runLength >= 3) {
                flushLiterals(output, literalBuffer);
                writeRepeat(output, runLength, current);
            } else {
                for (int i = 0; i < runLength; i++) {
                    literalBuffer.write(current);
                    if (literalBuffer.size() == MAX_LITERAL_LENGTH) {
                        flushLiterals(output, literalBuffer);
                    }
                }
            }

            if (next == -1) {
                break;
            }
            
            current = next;
            runLength = 1;
        }

        flushLiterals(output, literalBuffer);
    }

    @Override
    public void decompress(InputStream input, OutputStream output) throws IOException {
        int controlByte;
        while ((controlByte = input.read()) != -1) {
            int control = toSignedByte(controlByte);
            if (control == 0) {
                throw new IOException("Неверная длина блока: 0");
            }

            if (control > 0) {
                int value = input.read();
                if (value == -1) {
                    throw new EOFException("Неожиданный конец потока при чтении значения повтора");
                }
                for (int i = 0; i < control; i++) {
                    output.write(value);
                }
            } else {
                int length = -control;
                byte[] buffer = input.readNBytes(length);
                if (buffer.length != length) {
                    throw new EOFException("Неожиданный конец потока при чтении литерального блока");
                }
                output.write(buffer);
            }
        }
    }

    private void flushLiterals(OutputStream output, ByteArrayOutputStream literalBuffer) throws IOException {
        if (literalBuffer.size() == 0) {
            return;
        }
        byte[] data = literalBuffer.toByteArray();
        int offset = 0;
        while (offset < data.length) {
            int chunk = Math.min(MAX_LITERAL_LENGTH, data.length - offset);
            output.write(encodeSignedByte(-chunk));
            output.write(data, offset, chunk);
            offset += chunk;
        }
        literalBuffer.reset();
    }

    private void writeRepeat(OutputStream output, int runLength, int value) throws IOException {
        while (runLength > 0) {
            int chunk = Math.min(runLength, MAX_REPEAT_LENGTH);
            output.write(chunk);
            output.write(value);
            runLength -= chunk;
        }
    }

    private int toSignedByte(int value) {
        return value <= 127 ? value : value - 256;
    }

    private int encodeSignedByte(int value) {
        return value & 0xFF;
    }
}
