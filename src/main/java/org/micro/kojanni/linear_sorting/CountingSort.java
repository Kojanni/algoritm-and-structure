package org.micro.kojanni.linear_sorting;

/**
 * Сортировка подсчётом О(n)
 * для массивов, в которых данные представлены ограниченным количеством значений элементов.
 */
public class CountingSort extends Sorting {

    @Override
    public int[] sort(int[] array) {
        if (array == null || array.length < 2) {
            return array;
        }

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
            int pos = count[element - min] - 1;
            output[pos] = element;
            count[element - min]--;
        }

        return output;
    }
}
