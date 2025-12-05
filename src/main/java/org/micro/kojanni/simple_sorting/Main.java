package org.micro.kojanni.simple_sorting;


import org.micro.kojanni.simple_sorting.BubbleSort;

import java.util.Arrays;
import java.util.Random;

/**
 * Выполнить все пункты.
 *
 * BUB1. +1 байт. Реализовать алгоритм BubbleSort.
 * INS1. +1 байт. Реализовать алгоритм InsertionSort.
 * SHS1. +1 байт. Реализовать алгоритм ShellSort.
 *
 * +2 байта. Занести в сравнительную таблицу время сортировки случайного массива размером 100, 1000 и 10000 для каждого алгоритма.
 *
 * УРОВЕНЬ MIDDLE.
 *
 * Выполнить не менее двух пунктов и обязательно последний пункт.
 *
 * BUB2. +1 байт. Оптимизировать алгоритм BubbleSort.
 * INS2. +1 байт. Оптимизировать алгоритм InsertionSort, сделать сдвиг элементов вместо обмена.
 * INS3. +1 байт. Оптимизировать алгоритм InsertionSort, сделать бинарный поиск места вставки.
 * SHS2, SHS3. +1 байт. Оптимизировать алгоритм ShellSort, выбрать два других варианта выбора шагов.
 *
 * +1 байт. Занести в сравнительную таблицу время сортировки случайного массива размером 10^2, 10^3, 10^4, 10^5, 10^6 для каждого реализованного алгоритма (дольше двух минут можно не ждать).
 *
 * УРОВЕНЬ SENIOR.
 *
 * На выбор:
 *
 * +5 байт. Сделать визуализацию работы алгоритмов сортировки (заготовка для проектной работы).
 *
 * +5 байт. Выполнить комплексное тестирование алгоритмов сортировки.
 *
 * Выполнить прогон всех алгоритмов по всем тестам.
 * Файл с тестами приложен к заданию (563 мегабайта).
 * На первой строчке указан размер массива, на второй строчке через пробел перечислены элементы массива.
 * В файл результата записать числа из отсортированного массива в одну строчку через пробел.
 *
 * Тестировать алгоритмы следует на массивах таких размеров:
 * 1, 10, 100, 1.000, 10.000, 100.000, 1.000.000, 10.000.000 (этот по желанию)
 *
 * И с различным характером данных:
 * а) random - массив из случайных чисел
 * б) digits - массив из случайных цифр
 * в) sorted - на 99% отсортированный массив
 * г) revers - обратно-отсортированный массив
 *
 * Напишите, какие пункты вы сделали, сколько байт набрали и сколько времени ушло на каждый пункт.
 * Приложите ссылку на ваш код и на заполненную таблицу.
 * Укажите, на каком языке вы выполнили ДЗ.
 */
public class Main {
    public static void main(String[] args) {
        BubbleSort bubbleSort = new BubbleSort();
        InsertionSort insertionSort = new InsertionSort();
        ShellSort shellSort = new ShellSort();

       for (int i = 2; i < 7; i++){
           int size = (int) Math.pow(10, i);
           System.out.println("\nРазмер массива = " + size);

           int[] a1 = randomArray(size);
           int[] a2 = Arrays.copyOf(a1, a1.length);
           int[] a3 = Arrays.copyOf(a1, a1.length);
           int[] a4 = Arrays.copyOf(a1, a1.length);

           long tBubble = test(() -> bubbleSort.sort(a1));
           long tInsert = test(() -> insertionSort.sort(a2));
           long tShell = test(() -> shellSort.sort(a3));
           long tShellH = test(() -> shellSort.sortHibbard(a4));

           System.out.println("BubbleSort:    " + tBubble / 1_000_000.0 + " ms");
           System.out.println("InsertionSort: " + tInsert / 1_000_000.0 + " ms");
           System.out.println("ShellSortSedgewick:     " + tShell / 1_000_000.0 + " ms");
           System.out.println("ShellSortHibbard:     " + tShellH / 1_000_000.0 + " ms");
       }

        System.out.println("Сортировка пузырьком:");
        Test testBubbleSort = new Test(bubbleSort::processSort);
        testBubbleSort.run("src/main/resources/simple_sorting/0.random/");
        testBubbleSort.run("src/main/resources/simple_sorting/1.digits/");
        testBubbleSort.run("src/main/resources/simple_sorting/2.sorted/");
        testBubbleSort.run("src/main/resources/simple_sorting/3.revers/");

        System.out.println("Сортировка вставками:");
        Test testInsertionSort = new Test(insertionSort::processSort);
        testInsertionSort.run("src/main/resources/simple_sorting/0.random/");
        testInsertionSort.run("src/main/resources/simple_sorting/1.digits/");
        testInsertionSort.run("src/main/resources/simple_sorting/2.sorted/");
        testInsertionSort.run("src/main/resources/simple_sorting/3.revers/");

        System.out.println("Сортировка Шелла:");
        Test testShellSort = new Test(shellSort::processSort);
        testShellSort.run("src/main/resources/simple_sorting/0.random/");
        testShellSort.run("src/main/resources/simple_sorting/1.digits/");
        testShellSort.run("src/main/resources/simple_sorting/2.sorted/");
        testShellSort.run("src/main/resources/simple_sorting/3.revers/");
    }

    private static int[] randomArray(int n) {
        Random r = new Random();
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) arr[i] = r.nextInt();
        return arr;
    }

    private static long test(Runnable sort) {
        long t1 = System.nanoTime();
        sort.run();
        return System.nanoTime() - t1;
    }
}