package org.micro.kojanni.linear_sorting;

/**
 *
 * Блочная сортировка, Карманная сортировка, корзинная сортировка
 */
public class BucketSort extends Sorting {

    private static final int NUM_BASKETS = 10;

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

        int[][] buckets = new int[NUM_BASKETS][array.length];
        int[] bucketCounts = new int[NUM_BASKETS];


        for (int i = 0; i < array.length; i++) {
            int m = (int) ((long)(array[i] - min) * NUM_BASKETS / (max - min + 1));
            if (m >= NUM_BASKETS) m = NUM_BASKETS - 1;

            int count = bucketCounts[m];
            int j = count - 1;
            while (j >= 0 && buckets[m][j] > array[i]) {
                buckets[m][j + 1] = buckets[m][j];
                j--;
            }
            buckets[m][j + 1] = array[i];
            bucketCounts[m]++;
        }

        int idx = 0;
        for (int b = 0; b < NUM_BASKETS; b++) {
            for (int k = 0; k < bucketCounts[b]; k++) {
                array[idx++] = buckets[b][k];
            }
        }

        return array;
    }
}
