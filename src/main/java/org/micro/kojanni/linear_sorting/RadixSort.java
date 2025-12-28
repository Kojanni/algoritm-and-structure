package org.micro.kojanni.linear_sorting;

import org.micro.kojanni.visual.SortingAlgorithm;

/**
 * Поразрядная сортировка
 */
@SortingAlgorithm(
        name = "Поразрядная сортировка",
        description = "Сортирует числа по разрядам, начиная с младшего"
)
public class RadixSort extends Sorting {

    @Override
    public int[] sort(int[] array) {
        if (array == null || array.length < 2) {
            return array;
        }

        step(array, -1, -1); // Начало сортировки

        int max = array[0];
        for (int x : array) {
            if (x > max) max = x;
        }
        
        for (int exp = 1; max / exp > 0; exp *= 10) {
            countingSortByDigit(array, exp);
            step(array, -1, -1);
        }

        step(array, -1, -1);
        return array;
    }

    private void countingSortByDigit(int[] arr, int exp) {
        int n = arr.length;
        int[] output = new int[n];
        int[] count = new int[10];

        for (int x : arr) {
            int digit = (x / exp) % 10;
            count[digit]++;
        }

        for (int i = 1; i < 10; i++) {
            count[i] += count[i - 1];
        }

        for (int i = n - 1; i >= 0; i--) {
            int digit = (arr[i] / exp) % 10;
            output[count[digit] - 1] = arr[i];
            count[digit]--;
            
            // Визуализация перемещения элемента
            if (i % 2 == 0) { // Обновляем визуализацию не на каждом шаге для производительности
                step(arr, i, count[digit]);
            }
        }

        System.arraycopy(output, 0, arr, 0, n);
    }

    @Override
    public String getName() {
        return "Поразрядная сортировка (Radix Sort)";
    }
}
