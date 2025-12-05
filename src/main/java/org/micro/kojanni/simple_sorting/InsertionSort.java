package org.micro.kojanni.simple_sorting;

/**
 * сортировка вставками(включениями)
 */
public class InsertionSort extends Sorting {

    @Override
    int[] sort(int[] array) {
        for (int i = 1; i < array.length; i++) {
            int value = array[i];

            int left = 0;
            int right = i;
            while (left < right) {
                int mid = (left + right) / 2;
                if (array[mid] < value) {
                    left = mid + 1;
                } else {
                    right = mid;
                }
            }

            for (int j = i; j > left; j--) {
                array[j] = array[j - 1];
            }
            array[left] = value;
        }
        return array;
    }

    int[] sortShift(int[] array) {
        for (int i = 1; i < array.length; i++) {
            int value = array[i];
            int index = i - 1;
            while (index >= 0 && array[index] > value) {
                array[index + 1] = array[index];

                index--;
            }
            array[index] = value;
        }
        return array;
    }

    int[] sortSimple(int[] array) {
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
