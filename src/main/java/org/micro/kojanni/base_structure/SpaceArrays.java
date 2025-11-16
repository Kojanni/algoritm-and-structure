package org.micro.kojanni.base_structure;

public class SpaceArrays {

    /**
     * Написать Реализацию класса SpaceArray(разрежённого) массив массивов с неполным заполнением.
     */
    public static class SpaceArray<T> {

        private final DynamicArray.MatrixArray<T> matrix;
        private final int segmentSize;

        public SpaceArray(int segmentSize) {
            this.segmentSize = segmentSize;
            this.matrix = new DynamicArray.MatrixArray<>(segmentSize);
        }

        public SpaceArray() {
            this(10);
        }

        //Добавление в конец массива
        public void add(T item) {
            matrix.add(item);
        }

        public void add(T item, int index) {
            if (index < 0) {
                System.out.println("Index must be >= 0");
                return;
            }

            // Если index больше текущего размера — заполняем null до него
            while (matrix.size() < index) {
                matrix.add(null);
            }

            matrix.add(item, index);
        }

        public T remove(int index) {
            return matrix.remove(index);
        }

        public int size() {
            return matrix.size();
        }

        public void debugPrint() {
            matrix.debugPrint();
        }

        public static void main(String[] args) {
            SpaceArray<Integer> arr = new SpaceArray<>(5);

            arr.add(10);
            arr.add(20);
            arr.add(30);

            arr.add(99, 12);
            arr.add(40);
            arr.add(50);

            System.out.println("SpaceArray:");
            arr.debugPrint();

            System.out.println("Remove index 1: " + arr.remove(1));
            arr.debugPrint();
        }
    }
}
