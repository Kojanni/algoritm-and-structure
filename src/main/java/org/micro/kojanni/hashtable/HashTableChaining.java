package org.micro.kojanni.hashtable;

import java.util.LinkedList;

/**
 * Метод цепочек
 */
public class HashTableChaining<K, V> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private LinkedList<Entry<K, V>>[] table;

    private int size;
    private int capacity;

    public HashTableChaining() {
        this(DEFAULT_CAPACITY);
    }

    public HashTableChaining(int capacity) {
        this.capacity = capacity;
        this.table = new LinkedList[capacity];
        this.size = 0;
    }

    private int hash(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode()) % capacity;
    }

    public V put(K key, V value) {
        if ((double) size / capacity >= LOAD_FACTOR) {
            resize();
        }

        int index = hash(key);

        if (table[index] == null) {
            table[index] = new LinkedList<>();
        }

        for (Entry<K, V> entry : table[index]) {
            if (entry.key == null ? key == null : entry.key.equals(key)) {
                V oldValue = entry.value;
                entry.value = value;
                return oldValue;
            }
        }

        table[index].add(new Entry<>(key, value));
        size++;
        return null;
    }

    public V get(K key) {
        int index = hash(key);

        if (table[index] == null) {
            return null;
        }

        for (Entry<K, V> entry : table[index]) {
            if (entry.key == null ? key == null : entry.key.equals(key)) {
                return entry.value;
            }
        }

        return null;
    }

    public V remove(K key) {
        int index = hash(key);

        if (table[index] == null) {
            return null;
        }

        Entry<K, V> toRemove = null;
        for (Entry<K, V> entry : table[index]) {
            if (entry.key == null ? key == null : entry.key.equals(key)) {
                toRemove = entry;
                break;
            }
        }

        if (toRemove != null) {
            table[index].remove(toRemove);
            size--;
            return toRemove.value;
        }

        return null;
    }

    public boolean containsKey(K key) {
        return get(key) != null;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        table = new LinkedList[capacity];
        size = 0;
    }

    private void resize() {
        int newCapacity = capacity * 2;
        LinkedList<Entry<K, V>>[] oldTable = table;

        table = new LinkedList[newCapacity];
        capacity = newCapacity;
        size = 0;

        for (LinkedList<Entry<K, V>> bucket : oldTable) {
            if (bucket != null) {
                for (Entry<K, V> entry : bucket) {
                    put(entry.key, entry.value);
                }
            }
        }
    }

    public String getInformation() {
        int usedBuckets = 0;
        int maxChainLength = 0;
        int totalChainLength = 0;

        for (LinkedList<Entry<K, V>> bucket : table) {
            if (bucket != null && !bucket.isEmpty()) {
                usedBuckets++;
                int chainLength = bucket.size();
                totalChainLength += chainLength;
                maxChainLength = Math.max(maxChainLength, chainLength);
            }
        }

        double avgChainLength = usedBuckets > 0 ? (double) totalChainLength / usedBuckets : 0;
        double loadFactor = (double) size / capacity;

        return String.format(
            "Capacity: %d, Size: %d, Load Factor: %.2f, Used Buckets: %d, " +
            "Avg Chain Length: %.2f, Max Chain Length: %d",
            capacity, size, loadFactor, usedBuckets, avgChainLength, maxChainLength
        );
    }

    private static class Entry<K, V> {
        final K key;
        V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
