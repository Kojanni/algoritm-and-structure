package org.micro.kojanni.algebraic_algorithms;

/**
 * На первой строчке записано вещественное число A > 0.
 * На второй строчке записано целое число N >= 0.
 *
 * Вычислить A^N. Результат вывести на экран в стандартном виде.
 *
 * Решить задачу разными способами.
 * 1. Через обычные итерации.
 * 2. Через степень двойки с домножением.
 * 3. Через двоичное разложение показателя степени.
 */
public class MainPower {
    public static void main(String[] args) {

        PowerCalculator calculator = new PowerCalculator();
        Test test1 = new Test(calculator::iterativePower);
        System.out.println("iterativePower:");
        test1.run("src/main/resources/algebraic_algorithms/3.Power/");

        Test test2 = new Test(calculator::binPower);
        System.out.println("binPower:");
        test2.run("src/main/resources/algebraic_algorithms/3.Power/");

        Test test3 = new Test(calculator::subtractPower);
        System.out.println("subtractPower:");
        test3.run("src/main/resources/algebraic_algorithms/3.Power/");

    }
}