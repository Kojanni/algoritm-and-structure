package org.micro.kojanni.simple_sorting;

/**
 * сортировка Шелла
 * оптимизация:
 * 1. последовательность Седжвика
 * Формулы:
 * 9*4^k - 9*2^k + 1
 * 4^k - 3*2^k + 1
 * <p>
 * 2. последовательность Хиббарда (Hibbard)
 * 2^k - 1
 */
public class ShellSort extends Sorting {

    @Override
    int[] sort(int[] array) {
        int length = array.length;
        int[] gaps = sedgewickGaps(length);

        for (int gap : gaps) {
            for (int i = gap; i < length; i++) {
                int temp = array[i];
                int j;
                for (j = i; j >= gap && array[j - gap] > temp; j -= gap) {
                    array[j] = array[j - gap];
                }
                array[j] = temp;
            }
        }
        return array;
    }

    private int[] sedgewickGaps(int length) {
        int[] tmp = new int[100];
        int count = 0;

        int k = 0;
        while (true) {
            int gap1 = 9 * (1 << (2 * k)) - 9 * (1 << k) + 1;
            int gap2 = (1 << (2 * k)) - 3 * (1 << k) + 1;

            boolean addedAnything = false;

            if (gap1 < length) {
                tmp[count++] = gap1;
                addedAnything = true;
            }
            if (gap2 < length && gap2 > 0) {
                tmp[count++] = gap2;
                addedAnything = true;
            }

            if (!addedAnything)
                break;

            k++;
        }

        // сокращаем размер
        int[] gaps = new int[count];
        System.arraycopy(tmp, 0, gaps, 0, count);

        for (int i = 0; i < gaps.length; i++) {
            for (int j = 0; j < gaps.length - 1; j++) {
                if (gaps[j] < gaps[j + 1]) {
                    int t = gaps[j];
                    gaps[j] = gaps[j + 1];
                    gaps[j + 1] = t;
                }
            }
        }

        return gaps;
    }

    int[] sortHibbard(int[] array) {
        int length = array.length;
        int[] gaps = hibbardGaps(length);

        for (int gap : gaps) {
            for (int i = gap; i < length; i++) {
                int temp = array[i];
                int j;
                for (j = i; j >= gap && array[j - gap] > temp; j -= gap) {
                    array[j] = array[j - gap];
                }
                array[j] = temp;
            }
        }
        return array;
    }

    private int[] hibbardGaps(int length) {
        int[] tmp = new int[100];
        int count = 0;

        int k = 1;
        while (true) {
            int gap = (1 << k) - 1;

            if (gap >= length)
                break;

            tmp[count++] = gap;
            k++;
        }

        // сокращаем размер
        int[] gaps = new int[count];
        System.arraycopy(tmp, 0, gaps, 0, count);

        for (int i = 0, j = gaps.length - 1; i < j; i++, j--) {
            int t = gaps[i];
            gaps[i] = gaps[j];
            gaps[j] = t;
        }

        return gaps;
    }

    int[] sortSimple(int[] array) {
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
