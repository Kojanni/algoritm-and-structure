package org.micro.kojanni.pyramid_sorting;

import java.util.Arrays;
import java.util.Random;

/**
 * УРОВЕНЬ JUNIOR.
 *
 * Выполнить все пункты.
 *
 * SEL1. +1 байт. Реализовать алгоритм SelectionSort.
 * HIP1. +1 байт. Реализовать алгоритм HeapSort.
 *
 * УРОВЕНЬ MIDDLE.
 *
 * +3 байта. Занести в сравнительную таблицу время сортировки случайного массива размером 10^2, 10^3, 10^4, 10^5, 10^6 для каждого реализованного алгоритма (дольше двух минут можно не ждать).
 *
 * УРОВЕНЬ SENIOR.
 *
 * На выбор:
 *
 * +5 байт. Сделать визуализацию работы алгоритмов сортировки (заготовка для проектной работы).
 * +5 байт. Выполнить комплексное тестирование алгоритмов сортировки.
 *
 * Выполнить прогон всех алгоритмов по всем тестам.
 * Файл с тестами приложен к заданию (563 мегабайта).
 * На первой строчке указан размер массива, на второй строчке через пробел перечислены элементы массива.
 * В файл результата записать числа из отсортированного массива в одну строчку через пробел.
 *
 * Тестировать алгоритмы следует на массивах таких размеров:
 * 1, 10, 100, 1.000, 10.000, 100.000, 1.000.000, 10.000.000 (по желанию)
 *
 * И с различным характером данных:
 * а) random - массив из случайных чисел
 * б) digits - массив из случайных цифр
 * в) sorted - на 99% отсортированный массив
 * г) revers - обратно-отсортированный массив
 *
 * Напишите, какие пункты вы сделали, сколько байт набрали и сколько времени ушло на каждый пункт.
 * Приложите ссылку на ваш код и на заполненную таблицу.
 *
 * Укажите, на каком языке вы выполнили ДЗ.
 */
public class Main {

    public static void main(String[] args) {
        SelectionSort selectionSort = new SelectionSort();
        HeapSort heapSort = new HeapSort();

       for (int i = 2; i < 7; i++){
           int size = (int) Math.pow(10, i);
           System.out.println("\nРазмер массива = " + size);

           int[] a1 = randomArray(size);
           int[] a2 = Arrays.copyOf(a1, a1.length);

           long tSelect = test(() -> selectionSort.sort(a1));
           long tHeap = test(() -> heapSort.sort(a2));

           System.out.println("SelectionSort:    " + tSelect / 1_000_000.0 + " ms");
           System.out.println("HeapSort: " + tHeap / 1_000_000.0 + " ms");
       }

        System.out.println("Сортировка выбором:");
        Test testBubbleSort = new Test(selectionSort::processSort);
        testBubbleSort.run("src/main/resources/sorting/0.random/");
        testBubbleSort.run("src/main/resources/sorting/1.digits/");
        testBubbleSort.run("src/main/resources/sorting/2.sorted/");
        testBubbleSort.run("src/main/resources/sorting/3.revers/");

        System.out.println("Сортировка пирамидальная:");
        Test testInsertionSort = new Test(heapSort::processSort);
        testInsertionSort.run("src/main/resources/sorting/0.random/");
        testInsertionSort.run("src/main/resources/sorting/1.digits/");
        testInsertionSort.run("src/main/resources/sorting/2.sorted/");
        testInsertionSort.run("src/main/resources/sorting/3.revers/");
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