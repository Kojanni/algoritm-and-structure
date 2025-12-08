package org.micro.kojanni.pyramid_sorting;

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
}