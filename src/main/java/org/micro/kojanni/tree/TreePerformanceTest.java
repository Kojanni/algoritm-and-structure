package org.micro.kojanni.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Класс для тестирования производительности деревьев
 */
public class TreePerformanceTest {

    private static final Random random = new Random(42);

    public static void main(String[] args) {
        int[] sizes = {5000, 15000};

        for (int size : sizes) {
            runAllTests(size);
        }
    }
    public static TestResult testTree(BinarySearchTree tree, String treeName,
                                      List<Integer> insertData, String dataType) {
        TestResult result = new TestResult(treeName, dataType, insertData.size());

        try {
            long startTime = System.currentTimeMillis();
            for (int value : insertData) {
                tree.insert(value);
            }
            result.insertTime = System.currentTimeMillis() - startTime;
            result.treeHeight = tree.height();
        } catch (StackOverflowError e) {
            result.error = "StackOverflow при вставке";
            System.out.println("⚠ " + treeName + " (" + dataType + "): переполнение стека при вставке");
            return result;
        }

        try {
            int searchCount = insertData.size() / 10;
            List<Integer> searchData = new ArrayList<>();
            for (int i = 0; i < searchCount; i++) {
                searchData.add(insertData.get(random.nextInt(insertData.size())));
            }

            long startTime = System.currentTimeMillis();
            for (int value : searchData) {
                tree.search(value);
            }
            result.searchTime = System.currentTimeMillis() - startTime;
        } catch (StackOverflowError e) {
            result.error = "StackOverflow при поиске";
            System.out.println("⚠ " + treeName + " (" + dataType + "): переполнение стека при поиске");
            return result;
        }

        try {
            int removeCount = insertData.size() / 10;
            List<Integer> removeData = new ArrayList<>();
            for (int i = 0; i < removeCount; i++) {
                removeData.add(insertData.get(random.nextInt(insertData.size())));
            }

            long startTime = System.currentTimeMillis();
            for (int value : removeData) {
                tree.remove(value);
            }
            result.removeTime = System.currentTimeMillis() - startTime;
        } catch (StackOverflowError e) {
            result.error = "StackOverflow при удалении";
            System.out.println("!!! " + treeName + " (" + dataType + "): переполнение стека при удалении");
            return result;
        }

        return result;
    }

    public static TestResult testBTree(BTree tree, String treeName,
                                       List<Integer> insertData, String dataType) {
        TestResult result = new TestResult(treeName, dataType, insertData.size());

        long startTime = System.currentTimeMillis();
        for (int value : insertData) {
            tree.insert(value);
        }
        result.insertTime = System.currentTimeMillis() - startTime;
        result.treeHeight = tree.height();

        // N/10 случайных элементов
        int searchCount = insertData.size() / 10;
        List<Integer> searchData = new ArrayList<>();
        for (int i = 0; i < searchCount; i++) {
            searchData.add(insertData.get(random.nextInt(insertData.size())));
        }

        startTime = System.currentTimeMillis();
        for (int value : searchData) {
            tree.search(value);
        }
        result.searchTime = System.currentTimeMillis() - startTime;

        // N/10 случайных элементов
        int removeCount = insertData.size() / 10;
        List<Integer> removeData = new ArrayList<>();
        for (int i = 0; i < removeCount; i++) {
            removeData.add(insertData.get(random.nextInt(insertData.size())));
        }

        // удаление
        startTime = System.currentTimeMillis();
        for (int value : removeData) {
            tree.remove(value);
        }
        result.removeTime = System.currentTimeMillis() - startTime;

        return result;
    }

    public static void runAllTests(int n) {
        System.out.println("Размер выборки: N = " + n);
        System.out.println("Количество операций поиска и удаления: N/10 = " + (n / 10));
        System.out.println(String.format("%-25s | %-15s | %10s | %14s | %14s | %14s | %s",
                "Тип дерева", "Тип данных", "Размер", "Вставка", "Поиск", "Удаление", "Характеристики"));
        System.out.println("-".repeat(120));

        List<TestResult> results = new ArrayList<>();

        List<Integer> randomData = generateRandomData(n);
        List<Integer> sortedData = generateSortedData(n);

        // Базовые деревья
        results.add(testTree(new BinarySearchTree(), "BST", randomData, "Случайные"));
        results.add(testTree(new BinarySearchTree(), "BST", sortedData, "Упорядоченные"));

        results.add(testTree(new AVLTree(), "AVL", randomData, "Случайные"));
        results.add(testTree(new AVLTree(), "AVL", sortedData, "Упорядоченные"));

        results.add(testTree(new TreapTree(), "Treap", randomData, "Случайные"));
        results.add(testTree(new TreapTree(), "Treap", sortedData, "Упорядоченные"));

        // Опциональное задание 1: Расширяющееся деревья
        results.add(testTree(new SplayTree(), "Splay", randomData, "Случайные"));
        results.add(testTree(new SplayTree(), "Splay", sortedData, "Упорядоченные"));

        // B-деревья с разными степенями
        results.add(testBTree(new BTree(3), "B-дерево (t=3)", randomData, "Случайные"));
        results.add(testBTree(new BTree(3), "B-дерево (t=3)", sortedData, "Упорядоченные"));
        
        results.add(testBTree(new BTree(5), "B-дерево (t=5)", randomData, "Случайные"));
        results.add(testBTree(new BTree(5), "B-дерево (t=5)", sortedData, "Упорядоченные"));
        
        results.add(testBTree(new BTree(10), "B-дерево (t=10)", randomData, "Случайные"));
        results.add(testBTree(new BTree(10), "B-дерево (t=10)", sortedData, "Упорядоченные"));

        for (TestResult result : results) {
            System.out.println(result);
        }
    }

    private static List<Integer> generateRandomData(int n) {
        List<Integer> data = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            data.add(random.nextInt(n * 10));
        }
        return data;
    }

    private static List<Integer> generateSortedData(int n) {
        List<Integer> data = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            data.add(i);
        }
        return data;
    }

    public static class TestResult {
        String treeName;
        String dataType;
        int size;
        long insertTime;
        long searchTime;
        long removeTime;
        int treeHeight;
        String error;

        public TestResult(String treeName, String dataType, int size) {
            this.treeName = treeName;
            this.dataType = dataType;
            this.size = size;
        }

        @Override
        public String toString() {
            if (error != null) {
                return String.format("%-25s | %-15s | %10d | ОШИБКА: %s (дерево слишком глубокое для рекурсии)",
                        treeName, dataType, size, error);
            }
            return String.format("%-25s | %-15s | %10d | %10d мс | %10d мс | %10d мс | Высота: %d",
                    treeName, dataType, size, insertTime, searchTime, removeTime, treeHeight);
        }
    }

}

