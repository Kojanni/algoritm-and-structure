package org.micro.kojanni.trie;

import org.micro.kojanni.hashtable.HashTableChaining;

import java.util.Random;

public class TrieMapTest {

    private static final int[] TEST_SIZES = {1000, 5000, 10000, 50000, 100000};
    private static final Random random = new Random(42);

    public static void main(String[] args) {
        for (int size : TEST_SIZES) {
            System.out.println("Размер теста: " + size + " элементов");

            String[] keys = generateKeys(size);
            Integer[] values = generateValues(size);

            testHashTableChaining(keys, values);
            testTrieMap(keys, values);

            System.out.println();
        }
    }

    private static Integer[] generateValues(int size) {
        Integer[] values = new Integer[size];
        for (int i = 0; i < size; i++) {
            values[i] = i;
        }
        shuffleArray(values);
        return values;
    }

    private static String[] generateKeys(int size) {
        String[] keys = new String[size];
        for (int i = 0; i < size; i++) {
            keys[i] = generateRandomKey(8 + (i % 5)); // Длина от 8 до 12 символов
        }
        return keys;
    }

    private static String generateRandomKey(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append((char) ('a' + random.nextInt(26)));
        }
        return sb.toString();
    }

    private static <T> void shuffleArray(T[] array) {
        for (int i = array.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            T temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
    }
    public static void testHashTableChaining(String[] keys, Integer[] values) {
        System.out.println("HashTableChaining");

        HashTableChaining<String, Integer> table = new HashTableChaining<>();

        long startTime = System.nanoTime();
        for (int i = 0; i < keys.length; i++) {
            table.put(keys[i], values[i]);
        }
        long insertTime = System.nanoTime() - startTime;

        startTime = System.nanoTime();
        for (String key : keys) {
            table.get(key);
        }
        long searchTime = System.nanoTime() - startTime;

        startTime = System.nanoTime();
        for (int i = 0; i < keys.length; i++) {
            table.get("miss" + i);
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

    private static void testTrieMap(String[] keys, Integer[] values) {
        System.out.println("TrieMap");

        TrieMap<Integer> map = new TrieMap<>();

        long startTime = System.nanoTime();
        for (int i = 0; i < keys.length; i++) {
            map.put(keys[i], values[i]);
        }
        long insertTime = System.nanoTime() - startTime;

        startTime = System.nanoTime();
        for (String key : keys) {
            map.get(key);
        }
        long searchTime = System.nanoTime() - startTime;

        startTime = System.nanoTime();
        for (int i = 0; i < keys.length; i++) {
            map.get(generateRandomKey(10)); // Генерируем случайные ключи для поиска
        }
        long missTime = System.nanoTime() - startTime;

        startTime = System.nanoTime();
        for (int i = 0; i < keys.length / 2; i++) {
            map.remove(keys[i]);
        }
        long deleteTime = System.nanoTime() - startTime;

        System.out.println("Вставка: " + formatTime(insertTime) + " (" + formatOpsPerSec(keys.length, insertTime) + " ops/sec)");
        System.out.println("Поиск (найден): " + formatTime(searchTime) + " (" + formatOpsPerSec(keys.length, searchTime) + " ops/sec)");
        System.out.println("Поиск (не найден): " + formatTime(missTime) + " (" + formatOpsPerSec(keys.length, missTime) + " ops/sec)");
        System.out.println("Удаление: " + formatTime(deleteTime) + " (" + formatOpsPerSec(keys.length / 2, deleteTime) + " ops/sec)");
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
