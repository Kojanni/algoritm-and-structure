package org.micro.kojanni.happy_bilets;

import lombok.SneakyThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Function;

public class Test {

    private Function<String[], String> run;

    public Test(Function<String[], String> run) {
        this.run = run;
    }

    @SneakyThrows
    public void run() {
        int iter = 0;
        while (true) {
            String basePath = "src/main/resources/happy/";
            String fileIn = basePath + "test." + iter + ".in";
            String fileOut = basePath + "test." + iter + ".out";

            if (!Files.exists(Paths.get(fileIn)) || !Files.exists(Paths.get(fileOut))) {
                return;
            }

            try {
                // Чтение входных данных
                String[] input = Files.readAllLines(Paths.get(fileIn)).toArray(new String[0]);

                // Выполнение теста
                String result = run.apply(input);

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