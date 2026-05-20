package org.micro.kojanni.dynamic_programming;

import org.micro.kojanni.utils.Test;
import java.util.Stack;

/**
 * Этап 3. Ширина сарая.
 * Для каждого элемента массива A находит границы L и R:
 * - L[j] - индекс самого левого элемента >= A[j]
 * - R[j] - индекс самого правого элемента >= A[j]
 * 
 * Вход:
 * - Первая строка: N (размер массива, от 1 до 10000)
 * - Следующие N строк: элементы массива A (от 0 до 10000)
 * 
 * Выход:
 * - Две строки: массивы L и R, элементы разделены пробелами
 */
public class Stage3BarnWidth {

    public static String run(String[] input) {
        int n = Integer.parseInt(input[0].trim());
        int[] a = new int[n];
        
        for (int i = 0; i < n; i++) {
            a[i] = Integer.parseInt(input[i + 1].trim());
        }
        
        int[] left = calculateLeft(a, n);
        int[] right = calculateRight(a, n);
        
        StringBuilder result = new StringBuilder();
        
        // Массив L
        for (int i = 0; i < n; i++) {
            if (i > 0) {
                result.append(" ");
            }
            result.append(left[i]);
        }
        result.append("\n");
        
        // Массив R
        for (int i = 0; i < n; i++) {
            if (i > 0) {
                result.append(" ");
            }
            result.append(right[i]);
        }
        
        return result.toString();
    }
    
    /**
     * Вычисляет для каждого элемента индекс самого левого элемента >= A[j].
     * Использует стек для эффективного поиска за O(n).
     */
    private static int[] calculateLeft(int[] a, int n) {
        int[] left = new int[n];
        Stack<Integer> stack = new Stack<>();
        
        for (int i = 0; i < n; i++) {
            // Удаляем из стека все элементы меньше текущего
            while (!stack.isEmpty() && a[stack.peek()] >= a[i]) {
                stack.pop();
            }
            
            // Если стек пуст, то слева нет элементов меньше текущего
            if (stack.isEmpty()) {
                left[i] = 0;
            } else {
                // Иначе левая граница - следующий элемент после вершины стека
                left[i] = stack.peek() + 1;
            }
            
            stack.push(i);
        }
        
        return left;
    }
    
    /**
     * Вычисляет для каждого элемента индекс самого правого элемента >= A[j].
     * Использует стек для эффективного поиска за O(n).
     */
    private static int[] calculateRight(int[] a, int n) {
        int[] right = new int[n];
        Stack<Integer> stack = new Stack<>();
        
        for (int i = n - 1; i >= 0; i--) {
            // Удаляем из стека все элементы меньше текущего
            while (!stack.isEmpty() && a[stack.peek()] >= a[i]) {
                stack.pop();
            }
            
            // Если стек пуст, то справа нет элементов меньше текущего
            if (stack.isEmpty()) {
                right[i] = n - 1;
            } else {
                // Иначе правая граница - предыдущий элемент перед вершиной стека
                right[i] = stack.peek() - 1;
            }
            
            stack.push(i);
        }
        
        return right;
    }

    public static void main(String[] args) {
        Test test = new Test(Stage3BarnWidth::run);
        test.run("src/test/java/org/micro/kojanni/dynamic_programming/Ширина_сарая/");
    }
}
