package org.micro.kojanni.pyramid_sorting;

/**
 * Сортировка выбором
 */
public class SelectionSort extends Sorting {

    @Override
    public int[] sort(int[] array) {
        int length = array.length;

        for (int i = 0; i < length - 1; i++) {
            int min = i;
            for (int j = i + 1; j < length; j++) {
                if (array[j] < array[min]) {
                    min = j;
                }
            }
            int temp = array[min];
            array[min] = array[i];
            array[i] = temp;
        }

        return array;
    }
}
