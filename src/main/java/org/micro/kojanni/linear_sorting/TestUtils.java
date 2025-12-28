package org.micro.kojanni.linear_sorting;

import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.Random;

@UtilityClass
public class TestUtils {

    /**
     * Тестируем несколько сортировок и выводим результаты в виде таблицы
     * @param sizes массив размеров тестовых массивов
     * @param sortings массив объектов наследников Sorting
     */
    public static void benchmark(int[] sizes, Sorting[] sortings) {

        // Заголовок таблицы
        System.out.printf("%12s", "Размер массива");
        for (Sorting s : sortings) {
            System.out.printf(" | %22s", s.getClass().getSimpleName());
        }
        System.out.println();
        System.out.println("-".repeat(16 + 25 * sortings.length));

        for (int size : sizes) {
            int[] baseArray = randomArray(size);

            System.out.printf("%12d", size);

            for (Sorting sorter : sortings) {
                int[] arrCopy = Arrays.copyOf(baseArray, baseArray.length);
                long time = test(() -> sorter.sort(arrCopy));
                System.out.printf(" | %22.3f", time / 1_000_000.0);
            }

            System.out.println();
        }
    }

    public static int[] randomArray(int n) {
        Random r = new Random();
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) arr[i] = r.nextInt(999);
        return arr;
    }

    public static long test(Runnable sort) {
        long t1 = System.nanoTime();
        sort.run();
        return System.nanoTime() - t1;
    }
}
