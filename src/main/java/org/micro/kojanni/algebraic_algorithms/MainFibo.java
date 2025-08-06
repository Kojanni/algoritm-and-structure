package org.micro.kojanni.algebraic_algorithms;

/**
 На первой строчке записано целое число N >= 0.
 Найти и вывести на экран точное значение N-ого числа Фибоначчи.

 Решить задачу разными способами.
 1. Через рекурсию.
 2. Через итерацию.
 3. Через формулу золотого сечения.
 4. Через возведение матрицы в степень.
 */
public class MainFibo {
    public static void main(String[] args) {

        FibonacciNumberCalculator calculator = new FibonacciNumberCalculator();
        Test test1 = new Test(calculator::recursive);
        System.out.println("Через рекурсию:");
        test1.run("src/main/resources/algebraic_algorithms/4.Fibo/");

        Test test2 = new Test(calculator::iterative);
        System.out.println("Через итерацию:");
        test2.run("src/main/resources/algebraic_algorithms/4.Fibo/");

        Test test3 = new Test(calculator::goldenSection);
        System.out.println("Через формулу золотого сечения:");
        test3.run("src/main/resources/algebraic_algorithms/4.Fibo/");

        MatrixFibonacciNumberCalculator calculator4 = new MatrixFibonacciNumberCalculator();
        Test test4 = new Test(calculator4::fibonacci);
        System.out.println("Через возведение матрицы в степень:");
        test4.run("src/main/resources/algebraic_algorithms/4.Fibo/");

    }
}