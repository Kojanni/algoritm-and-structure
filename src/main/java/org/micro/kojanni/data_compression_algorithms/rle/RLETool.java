package org.micro.kojanni.data_compression_algorithms.rle;

import org.micro.kojanni.data_compression_algorithms.DataCompressionAlgorithm;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class RLETool {

    private static final DataCompressionAlgorithm BASIC = new RLEAlgorithm();
    private static final DataCompressionAlgorithm PACK_BITS = new PackBitsRLEAlgorithm();

    private RLETool() {
    }

    public static void main(String[] args) {
        if (args.length < 3) {
            printUsage();
            return;
        }

        String mode = args[0];
        String inputPath = args[1];
        String outputPath = args[2];

        DataCompressionAlgorithm algorithm = chooseAlgorithm(args);
        if (algorithm == null) {
            return;
        }

        try (InputStream input = new BufferedInputStream(new FileInputStream(inputPath));
             OutputStream output = new BufferedOutputStream(new FileOutputStream(outputPath))) {

            switch (mode.toLowerCase()) {
                case "compress":
                case "c":
                    algorithm.compress(input, output);
                    break;
                case "decompress":
                case "d":
                    algorithm.decompress(input, output);
                    break;
                default:
                    System.err.println("Неизвестный режим: " + mode);
                    printUsage();
            }
        } catch (IOException e) {
            System.err.println("Ошибка при обработке файла: " + e.getMessage());
        }
    }

    private static DataCompressionAlgorithm chooseAlgorithm(String[] args) {
        DataCompressionAlgorithm algorithm = BASIC;
        for (int i = 3; i < args.length; i++) {
            String option = args[i].toLowerCase();
            switch (option) {
                case "--improved":
                case "-i":
                    algorithm = PACK_BITS;
                    break;
                case "--basic":
                case "-b":
                    algorithm = BASIC;
                    break;
                default:
                    System.err.println("Неизвестная опция: " + args[i]);
                    printUsage();
                    return null;
            }
        }
        return algorithm;
    }

    private static void printUsage() {
        System.out.println("Использование: java ... RLETool <compress|decompress> <входной_файл> <выходной_файл> [--improved|--basic]");
        System.out.println("Сокращённые команды: c или d; опции: -i (улучшенный), -b (базовый)");
        System.out.println("Примеры:");
        System.out.println("  java ... RLETool compress input.txt input.rle --basic");
        System.out.println("  java ... RLETool compress input.txt input.rle --improved");
        System.out.println("  java ... RLETool decompress input.rle output.txt -i");
    }
}
