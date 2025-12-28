package org.micro.kojanni.linear_sorting;

/**
 * УРОВЕНЬ JUNIOR.
 * <p>
 * Выполнить все пункты.
 * <p>
 * CS1. +1 байт. Реализовать алгоритм сортировки подсчётом CountingSort.30
 * RS1. +1 байт. Реализовать алгоритм поразрядкнй сортировки RadixSort.30
 * BS1. +1 байт. Реализовать алгоритм блочной сортировки BucketSort.40
 * +2 байта. Занести в сравнительную таблицу время сортировки 25
 * случайного массива размером 10^2, 10^3, 10^4, 10^5, 10^6 из чисел от 0 до 999.
 * для каждого реализованного алгоритма, timeout 2 минуты.
 * +1 байт. Выполнить тест (прикреплён в комментариях).15
 * <p>
 * УРОВЕНЬ MIDDLE.
 * <p>
 * +2 байта. Сгенерировать бинарный файл, который содержит N=10^9 миллиард целых, 16-битных чисел (от 0 до 65535), по 2 байта на каждое число.
 * (возможен упрощённый вариант задания, N=10^8 или 10^7).
 * <p>
 * Отсортировать числа, внести в таблицу время сортировки:
 * +1 байт. Отсортировать массив алгоритмом CountingSort
 * +1 байт. Отсортировать массив алгоритмом RadixSort
 * +1 байт. Отсортировать массив алгоритмом BucketSort
 * <p>
 * Написать, сколько ушло времени на выполнение каждого пункта и всего.
 * Опубликовать ссылку на репозиторий, таблицу сравнения и вывод.
 * <p>
 * УРОВЕНЬ SENIOR.
 * <p>
 * Проектная работа -
 * Создать программу для визуализации алгоритмов сортировки.
 */
public class Main {

    public static void main(String[] args) {
        CountingSort countingSort = new CountingSort();
        RadixSort radixSort = new RadixSort();
        BucketSort bucketSort = new BucketSort();

//
//        int[] sizes = {100, 1000, 10000, 100000, 1000000};
//        Sorting[] sortings = {countingSort, radixSort, bucketSort};
//
//        TestUtils.benchmark(sizes, sortings);

        int n = 10_000_000;
        String filename = "src/main/java/org/micro/kojanni/linear_sorting/numbers.bin";
//        generateBinaryFile(filename, n);

        System.out.println("Сортировка подсчётом:");
        Test testCountingSort = new Test(countingSort::processSort);
        testCountingSort.runBinary("src/main/java/org/micro/kojanni/linear_sorting/numbers.bin", countingSort::sort);

        System.out.println("Поразрядная сортировка:");
        Test testRadixSort = new Test(radixSort::processSort);
        testRadixSort.runBinary("src/main/java/org/micro/kojanni/linear_sorting/numbers.bin", radixSort::sort);

        System.out.println("Блочная сортировка:");
        Test testBucketSort = new Test(bucketSort::processSort);
        testBucketSort.runBinary("src/main/java/org/micro/kojanni/linear_sorting/numbers.bin", bucketSort::sort);
    }
}