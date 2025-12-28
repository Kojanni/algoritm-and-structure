package org.micro.kojanni.linear_sorting;

import org.micro.kojanni.visual.SortingAlgorithm;

/**
 * Сортировка подсчётом О(n)
 * для массивов, в которых данные представлены ограниченным количеством значений элементов.
 */
@SortingAlgorithm(
        name = "Сортировка подсчётом",
        description = "Эффективный алгоритм для сортировки целых чисел в небольшом диапазоне"
)
public class CountingSort extends Sorting {

    @Override
    public int[] sort(int[] array) {
        if (array == null || array.length < 2) {
            return array;
        }

        step(array, -1, -1); // Начало сортировки

        int max = array[0];
        int min = array[0];
        for (int i : array) {
            if (i > max) max = i;
            if (i < min) min = i;
        }
        int range = max - min + 1;

        int[] count = new int[range];
        for (int i : array) {
            count[i - min]++;
        }

        //Префиксные суммы
        for (int i = 1; i < range; i++) {
            count[i] += count[i - 1];
        }

        int[] output = new int[array.length];

        for (int i = array.length - 1; i >= 0; i--) {
            int element = array[i];
            int pos = --count[element - min];
            output[pos] = element;
            
            // Визуализация перемещения элемента
            if (i % 2 == 0) { // Обновляем визуализацию не на каждом шаге для производительности
                // Создаем временный массив для визуализации
                int[] temp = new int[array.length];
                System.arraycopy(output, 0, temp, 0, array.length);
                step(temp, i, pos);
            }
        }

        // Обновляем исходный массив и показываем финальное состояние
        System.arraycopy(output, 0, array, 0, array.length);
        step(array, -1, -1);

        return output;
    }

    @Override
    public String getName() {
        return "Сортировка подсчётом (Counting Sort)";
    }
}
