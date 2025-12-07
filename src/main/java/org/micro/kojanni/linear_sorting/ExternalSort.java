package org.micro.kojanni.linear_sorting;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class ExternalSort {

    public static void generateFile(int N, int T, String filename) throws IOException {
        Random random = new Random();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (int i = 0; i < N; i++) {
                int num = random.nextInt(Math.max(1, T)) + 1;
                writer.write(Integer.toString(num));
                writer.newLine();
            }
        }
    }

    public static void externalSortES1(String inputFile, String outputFile, int T) throws IOException {
        String[] tempFiles = new String[T];
        for (int i = 0; i < T; i++) {
            tempFiles[i] = "temp_es1_" + i + ".txt";
            new File(tempFiles[i]).delete();
            new File(tempFiles[i]).createNewFile();
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                int num = Integer.parseInt(line);
                if (num >= 1 && num <= T) {
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFiles[num - 1], true))) {
                        writer.write(line);
                        writer.newLine();
                    }
                }
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
                for (int i = 0; i < T; i++) {
                    try (BufferedReader tempReader = new BufferedReader(new FileReader(tempFiles[i]))) {
                        String num;
                        while ((num = tempReader.readLine()) != null) {
                            writer.write(num);
                            writer.newLine();
                        }
                    }
                }
            }
        } finally {
            for (int i = 0; i < T; i++) {
                new File("temp_es1_" + i + ".txt").delete();
            }
        }
    }

    public static void externalSortES2(String inputFile, String outputFile) throws IOException {
        final String A = "temp_es2_a.txt";
        final String B = "temp_es2_b.txt";
        final String OUT_A = "temp_es2_out_a.txt";
        final String OUT_B = "temp_es2_out_b.txt";

        final int INITIAL_RUN = 100;

        int runs = createInitialRunsTwoFiles(inputFile, A, B, INITIAL_RUN);
        if (runs == 0) {
            new File(outputFile).createNewFile();
            return;
        }

        if (runs == 1) {
            if (new File(A).length() > 0) Files.move(new File(A).toPath(), new File(outputFile).toPath(), StandardCopyOption.REPLACE_EXISTING);
            else Files.move(new File(B).toPath(), new File(outputFile).toPath(), StandardCopyOption.REPLACE_EXISTING);
            new File(A).delete();
            new File(B).delete();
            return;
        }

        int runSize = INITIAL_RUN;
        String currA = A, currB = B;
        String nextA = OUT_A, nextB = OUT_B;

        while (true) {
            int producedRuns = mergePassTwoFiles(currA, currB, nextA, nextB, runSize);

            if (producedRuns <= 1) {
                if (new File(nextA).length() > 0) Files.move(new File(nextA).toPath(), new File(outputFile).toPath(), StandardCopyOption.REPLACE_EXISTING);
                else Files.move(new File(nextB).toPath(), new File(outputFile).toPath(), StandardCopyOption.REPLACE_EXISTING);

                new File(currA).delete();
                new File(currB).delete();
                new File(nextA).delete();
                new File(nextB).delete();
                break;
            }

            new File(currA).delete();
            new File(currB).delete();

            Files.move(new File(nextA).toPath(), new File(currA).toPath(), StandardCopyOption.REPLACE_EXISTING);
            Files.move(new File(nextB).toPath(), new File(currB).toPath(), StandardCopyOption.REPLACE_EXISTING);

            new File(nextA).delete();
            new File(nextB).delete();

            runSize *= 2;
        }
    }

    /**
     * Создаёт начальные ранги, по runSize элементов, сортирует их и чередует запись в fileA и fileB.
     * Возвращает количество созданных ран (series).
     */
    private static int createInitialRunsTwoFiles(String inputFile, String fileA, String fileB, int runSize) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter wa = new BufferedWriter(new FileWriter(fileA));
             BufferedWriter wb = new BufferedWriter(new FileWriter(fileB))) {

            boolean writeToA = true;
            int[] buffer = new int[runSize];
            int read = 0;
            String line;
            int runs = 0;

            while ((line = reader.readLine()) != null) {
                buffer[read++] = Integer.parseInt(line);
                if (read == runSize) {
                    Arrays.sort(buffer, 0, read);
                    BufferedWriter cur = writeToA ? wa : wb;
                    for (int i = 0; i < read; i++) {
                        cur.write(Integer.toString(buffer[i]));
                        cur.newLine();
                    }
                    read = 0;
                    writeToA = !writeToA;
                    runs++;
                }
            }

            if (read > 0) {
                Arrays.sort(buffer, 0, read);
                BufferedWriter cur = writeToA ? wa : wb;
                for (int i = 0; i < read; i++) {
                    cur.write(Integer.toString(buffer[i]));
                    cur.newLine();
                }
                runs++;
            }

            return runs;
        }
    }

    /**
     * Одна фаза слияния: читаем по runSize из currA и currB, сливаем соответствующие пары и пишем по очереди в outA/outB.
     * Возвращает количество созданных ран в out (series).
     */
    private static int mergePassTwoFiles(String currA, String currB, String outA, String outB, int runSize) throws IOException {
        try (BufferedReader ra = new BufferedReader(new FileReader(currA));
             BufferedReader rb = new BufferedReader(new FileReader(currB));
             BufferedWriter wa = new BufferedWriter(new FileWriter(outA));
             BufferedWriter wb = new BufferedWriter(new FileWriter(outB))) {

            boolean writeToOutA = true;
            int producedRuns = 0;

            while (true) {
                int[] blockA = readBlock(ra, runSize);
                int lenA = blockA == null ? 0 : blockA.length;
                int[] blockB = readBlock(rb, runSize);
                int lenB = blockB == null ? 0 : blockB.length;

                if (lenA == 0 && lenB == 0) break;
                BufferedWriter curOut = writeToOutA ? wa : wb;

                if (lenA == 0) {
                    writeArrayToWriter(blockB, lenB, curOut);
                    producedRuns++;
                    writeToOutA = !writeToOutA;

                    while (true) {
                        blockB = readBlock(rb, runSize);
                        if (blockB == null || blockB.length == 0) break;
                        curOut = writeToOutA ? wa : wb;
                        writeArrayToWriter(blockB, blockB.length, curOut);
                        producedRuns++;
                        writeToOutA = !writeToOutA;
                    }
                    break;
                } else if (lenB == 0) {
                    writeArrayToWriter(blockA, lenA, curOut);
                    producedRuns++;
                    writeToOutA = !writeToOutA;

                    while (true) {
                        blockA = readBlock(ra, runSize);
                        if (blockA == null || blockA.length == 0) break;
                        curOut = writeToOutA ? wa : wb;
                        writeArrayToWriter(blockA, blockA.length, curOut);
                        producedRuns++;
                        writeToOutA = !writeToOutA;
                    }
                    break;
                } else {
                    mergeTwoIntArraysToWriter(blockA, lenA, blockB, lenB, curOut);
                    producedRuns++;
                    writeToOutA = !writeToOutA;
                }
            }

            return producedRuns;
        }
    }

    private static int[] readBlock(BufferedReader reader, int runSize) throws IOException {
        int[] buffer = new int[runSize];
        int cnt = 0;
        String line;
        while (cnt < runSize && (line = reader.readLine()) != null) {
            buffer[cnt++] = Integer.parseInt(line);
        }
        if (cnt == 0) return null;
        if (cnt == runSize) return buffer;
        return Arrays.copyOf(buffer, cnt);
    }

    private static void writeArrayToWriter(int[] arr, int size, BufferedWriter writer) throws IOException {
        for (int i = 0; i < size; i++) {
            writer.write(Integer.toString(arr[i]));
            writer.newLine();
        }
    }

    private static void mergeTwoIntArraysToWriter(int[] a, int lenA, int[] b, int lenB, BufferedWriter writer) throws IOException {
        int i = 0, j = 0;
        while (i < lenA && j < lenB) {
            if (a[i] <= b[j]) {
                writer.write(Integer.toString(a[i++]));
            } else {
                writer.write(Integer.toString(b[j++]));
            }
            writer.newLine();
        }
        while (i < lenA) { writer.write(Integer.toString(a[i++])); writer.newLine(); }
        while (j < lenB) { writer.write(Integer.toString(b[j++])); writer.newLine(); }
    }

    public static void externalSortES3(String inputFile, String outputFile) throws IOException {
        final String tempRunsFileA = "temp_es3_a.txt";
        final String tempRunsFileB = "temp_es3_b.txt";
        final int BLOCK = 100;

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter wa = new BufferedWriter(new FileWriter(tempRunsFileA));
             BufferedWriter wb = new BufferedWriter(new FileWriter(tempRunsFileB))) {

            boolean writeToA = true;
            int[] buffer = new int[BLOCK];
            int read = 0;
            String line;
            int runs = 0;

            while ((line = reader.readLine()) != null) {
                buffer[read++] = Integer.parseInt(line);
                if (read == BLOCK) {
                    Arrays.sort(buffer, 0, read);
                    BufferedWriter cur = writeToA ? wa : wb;
                    for (int i = 0; i < read; i++) { cur.write(Integer.toString(buffer[i])); cur.newLine(); }
                    read = 0;
                    writeToA = !writeToA;
                    runs++;
                }
            }
            if (read > 0) {
                Arrays.sort(buffer, 0, read);
                BufferedWriter cur = writeToA ? wa : wb;
                for (int i = 0; i < read; i++) { cur.write(Integer.toString(buffer[i])); cur.newLine(); }
                runs++;
            }

            if (runs == 0) {
                new File(outputFile).createNewFile();
                return;
            }
        }

        String currA = tempRunsFileA, currB = tempRunsFileB;
        String nextA = "temp_es3_out_a.txt", nextB = "temp_es3_out_b.txt";
        int runSize = BLOCK;

        while (true) {
            int produced = mergePassTwoFiles(currA, currB, nextA, nextB, runSize);
            if (produced <= 1) {
                if (new File(nextA).length() > 0) Files.move(new File(nextA).toPath(), new File(outputFile).toPath(), StandardCopyOption.REPLACE_EXISTING);
                else Files.move(new File(nextB).toPath(), new File(outputFile).toPath(), StandardCopyOption.REPLACE_EXISTING);

                new File(currA).delete();
                new File(currB).delete();
                new File(nextA).delete();
                new File(nextB).delete();
                break;
            }

            new File(currA).delete();
            new File(currB).delete();
            Files.move(new File(nextA).toPath(), new File(currA).toPath(), StandardCopyOption.REPLACE_EXISTING);
            Files.move(new File(nextB).toPath(), new File(currB).toPath(), StandardCopyOption.REPLACE_EXISTING);
            new File(nextA).delete();
            new File(nextB).delete();

            runSize *= 2;
        }
    }

    private static boolean isSorted(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            Integer prev = null;

            while ((line = reader.readLine()) != null) {
                int current = Integer.parseInt(line);
                if (prev != null && current < prev) {
                    return false;
                }
                prev = current;
            }
        }
        return true;
    }

    private static void copyFile(String source, String dest) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(source));
             BufferedWriter writer = new BufferedWriter(new FileWriter(dest))) {
            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.newLine();
            }
        }
    }

    public static void main(String[] args) {
        try {
            runTests();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void runTests() throws IOException {
        int[] N_values = {100, 1000, 10000, 100000, 1000000};
        String[] T_values = {"10", "N"};

        System.out.println("Сравнительная таблица времени сортировки (в миллисекундах)");
        System.out.println("-----------------------------------------------------------");
        System.out.printf("%-10s %-10s %-15s %-15s %-15s%n",
                "N", "T", "ES1", "ES2", "ES3");
        System.out.println("-----------------------------------------------------------");

        for (int N : N_values) {
            for (String T_str : T_values) {
                int T = T_str.equals("N") ? N : 10;

                String inputFile = "test_" + N + "_" + T + ".txt";
                generateFile(N, T, inputFile);

                String outputES1 = "output_es1_" + N + "_" + T + ".txt";
                long startTime = System.nanoTime();
                externalSortES1(inputFile, outputES1, T);
                long es1Time = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime);

                String outputES2 = "output_es2_" + N + "_" + T + ".txt";
                startTime = System.nanoTime();
                externalSortES2(inputFile, outputES2);
                long es2Time = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime);

                String outputES3 = "output_es3_" + N + "_" + T + ".txt";
                startTime = System.nanoTime();
                externalSortES3(inputFile, outputES3);
                long es3Time = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime);

                System.out.printf("%-10d %-10s %-15d %-15d %-15d%n",
                        N, T_str, es1Time, es2Time, es3Time);

                if (!isSorted(outputES1)) {
                    System.err.println("Ошибка: ES1 не отсортировал правильно для N=" + N + ", T=" + T_str);
                }
                if (!isSorted(outputES2)) {
                    System.err.println("Ошибка: ES2 не отсортировал правильно для N=" + N + ", T=" + T_str);
                }
                if (!isSorted(outputES3)) {
                    System.err.println("Ошибка: ES3 не отсортировал правильно для N=" + N + ", T=" + T_str);
                }

                new File(inputFile).delete();
                new File(outputES1).delete();
                new File(outputES2).delete();
                new File(outputES3).delete();
            }
        }
    }
}
