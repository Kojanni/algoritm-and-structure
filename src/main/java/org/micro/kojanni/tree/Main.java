package org.micro.kojanni.tree;

import java.util.Scanner;

/**
 * Главный класс для запуска демонстраций и тестов
 */
public class Main {
    
    public static void main(String[] args) {
        System.out.println("Выберите режим работы:");
        System.out.println("1 - Демонстрация базовой работы деревьев");
        System.out.println("2 - Тестирование производительности");
        System.out.println("3 - Простой тест с выбранным размером");
        System.out.print("\nВаш выбор: ");
        
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        
        System.out.println();
        
        switch (choice) {
            case 1:
                System.out.println("Демонстрация базовой работы деревьев:\n");
                TreeDemo.main(args);
                break;
                
            case 2:
                System.out.println("Тестирование производительности:");
                TreePerformanceTest.main(args);
                break;
                
            case 3:
                System.out.print("Введите размер выборки (рекомендуется <= 20000): ");
                int size = scanner.nextInt();
                System.out.println();
                TreePerformanceTest.runAllTests(size);
                break;

            default:
               break;
        }
        
        scanner.close();
    }
}
