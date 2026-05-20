package org.micro.kojanni.probabilistic;

import java.util.BitSet;

/**
 * Реализация Bloom Filter - вероятностной структуры данных для проверки принадлежности элемента множеству.
 * 
 * Особенности:
 * - Может давать false positives (ложные срабатывания), но никогда не дает false negatives
 * - Очень эффективен по памяти
 * - Операции add и contains выполняются за O(k), где k - количество хеш-функций
 * 
 * @param <T> тип элементов в фильтре
 */
public class BloomFilter<T> {
    
    private final BitSet bitSet;
    private final int size;
    private final int hashFunctionCount;
    private int elementCount;
    
    /**
     * Создает Bloom Filter с заданными параметрами.
     * 
     * @param expectedElements ожидаемое количество элементов
     * @param falsePositiveProbability желаемая вероятность ложных срабатываний (например, 0.01 для 1%)
     */
    public BloomFilter(int expectedElements, double falsePositiveProbability) {
        this.size = optimalSize(expectedElements, falsePositiveProbability);
        this.hashFunctionCount = optimalHashFunctionCount(expectedElements, size);
        this.bitSet = new BitSet(size);
        this.elementCount = 0;
    }
    
    /**
     * Создает Bloom Filter с явным указанием размера и количества хеш-функций.
     */
    public BloomFilter(int size, int hashFunctionCount) {
        this.size = size;
        this.hashFunctionCount = hashFunctionCount;
        this.bitSet = new BitSet(size);
        this.elementCount = 0;
    }
    
    /**
     * Вычисляет оптимальный размер битового массива.
     * Формула: m = -n * ln(p) / (ln(2)^2)
     * где n - количество элементов, p - вероятность ложных срабатываний
     */
    private static int optimalSize(int n, double p) {
        if (p <= 0 || p >= 1) {
            throw new IllegalArgumentException("False positive probability must be between 0 and 1");
        }
        return (int) Math.ceil(-n * Math.log(p) / (Math.log(2) * Math.log(2)));
    }
    
    /**
     * Вычисляет оптимальное количество хеш-функций.
     * Формула: k = (m/n) * ln(2)
     * где m - размер битового массива, n - количество элементов
     */
    private static int optimalHashFunctionCount(int n, int m) {
        return Math.max(1, (int) Math.round((double) m / n * Math.log(2)));
    }
    
    /**
     * Добавляет элемент в фильтр.
     */
    public void add(T element) {
        for (int i = 0; i < hashFunctionCount; i++) {
            int hash = hash(element, i);
            bitSet.set(hash);
        }
        elementCount++;
    }
    
    /**
     * Проверяет, может ли элемент присутствовать в фильтре.
     * 
     * @return true если элемент возможно присутствует (может быть false positive),
     *         false если элемент точно отсутствует
     */
    public boolean mightContain(T element) {
        for (int i = 0; i < hashFunctionCount; i++) {
            int hash = hash(element, i);
            if (!bitSet.get(hash)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Вычисляет i-ю хеш-функцию для элемента.
     * Использует технику double hashing с MurmurHash3: h_i(x) = (h1(x) + i * h2(x)) mod m
     * 
     * MurmurHash3 обеспечивает отличное распределение и низкую вероятность коллизий.
     */
    private int hash(T element, int i) {
        // Используем MurmurHash3 для получения двух независимых хешей
        long[] hashes = MurmurHash3.hash128(element, 0);
        long hash1 = hashes[0];
        long hash2 = hashes[1];

        long combinedHash = hash1 + (long) i * hash2;
        
        // Безопасное приведение к положительному значению и взятие по модулю
        // Используем побитовое И с 0x7FFFFFFFFFFFFFFFL вместо Math.abs для избежания проблемы с MIN_VALUE
        return (int) ((combinedHash & 0x7FFFFFFFFFFFFFFFL) % size);
    }
    
    /**
     * Возвращает текущую вероятность ложных срабатываний.
     * Формула: p = (1 - e^(-kn/m))^k
     * где k - количество хеш-функций, n - количество элементов, m - размер битового массива
     */
    public double getCurrentFalsePositiveProbability() {
        if (elementCount == 0) {
            return 0.0;
        }
        double exponent = -(double) hashFunctionCount * elementCount / size;
        return Math.pow(1 - Math.exp(exponent), hashFunctionCount);
    }

    public void clear() {
        bitSet.clear();
        elementCount = 0;
    }

    public int getElementCount() {
        return elementCount;
    }

    public int getSize() {
        return size;
    }

    public int getHashFunctionCount() {
        return hashFunctionCount;
    }

    public double getFillRatio() {
        return (double) bitSet.cardinality() / size;
    }
    
    @Override
    public String toString() {
        return String.format("BloomFilter[size=%d, hashFunctions=%d, elements=%d, fillRatio=%.2f%%, fpProbability=%.4f%%]",
                size, hashFunctionCount, elementCount, getFillRatio() * 100, getCurrentFalsePositiveProbability() * 100);
    }
}
