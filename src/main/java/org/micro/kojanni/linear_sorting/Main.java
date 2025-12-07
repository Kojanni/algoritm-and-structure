package org.micro.kojanni.linear_sorting;

import java.util.Arrays;
import java.util.Random;

/**
 * УРОВЕНЬ JUNIOR.
 *
 * Выполнить все пункты.
 *
 * QS1. +1 байт. Реализовать алгоритм быстрой сортировки QuickSort.
 * MS2. +1 байт. Реализовать алгоритм сортировки слиянием MergeSort.
 * +2 байта. Занести в сравнительную таблицу время сортировки
 * случайного массива размером 10^2, 10^3, 10^4, 10^5, 10^6
 * для каждого реализованного алгоритма, timeout 2 минуты.
 *
 * УРОВЕНЬ MIDDLE.
 *
 * +1 байт. Написать функцию (N, T) для генерации текстового файла из N строчек, на каждой строке записано случайное число от 1 до T.
 * ES1. +1 байта. Реализовать алгоритм внешней сортировки ExternalSort первым способом, создание T разных файлов с последующим слиянием.
 * ES2. +1 байта. Реализовать алгоритм внешней сортировки ExternalSort вторым способом, с двумя вспомогательными файлами.
 * ES3. +1 байта. Реализовать алгоритм внешней сортировки ExternalSort третьим способом, при первом проходе в память загружать блоки по 100 чисел, сортировать их любым другим алгоритмом и отправлять на выход, а потом действовать по алгоритму ES2.
 * +2 байта. Занести в сравнительную таблицу время сортировки файлов при:
 * N = 10^2, 10^3, 10^4, 10^5, 10^6,
 * T = 10, N. (всего 10 вариантов сочетания N и T).
 *
 * УРОВЕНЬ SENIOR.
 *
 * 5 байт.
 * Выполнить комплексное тестирование алгоритмов сортировки.
 * Выполнить прогон всех алгоритмов по всем тестам, приложенными к заданию (563 мегабайта).
 * Алгоритм внешней сортировки в этом пункте тестировать не нужно.
 *
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
        QuickSort quickSort = new QuickSort();
        MergeSort mergeSort = new MergeSort();

       for (int i = 2; i < 7; i++){
           int size = (int) Math.pow(10, i);
           System.out.println("\nРазмер массива = " + size);

           int[] a1 = randomArray(size);
           int[] a2 = Arrays.copyOf(a1, a1.length);

           long tQSort = test(() -> quickSort.sort(a1));
           long tMSort = test(() -> mergeSort.sort(a2));

           System.out.println("qSort:    " + tQSort / 1_000_000.0 + " ms");
           System.out.println("mSort: " + tMSort / 1_000_000.0 + " ms");
       }

        System.out.println("Сортировка быстрая:");
        Test testQSort = new Test(quickSort::processSort);
        testQSort.run("src/main/resources/sorting/0.random/");
        testQSort.run("src/main/resources/sorting/1.digits/");
        testQSort.run("src/main/resources/sorting/2.sorted/");
        testQSort.run("src/main/resources/sorting/3.revers/");

        System.out.println("Сортировка слиянием:");
        Test testMergeSort = new Test(mergeSort::processSort);
        testMergeSort.run("src/main/resources/sorting/0.random/");
        testMergeSort.run("src/main/resources/sorting/1.digits/");
        testMergeSort.run("src/main/resources/sorting/2.sorted/");
        testMergeSort.run("src/main/resources/sorting/3.revers/");
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