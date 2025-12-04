package org.micro.kojanni.simple_sorting;

/**
 * сортировка Шелла
 */
public class ShellSort extends Sorting {

    @Override
    int[] sort(int[] array) {
        int length = array.length;
        for (int gap = length / 2; gap > 0; gap--) {
            for (int i = gap; i < length; i++) {
                int temp = array[i];
                int j;
                for (j = i; j >= gap && array[j - gap] > temp; j -= gap) {
                    array[j] = array[j - gap];
                    array[j - gap] = temp;
                }
            }
        }
        return array;
    }
}
