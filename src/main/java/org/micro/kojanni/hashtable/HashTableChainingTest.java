package org.micro.kojanni.hashtable;

import java.util.Random;

public class HashTableChainingTest {

    private static final int[] TEST_SIZES = {1000, 5000, 10000, 50000, 100000};
    private static final Random random = new Random(42);

    public static void main(String[] args) {
        for (int size : TEST_SIZES) {
            System.out.println("Размер теста: " + size + " элементов");

            Integer[] keys = generateKeys(size);
            String[] values = generateValues(size);

            testHashTableChaining(keys, values);
        }
    }

    /**
     * Генерирует массив уникальных ключей
     */
    private static Integer[] generateKeys(int size) {
        Integer[] keys = new Integer[size];
        for (int i = 0; i < size; i++) {
            keys[i] = i;
        }
        // Перемешиваем для случайного порядка
        shuffleArray(keys);
        return keys;
    }

    /**
     * Генерирует массив значений
     */
    private static String[] generateValues(int size) {
        String[] values = new String[size];
        for (int i = 0; i < size; i++) {
            values[i] = "Value_" + i;
        }
        return values;
    }

    /**
     * Перемешивает массив (Fisher-Yates shuffle)
     */
    private static <T> void shuffleArray(T[] array) {
        for (int i = array.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            T temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
    }

    private static void testHashTableChaining(Integer[] keys, String[] values) {
        System.out.println("HashTableChaining");

        HashTableChaining<Integer, String> table = new HashTableChaining<>();

        long startTime = System.nanoTime();
        for (int i = 0; i < keys.length; i++) {
            table.put(keys[i], values[i]);
        }
        long insertTime = System.nanoTime() - startTime;

        startTime = System.nanoTime();
        for (Integer key : keys) {
            table.get(key);
        }
        long searchTime = System.nanoTime() - startTime;

        startTime = System.nanoTime();
        for (int i = 0; i < keys.length; i++) {
            table.get(keys.length + i);
        }
        long missTime = System.nanoTime() - startTime;

        startTime = System.nanoTime();
        for (int i = 0; i < keys.length / 2; i++) {
            table.remove(keys[i]);
        }
        long deleteTime = System.nanoTime() - startTime;

        System.out.println("Вставка: " + formatTime(insertTime) + " (" + formatOpsPerSec(keys.length, insertTime) + " ops/sec)");
        System.out.println("Поиск (найден): " + formatTime(searchTime) + " (" + formatOpsPerSec(keys.length, searchTime) + " ops/sec)");
        System.out.println("Поиск (не найден): " + formatTime(missTime) + " (" + formatOpsPerSec(keys.length, missTime) + " ops/sec)");
        System.out.println("Удаление: " + formatTime(deleteTime) + " (" + formatOpsPerSec(keys.length / 2, deleteTime) + " ops/sec)");
        table.getInformation();
    }

    private static String formatTime(long nanos) {
        double millis = nanos / 1_000_000.0;
        return String.format("%8.2f ms", millis);
    }

    private static String formatOpsPerSec(int operations, long nanos) {
        double seconds = nanos / 1_000_000_000.0;
        long opsPerSec = (long) (operations / seconds);
        return String.format("%,d", opsPerSec);
    }
}
