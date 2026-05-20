package org.micro.kojanni.probabilistic;

/**
 * Реализация MurmurHash3 для качественного хеширования.
 * Основано на оригинальной реализации Austin Appleby.
 * 
 * MurmurHash3 обеспечивает:
 * - Отличное распределение битов
 * - Высокую скорость
 * - Низкую вероятность коллизий
 */
public class MurmurHash3 {
    
    private static final int C1_32 = 0xcc9e2d51;
    private static final int C2_32 = 0x1b873593;
    private static final int R1_32 = 15;
    private static final int R2_32 = 13;
    private static final int M_32 = 5;
    private static final int N_32 = 0xe6546b64;
    
    private static final long C1_64 = 0x87c37b91114253d5L;
    private static final long C2_64 = 0x4cf5ad432745937fL;
    private static final int R1_64 = 31;
    private static final int R2_64 = 27;
    private static final int R3_64 = 33;
    private static final int M_64 = 5;
    private static final long N1_64 = 0x52dce729;
    private static final long N2_64 = 0x38495ab5;
    
    /**
     * Вычисляет 32-битный хеш для объекта.
     */
    public static int hash32(Object key, int seed) {
        if (key == null) {
            return 0;
        }
        
        byte[] data = key.toString().getBytes();
        return hash32(data, 0, data.length, seed);
    }
    
    /**
     * Вычисляет 32-битный хеш для байтового массива.
     */
    public static int hash32(byte[] data, int offset, int length, int seed) {
        int h1 = seed;
        int roundedEnd = offset + (length & 0xfffffffc); // round down to 4 byte block
        
        for (int i = offset; i < roundedEnd; i += 4) {
            int k1 = (data[i] & 0xff) | ((data[i + 1] & 0xff) << 8) |
                     ((data[i + 2] & 0xff) << 16) | (data[i + 3] << 24);
            
            k1 *= C1_32;
            k1 = Integer.rotateLeft(k1, R1_32);
            k1 *= C2_32;
            
            h1 ^= k1;
            h1 = Integer.rotateLeft(h1, R2_32);
            h1 = h1 * M_32 + N_32;
        }
        
        // tail
        int k1 = 0;
        switch (length & 0x03) {
            case 3:
                k1 = (data[roundedEnd + 2] & 0xff) << 16;
            case 2:
                k1 |= (data[roundedEnd + 1] & 0xff) << 8;
            case 1:
                k1 |= (data[roundedEnd] & 0xff);
                k1 *= C1_32;
                k1 = Integer.rotateLeft(k1, R1_32);
                k1 *= C2_32;
                h1 ^= k1;
        }
        
        // finalization
        h1 ^= length;
        h1 = fmix32(h1);
        
        return h1;
    }
    
    /**
     * Вычисляет 64-битный хеш для объекта.
     */
    public static long hash64(Object key, int seed) {
        if (key == null) {
            return 0;
        }
        
        byte[] data = key.toString().getBytes();
        return hash64(data, 0, data.length, seed);
    }
    
    /**
     * Вычисляет 64-битный хеш для байтового массива.
     */
    public static long hash64(byte[] data, int offset, int length, int seed) {
        long h1 = seed & 0xFFFFFFFFL;
        long h2 = seed & 0xFFFFFFFFL;
        
        int roundedEnd = offset + (length & 0xfffffff0); // round down to 16 byte block
        
        for (int i = offset; i < roundedEnd; i += 16) {
            long k1 = getLong(data, i);
            long k2 = getLong(data, i + 8);
            
            k1 *= C1_64;
            k1 = Long.rotateLeft(k1, R1_64);
            k1 *= C2_64;
            h1 ^= k1;
            
            h1 = Long.rotateLeft(h1, R2_64);
            h1 += h2;
            h1 = h1 * M_64 + N1_64;
            
            k2 *= C2_64;
            k2 = Long.rotateLeft(k2, R3_64);
            k2 *= C1_64;
            h2 ^= k2;
            
            h2 = Long.rotateLeft(h2, R1_64);
            h2 += h1;
            h2 = h2 * M_64 + N2_64;
        }
        
        // tail
        long k1 = 0;
        long k2 = 0;
        
        switch (length & 15) {
            case 15: k2 ^= ((long) data[roundedEnd + 14] & 0xff) << 48;
            case 14: k2 ^= ((long) data[roundedEnd + 13] & 0xff) << 40;
            case 13: k2 ^= ((long) data[roundedEnd + 12] & 0xff) << 32;
            case 12: k2 ^= ((long) data[roundedEnd + 11] & 0xff) << 24;
            case 11: k2 ^= ((long) data[roundedEnd + 10] & 0xff) << 16;
            case 10: k2 ^= ((long) data[roundedEnd + 9] & 0xff) << 8;
            case  9: k2 ^= ((long) data[roundedEnd + 8] & 0xff);
                     k2 *= C2_64;
                     k2 = Long.rotateLeft(k2, R3_64);
                     k2 *= C1_64;
                     h2 ^= k2;
            case  8: k1 ^= ((long) data[roundedEnd + 7] & 0xff) << 56;
            case  7: k1 ^= ((long) data[roundedEnd + 6] & 0xff) << 48;
            case  6: k1 ^= ((long) data[roundedEnd + 5] & 0xff) << 40;
            case  5: k1 ^= ((long) data[roundedEnd + 4] & 0xff) << 32;
            case  4: k1 ^= ((long) data[roundedEnd + 3] & 0xff) << 24;
            case  3: k1 ^= ((long) data[roundedEnd + 2] & 0xff) << 16;
            case  2: k1 ^= ((long) data[roundedEnd + 1] & 0xff) << 8;
            case  1: k1 ^= ((long) data[roundedEnd] & 0xff);
                     k1 *= C1_64;
                     k1 = Long.rotateLeft(k1, R1_64);
                     k1 *= C2_64;
                     h1 ^= k1;
        }
        
        // finalization
        h1 ^= length;
        h2 ^= length;
        
        h1 += h2;
        h2 += h1;
        
        h1 = fmix64(h1);
        h2 = fmix64(h2);
        
        h1 += h2;
        
        return h1;
    }
    
    /**
     * Вычисляет пару 64-битных хешей для double hashing.
     */
    public static long[] hash128(Object key, int seed) {
        if (key == null) {
            return new long[]{0, 0};
        }
        
        byte[] data = key.toString().getBytes();
        return hash128(data, 0, data.length, seed);
    }
    
    /**
     * Вычисляет пару 64-битных хешей для байтового массива.
     */
    public static long[] hash128(byte[] data, int offset, int length, int seed) {
        long h1 = seed & 0xFFFFFFFFL;
        long h2 = seed & 0xFFFFFFFFL;
        
        int roundedEnd = offset + (length & 0xfffffff0);
        
        for (int i = offset; i < roundedEnd; i += 16) {
            long k1 = getLong(data, i);
            long k2 = getLong(data, i + 8);
            
            k1 *= C1_64;
            k1 = Long.rotateLeft(k1, R1_64);
            k1 *= C2_64;
            h1 ^= k1;
            
            h1 = Long.rotateLeft(h1, R2_64);
            h1 += h2;
            h1 = h1 * M_64 + N1_64;
            
            k2 *= C2_64;
            k2 = Long.rotateLeft(k2, R3_64);
            k2 *= C1_64;
            h2 ^= k2;
            
            h2 = Long.rotateLeft(h2, R1_64);
            h2 += h1;
            h2 = h2 * M_64 + N2_64;
        }
        
        // tail (same as hash64)
        long k1 = 0;
        long k2 = 0;
        
        switch (length & 15) {
            case 15: k2 ^= ((long) data[roundedEnd + 14] & 0xff) << 48;
            case 14: k2 ^= ((long) data[roundedEnd + 13] & 0xff) << 40;
            case 13: k2 ^= ((long) data[roundedEnd + 12] & 0xff) << 32;
            case 12: k2 ^= ((long) data[roundedEnd + 11] & 0xff) << 24;
            case 11: k2 ^= ((long) data[roundedEnd + 10] & 0xff) << 16;
            case 10: k2 ^= ((long) data[roundedEnd + 9] & 0xff) << 8;
            case  9: k2 ^= ((long) data[roundedEnd + 8] & 0xff);
                     k2 *= C2_64;
                     k2 = Long.rotateLeft(k2, R3_64);
                     k2 *= C1_64;
                     h2 ^= k2;
            case  8: k1 ^= ((long) data[roundedEnd + 7] & 0xff) << 56;
            case  7: k1 ^= ((long) data[roundedEnd + 6] & 0xff) << 48;
            case  6: k1 ^= ((long) data[roundedEnd + 5] & 0xff) << 40;
            case  5: k1 ^= ((long) data[roundedEnd + 4] & 0xff) << 32;
            case  4: k1 ^= ((long) data[roundedEnd + 3] & 0xff) << 24;
            case  3: k1 ^= ((long) data[roundedEnd + 2] & 0xff) << 16;
            case  2: k1 ^= ((long) data[roundedEnd + 1] & 0xff) << 8;
            case  1: k1 ^= ((long) data[roundedEnd] & 0xff);
                     k1 *= C1_64;
                     k1 = Long.rotateLeft(k1, R1_64);
                     k1 *= C2_64;
                     h1 ^= k1;
        }
        
        // finalization
        h1 ^= length;
        h2 ^= length;
        
        h1 += h2;
        h2 += h1;
        
        h1 = fmix64(h1);
        h2 = fmix64(h2);
        
        h1 += h2;
        h2 += h1;
        
        return new long[]{h1, h2};
    }
    
    private static int fmix32(int h) {
        h ^= h >>> 16;
        h *= 0x85ebca6b;
        h ^= h >>> 13;
        h *= 0xc2b2ae35;
        h ^= h >>> 16;
        return h;
    }
    
    private static long fmix64(long k) {
        k ^= k >>> 33;
        k *= 0xff51afd7ed558ccdL;
        k ^= k >>> 33;
        k *= 0xc4ceb9fe1a85ec53L;
        k ^= k >>> 33;
        return k;
    }
    
    private static long getLong(byte[] data, int offset) {
        return ((long) data[offset] & 0xff) |
               (((long) data[offset + 1] & 0xff) << 8) |
               (((long) data[offset + 2] & 0xff) << 16) |
               (((long) data[offset + 3] & 0xff) << 24) |
               (((long) data[offset + 4] & 0xff) << 32) |
               (((long) data[offset + 5] & 0xff) << 40) |
               (((long) data[offset + 6] & 0xff) << 48) |
               (((long) data[offset + 7] & 0xff) << 56);
    }
}
