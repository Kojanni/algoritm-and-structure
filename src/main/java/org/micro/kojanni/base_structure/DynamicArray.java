package org.micro.kojanni.base_structure;

import java.util.Arrays;

/**
 * Динамические массивы.
 * <p>
 * Написать метод добавления и удаления элементов:
 * void add(T item, int index);
 * T remove(int index); // возвращает удаляемый элемент
 * по индексу во все варианты динамических массивов:
 * SingleArray, VectorArray, FactorArray, MatrixArray
 */
public class DynamicArray {

    /**
     * SingleArray (простой массив) - каждый раз создает новый массив при добавлении/удалении
     */
    public static class SingleArray<T> implements IDynamicArray<T> {
        private T[] values;
        private int size;

        public SingleArray() {
            values = (T[]) new Object[0];
            size = 0;
        }

        public void add(T item, int index) {
            if (index < 0 || index > size) {
                System.out.print("Index not exist. ");
                return;
            }
            T[] perValues = (T[]) new Object[size + 1];

            // Копируем элементы до index
            System.arraycopy(values, 0, perValues, 0, index);
            // Вставляем новый элемент
            perValues[index] = item;
            // Копируем элементы после index
            System.arraycopy(values, index, perValues, index + 1, size - index);

            values = perValues;
            size++;
        }

        @Override
        public T remove(int index) {
            if (index < 0 || index >= size) {
                System.out.print("Index not exist. ");
                return null;
            }
            T value = values[index];
            T[] perValues = (T[]) new Object[size - 1];

            // Копируем элементы до index
            System.arraycopy(values, 0, perValues, 0, index);
            // Копируем элементы после index
            System.arraycopy(values, index + 1, perValues, index, size - index - 1);

            values = perValues;
            size--;
            return value;
        }

        public int size() {
            return size;
        }

        @Override
        public T get(int index) {
            if (index < 0 || index >= size) {
                throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
            }
            return values[index];
        }
    }

    /**
     *  VectorArray (массив с фиксированным шагом)
     */
    public static class VectorArray<T> implements IDynamicArray<T>{
        private T[] values;
        private int size;
        private int vector;

        public VectorArray(int vector) {
            this.vector = vector;
            values = (T[]) new Object[vector];
            size = 0;
        }

        public VectorArray() {
            this.vector = 10;
            values = (T[]) new Object[vector];
            size = 0;
        }

        @Override
        public void add(T item, int index) {
            if (index < 0 || index > size) {
                System.out.print("Index not exist. ");
                return;
            }

            // Проверяем, нужно ли расширять массив
            if (size == values.length) {
                T[] perValues = (T[]) new Object[values.length + vector];
                System.arraycopy(values, 0, perValues, 0, values.length);
                values = perValues;
            }

            // Сдвигаем элементы вправо
            for (int i = size; i > index; i--) {
                values[i] = values[i - 1];
            }

            values[index] = item;
            size++;
        }

        @Override
        public T remove(int index) {
            if (index < 0 || index >= size) {
                System.out.print("Index not exist. ");
                return null;
            }

            T value = values[index];

            // Сдвигаем элементы влево
            for (int i = index; i < size - 1; i++) {
                values[i] = values[i + 1];
            }

            values[size - 1] = null;
            size--;
            return value;
        }

        public int size() {
            return size;
        }

        @Override
        public T get(int index) {
            if (index < 0 || index >= size) {
                throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
            }
            return values[index];
        }
    }

    /**
     * FactorArray (массив с умножением размера)
     */
    public static class FactorArray<T> implements IDynamicArray<T> {
        private T[] values;
        private int size;
        private double factor;

        public FactorArray(double factor, int initLength) {
            this.factor = factor;
            values = (T[]) new Object[initLength];
            size = 0;
        }

        public FactorArray() {
            this(1.5, 10); // по умолчанию
        }

        @Override
        public void add(T item, int index) {
            if (index < 0 || index > size) {
                System.out.print("Index not exist. ");
                return;
            }

            // Проверяем, нужно ли расширять массив
            if (size == values.length) {
                int newLength = (int) (values.length * factor) + 1;
                T[] newArray = (T[]) new Object[newLength];
                System.arraycopy(values, 0, newArray, 0, values.length);
                values = newArray;
            }

            // Сдвигаем элементы вправо
            for (int i = size; i > index; i--) {
                values[i] = values[i - 1];
            }

            values[index] = item;
            size++;
        }

        @Override
        public T remove(int index) {
            if (index < 0 || index >= size) {
                System.out.print("Index not exist. ");
                return null;
            }

            T removed = values[index];

            // Сдвигаем элементы влево
            for (int i = index; i < size - 1; i++) {
                values[i] = values[i + 1];
            }

            values[size - 1] = null;
            size--;
            return removed;
        }

        public int size() {
            return size;
        }

        @Override
        public T get(int index) {
            if (index < 0 || index >= size) {
                throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
            }
            return values[index];
        }
    }

    /**
     *  MatrixArray (матричный массив, свободный массив) - использует массив массивов для эффективной работы с памятью
     */
    public static class MatrixArray<T> implements IDynamicArray<T> {
        private int size;
        private int segmentSize;
        private T[][] values;

        public MatrixArray(int segmentSize) {
            this.segmentSize = segmentSize;
            this.size = 0;
            this.values = (T[][]) new Object[1][];
            values[0] = (T[]) new Object[segmentSize];
        }

        public MatrixArray() {
            this(10); // по умолчанию
        }

        @Override
        public void add(T item, int index) {
            if (index < 0 || index > size) {
                System.out.print("Index not exist. Index: " + index + ", Size: " + size);
                return;
            }

            // Проверяем, нужно ли расширять массив сегментов
            if (size == segmentSize * values.length) {
                expandSegments();
            }

            // Находим сегмент и позицию для вставки
            int segmentIndex = index / segmentSize;
            int positionInSegment = index % segmentSize;

            // Сдвигаем элементы вправо, начиная с конца
            shiftElementsRight(segmentIndex, positionInSegment);

            // Вставляем новый элемент
            values[segmentIndex][positionInSegment] = item;
            size++;
        }

        @Override
        public T remove(int index) {
            if (index < 0 || index >= size) {
                System.out.print("Index not exist. Index: " + index + ", Size: " + size);
                return null;
            }

            // Находим сегмент и позицию
            int segmentIndex = index / segmentSize;
            int positionInSegment = index % segmentSize;

            // Сохраняем удаляемый элемент
            T removedItem = values[segmentIndex][positionInSegment];

            // Сдвигаем элементы влево
            shiftElementsLeft(segmentIndex, positionInSegment);

            // Очищаем последний элемент
            int lastSegmentIndex = (size - 1) / segmentSize;
            int lastPositionInSegment = (size - 1) % segmentSize;
            values[lastSegmentIndex][lastPositionInSegment] = null;

            size--;

            // Опционально: можно сжать массив сегментов, если много пустых
            if (size > 0 && values.length > 1 && size <= segmentSize * (values.length - 1)) {
                compressSegments();
            }

            return removedItem;
        }


        @Override
        public int size() {
            return size;
        }

        @Override
        public T get(int index) {
            if (index < 0 || index >= size) {
                throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
            }
            int segmentIndex = index / segmentSize;
            int positionInSegment = index % segmentSize;
            return values[segmentIndex][positionInSegment];
        }

        /**
         * Расширяет массив сегментов, добавляя один новый сегмент
         */
        private void expandSegments() {
            int newSegmentsCount = values.length + 1;
            T[][] newValues = (T[][]) new Object[newSegmentsCount][];

            // Копируем существующие сегменты
            System.arraycopy(values, 0, newValues, 0, values.length);

            // Создаем новый сегмент
            newValues[values.length] = (T[]) new Object[segmentSize];

            values = newValues;
        }

        /**
         * Сжимает массив сегментов, удаляя пустые сегменты в конце
         */
        private void compressSegments() {
            int neededSegments = (int) Math.ceil((double) size / segmentSize);
            if (neededSegments < values.length) {
                T[][] newValues = (T[][]) new Object[neededSegments][];
                System.arraycopy(values, 0, newValues, 0, neededSegments);
                values = newValues;
            }
        }

        /**
         * Сдвигает элементы вправо для освобождения места под новый элемент
         */
        private void shiftElementsRight(int startSegmentIndex, int startPositionInSegment) {
            int currentSegmentIndex = values.length - 1;
            int currentPositionInSegment = segmentSize - 1;

            // Ищем последний занятый элемент
            while (currentSegmentIndex >= 0 && values[currentSegmentIndex][currentPositionInSegment] == null) {
                currentPositionInSegment--;
                if (currentPositionInSegment < 0) {
                    currentSegmentIndex--;
                    currentPositionInSegment = segmentSize - 1;
                }
            }

            // Сдвигаем элементы, начиная с конца
            while (currentSegmentIndex > startSegmentIndex ||
                    (currentSegmentIndex == startSegmentIndex && currentPositionInSegment >= startPositionInSegment)) {

                // Вычисляем следующую позицию для этого элемента
                int nextSegmentIndex = currentSegmentIndex;
                int nextPositionInSegment = currentPositionInSegment + 1;

                if (nextPositionInSegment >= segmentSize) {
                    nextSegmentIndex++;
                    nextPositionInSegment = 0;
                }

                // Перемещаем элемент
                if (nextSegmentIndex < values.length) {
                    values[nextSegmentIndex][nextPositionInSegment] =
                            values[currentSegmentIndex][currentPositionInSegment];
                }

                // Переходим к предыдущему элементу
                currentPositionInSegment--;
                if (currentPositionInSegment < 0) {
                    currentSegmentIndex--;
                    currentPositionInSegment = segmentSize - 1;
                }
            }
        }

        /**
         * Сдвигает элементы влево после удаления элемента
         */
        private void shiftElementsLeft(int startSegmentIndex, int startPositionInSegment) {
            int currentSegmentIndex = startSegmentIndex;
            int currentPositionInSegment = startPositionInSegment;

            while (currentSegmentIndex < values.length) {
                // Вычисляем следующую позицию
                int nextSegmentIndex = currentSegmentIndex;
                int nextPositionInSegment = currentPositionInSegment + 1;

                if (nextPositionInSegment >= segmentSize) {
                    nextSegmentIndex++;
                    nextPositionInSegment = 0;
                }

                // Если это последний элемент - выходим
                if (nextSegmentIndex >= values.length ||
                        (nextSegmentIndex == (size - 1) / segmentSize &&
                                nextPositionInSegment > (size - 1) % segmentSize)) {
                    break;
                }

                // Перемещаем следующий элемент на текущую позицию
                values[currentSegmentIndex][currentPositionInSegment] =
                        values[nextSegmentIndex][nextPositionInSegment];

                currentSegmentIndex = nextSegmentIndex;
                currentPositionInSegment = nextPositionInSegment;
            }
        }

        /**
         * Добавляет элемент в конец массива
         */
        public void add(T item) {
            add(item, size);
        }

        public void debugPrint() {
            System.out.println("MatrixArray (size: " + size + ", segments: " + values.length + ")");
            for (int i = 0; i < values.length; i++) {
                System.out.print("Segment " + i + ": [");
                for (int j = 0; j < segmentSize; j++) {
                    if (values[i][j] != null) {
                        System.out.print(values[i][j]);
                    } else {
                        System.out.print("null");
                    }
                    if (j < segmentSize - 1) {
                        System.out.print(", ");
                    }
                }
                System.out.println("]");
            }
        }
    }


    public static void main(String[] args) {
        System.out.println("SingleArray: ");
        SingleArray<Integer> singleArray = new SingleArray<>();
        singleArray.add(1, 3);
        System.out.println("Result add [0] - " + 3 + ": " + Arrays.toString(singleArray.values));

        for (int i = 0; i < 30; i ++) {
            singleArray.add(i, i);
            System.out.println("Result add [" + i + "] - " + i + ": " + Arrays.toString(singleArray.values));
        }
        singleArray.add(4, 33);
        System.out.println("Result add [33] - 4: " + Arrays.toString(singleArray.values));

        System.out.println("Remove [0]: " + singleArray.remove(0) + ": " + Arrays.toString(singleArray.values));
        System.out.println("Remove [100]: " + singleArray.remove(100) + ": " + Arrays.toString(singleArray.values));

        System.out.println("VectorArray: ");
        VectorArray<Integer> vectorArray = new VectorArray<>(2);
        vectorArray.add(1, 3);
        System.out.println("Result add [0] - " + 3 + ": " + Arrays.toString(vectorArray.values));

        for (int i = 0; i < 30; i ++) {
            vectorArray.add(i, i);
            System.out.println("Result add [" + i + "] - " + i + ": " + Arrays.toString(vectorArray.values));
        }
        vectorArray.add(4, 33);
        System.out.println("Result add [33] - 4: " + Arrays.toString(vectorArray.values));

        System.out.println("Remove [0]: " + vectorArray.remove(0) + ": " + Arrays.toString(vectorArray.values));
        System.out.println("Remove [100]: " + vectorArray.remove(100) + ": " + Arrays.toString(vectorArray.values));

        System.out.println("FactorArray: ");
        FactorArray<Integer> factorArray = new FactorArray<>();
        factorArray.add(1, 3);
        System.out.println("Result add [0] - " + 3 + ": " + Arrays.toString(factorArray.values));

        for (int i = 0; i < 30; i ++) {
            factorArray.add(i, i);
            System.out.println("Result add [" + i + "] - " + i + ": " + Arrays.toString(factorArray.values));
        }
        factorArray.add(4, 33);
        System.out.println("Result add [33] - 4: " + Arrays.toString(factorArray.values));

        System.out.println("Remove [0]: " + factorArray.remove(0) + ": " + Arrays.toString(factorArray.values));
        System.out.println("Remove [100]: " + factorArray.remove(100) + ": " + Arrays.toString(factorArray.values));

        System.out.println("MatrixArray: ");
        MatrixArray<Integer> matrixArray = new MatrixArray<>();
        matrixArray.add(1, 3);
        System.out.println("Result add [0] - " + 3 + ": " );
        matrixArray.debugPrint();

        for (int i = 0; i < 30; i ++) {
            matrixArray.add(i, i);
            System.out.println("Result add [" + i + "] - " + i + ": ");
            matrixArray.debugPrint();
        }
        matrixArray.add(4, 33);
        System.out.println("Result add [33] - 4: ");
        matrixArray.debugPrint();

        System.out.println("Remove [0]: " + matrixArray.remove(0) + ": ");
        matrixArray.debugPrint();

        System.out.println("\nRemove [100]: " + matrixArray.remove(100) + ": ");
        matrixArray.debugPrint();
    }
}
