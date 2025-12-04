package org.micro.kojanni.simple_sorting;

/**
 * Сортировка обменом(метод пузырька)
 */
public class BubbleSort extends Sorting {

    public int[] sortSimple(int[] array) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array.length - i - 1; j++) {
                if (array[j] > array[j + 1]) {
                    int temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
            }
        }
        return array;
    }

    @Override
    public int[] sort(int[] array) {
        int length = array.length;

        while (length > 1) {
            int position = 0;

            for (int i = 1; i < length; i++) {
                if (array[i - 1] > array[i]) {
                    int temp = array[i];
                    array[i] = array[i - 1];
                    array[i - 1] = temp;

                    position = i;
                }
            }
            length = position;
        }
        return array;
    }
}
