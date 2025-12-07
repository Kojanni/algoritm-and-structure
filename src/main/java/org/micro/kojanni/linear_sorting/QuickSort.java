package org.micro.kojanni.linear_sorting;

/**
 * Быстрая сортировка
 */
public class QuickSort extends Sorting {

    @Override
    public int[] sort(int[] array) {
        qSort(array, 0, array.length - 1);

        return array;
    }

    private void qSort(int[] array, int left, int right) {
        if (left >= right) return;

        int pivot = array[(left + right) >>> 1];
        int i = left;
        int j = right;

        while (i <= j) {
            while (array[i] < pivot) i++;
            while (array[j] > pivot) j--;

            if (i <= j) {
                int tmp = array[i];
                array[i] = array[j];
                array[j] = tmp;
                i++;
                j--;
            }
        }

        if (left < j) qSort(array, left, j);
        if (i < right) qSort(array, i, right);
    }
}
