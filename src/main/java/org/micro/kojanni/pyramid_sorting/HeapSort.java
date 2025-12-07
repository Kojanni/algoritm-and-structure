package org.micro.kojanni.pyramid_sorting;

/**
 * Пирамидальная сортировка
 */
public class HeapSort extends Sorting {

    @Override
    public int[] sort(int[] array) {
        int length = array.length;

        for (int i = length / 2 - 1; i >= 0; i--) {
            heapify(array, length, i);
        }

        for (int i = length - 1; i >= 0; i--) {
            swap(array, 0, i);

            heapify(array, i, 0);
        }
        return array;
    }

    private void heapify(int[] array, int length, int root) {
        int largest = root;        // Наибольший элемент как корень
        int left = 2 * root + 1;   // Левый потомок
        int right = 2 * root + 2;  // Правый потомок

        if (left < length && array[left] > array[largest]) {
            largest = left;
        }

        if (right < length && array[right] > array[largest]) {
            largest = right;
        }

        if (largest != root) {
            swap(array, root, largest);

            heapify(array, length, largest);
        }
    }

    private void swap(int[] array, int target, int source) {
        int temp = array[target];
        array[target] = array[source];
        array[source] = temp;
    }
}
