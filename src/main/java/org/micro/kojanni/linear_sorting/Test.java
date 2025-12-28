package org.micro.kojanni.linear_sorting;

import lombok.SneakyThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Function;

public class Test {

    private Function<String[], String> runFunction;

    public Test(Function<String[], String> run) {
        this.runFunction = run;
    }

    @SneakyThrows
    public void run(String basePath) {
        int iter = 0;
        while (true) {
            String fileIn = basePath + "test." + iter + ".in";
            String fileOut = basePath + "test." + iter + ".out";

            if (!Files.exists(Paths.get(fileIn)) || !Files.exists(Paths.get(fileOut))) {
                return;
            }

            try {
                String[] input = Files.readAllLines(Paths.get(fileIn)).toArray(new String[0]);

                long startTime = System.nanoTime();

                String result = runFunction.apply(input);

                long endTime = System.nanoTime();
                long duration = endTime - startTime;
                System.out.printf("Test %d: Время выполнения: %d нс%n", iter, duration);

                // Чтение ожидаемого результата
                String expected = Files.readString(Paths.get(fileOut)).trim();

                // Сравнение результатов
                if (result.equals(expected)) {
                    System.out.printf("Test %d: PASSED%n", iter);
//                    System.out.println("Expected: " + expected);
                } else {
                    System.out.printf("Test %d: FAILED%n", iter);
                    System.out.println("Expected: " + expected);
                    System.out.println("Actual: " + result);
                }
            } catch (IOException e) {
                System.err.println("Error processing test files: " + e.getMessage());
                return;
            }

            iter++;
        }
    }

    @SneakyThrows
    public void runBinary(String filePath, Function<int[], int[]> function) {
        byte[] bytes = Files.readAllBytes(Paths.get(filePath));
        int[] numbers = new int[bytes.length / 2];

        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = ((bytes[i * 2] & 0xFF) | ((bytes[i * 2 + 1] & 0xFF) << 8));
        }

        long start = System.nanoTime();
        int[] result = function.apply(numbers);
        long time = System.nanoTime() - start;

        System.out.println("Time: " + time + " ns (" + (time / 1_000_000.0) + " ms)");
        System.out.println("Numbers: " + numbers.length);
        boolean sorted = isSorted(result);
        System.out.println("Проверка сортировки: " + (sorted ? "✓ УСПЕХ" : "✗ ОШИБКА"));
    }

    private boolean isSorted(int[] array) {
        for (int i = 1; i < Math.min(array.length, 10000); i++) {
            if (array[i] < array[i - 1]) {
                System.out.println("Ошибка на индексах " + (i-1) + "-" + i +
                        ": " + array[i-1] + " > " + array[i]);
                return false;
            }
        }
        return true;
    }
}