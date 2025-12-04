package org.micro.kojanni.simple_sorting;

/**
 * сортировка вставками(включениями)
 */
public class InsertionSort extends Sorting {

    @Override
    int[] sort(int[] array) {
        for (int i = 1; i < array.length; i++) {
            int value = array[i];
            int index = i - 1;
            while (index >= 0 && array[index] > value) {
                array[index + 1] = array[index];
                array[index] = value;
                index--;
            }
        }
        return array;
    }
}
