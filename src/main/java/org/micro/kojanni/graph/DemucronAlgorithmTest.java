package org.micro.kojanni.graph;

public class DemucronAlgorithmTest {

    public static void main(String[] args) {
        testSimpleDAG();
        testLinearGraph();
        testGraphWithCycle();
        testComplexDAG();
        testSingleVertex();
        testDisconnectedGraph();
    }

    /**
     * Простой ациклический граф
     * Граф:
     * 0 → 1
     * ↓   ↓
     * 2 → 3
     * Ожидаемые уровни:
     * Уровень 0: 0
     * Уровень 1: 1, 2
     * Уровень 2: 3
     */
    private static void testSimpleDAG() {
        System.out.println("Тест 1: Простой ациклический граф");
        System.out.println("Граф: 0→1→3, 0→2→3");

        int n = 4;
        int[][] adjacencyMatrix = {
                {1, 2, -1},  // Вершина 0 → 1, 2
                {3, -1, -1}, // Вершина 1 → 3
                {3, -1, -1}, // Вершина 2 → 3
                {-1, -1, -1} // Вершина 3 (нет исходящих рёбер)
        };

        int[][] result = new DemucronAlgorithm(adjacencyMatrix, n).execute();
        GraphAlgorithm.print(result);
        System.out.println();
    }

    /**
     * Сложный ациклический граф
     * Граф:
     * 0 → 2 → 4
     * ↓   ↓   ↓
     * 1 → 3 → 5
     * Ожидаемые уровни:
     * Уровень 0: 0
     * Уровень 1: 1, 2
     * Уровень 2: 3, 4
     * Уровень 3: 5
     */
    private static void testComplexDAG() {
        System.out.println("Тест 4: Сложный ациклический граф");
        System.out.println("Граф: 0→1,2; 1→3; 2→3,4; 3→5; 4→5");

        int n = 6;
        int[][] adjacencyMatrix = {
                {1, 2, -1},  // Вершина 0 → 1, 2
                {3, -1, -1}, // Вершина 1 → 3
                {3, 4, -1},  // Вершина 2 → 3, 4
                {5, -1, -1}, // Вершина 3 → 5
                {5, -1, -1}, // Вершина 4 → 5
                {-1, -1, -1} // Вершина 5
        };

        int[][] result = new DemucronAlgorithm(adjacencyMatrix, n).execute();
        GraphAlgorithm.print(result);
        System.out.println();
    }

    /**
     * Линейный граф
     * Граф: 0 → 1 → 2 → 3 → 4
     * Ожидаемые уровни:
     * Уровень 0: 0
     * Уровень 1: 1
     * Уровень 2: 2
     * Уровень 3: 3
     * Уровень 4: 4
     */
    private static void testLinearGraph() {
        System.out.println("Тест 2: Линейный граф");
        System.out.println("Граф: 0→1→2→3→4");

        int n = 5;
        int[][] adjacencyMatrix = {
                {1, -1, -1}, // Вершина 0 → 1
                {2, -1, -1}, // Вершина 1 → 2
                {3, -1, -1}, // Вершина 2 → 3
                {4, -1, -1}, // Вершина 3 → 4
                {-1, -1, -1} // Вершина 4 (нет исходящих рёбер)
        };

        int[][] result = new DemucronAlgorithm(adjacencyMatrix, n).execute();
        GraphAlgorithm.print(result);
        System.out.println();
    }

    /**
     * Граф с циклом
     * Граф: 0 → 1 → 2 → 0 (цикл)
     * Ожидается: null (обнаружен цикл)
     */
    private static void testGraphWithCycle() {
        System.out.println("Тест 3: Граф с циклом");
        System.out.println("Граф: 0→1→2→0 (цикл)");

        int n = 3;
        int[][] adjacencyMatrix = {
                {1, -1, -1}, // Вершина 0 → 1
                {2, -1, -1}, // Вершина 1 → 2
                {0, -1, -1}  // Вершина 2 → 0 (создаёт цикл)
        };

        int[][] result = new DemucronAlgorithm(adjacencyMatrix, n).execute();
        GraphAlgorithm.print(result);
        System.out.println();
    }

    /**
     * Граф с одной вершиной
     */
    private static void testSingleVertex() {
        System.out.println("Тест 5: Граф с одной вершиной");
        System.out.println("Граф: 0 (изолированная вершина)");

        int n = 1;
        int[][] adjacencyMatrix = {
                {-1, -1, -1} // Вершина 0 (нет исходящих рёбер)
        };

        int[][] result = new DemucronAlgorithm(adjacencyMatrix, n).execute();
        GraphAlgorithm.print(result);
        System.out.println();
    }

    /**
     * Несвязный граф
     * Граф:
     * 0 → 1
     * 2 → 3
     *
     * Ожидаемые уровни:
     * Уровень 0: 0, 2
     * Уровень 1: 1, 3
     */
    private static void testDisconnectedGraph() {
        System.out.println("Тест 6: Несвязный граф");
        System.out.println("Граф: 0→1, 2→3 (две компоненты)");

        int n = 4;
        int[][] adjacencyMatrix = {
                {1, -1, -1}, // Вершина 0 → 1
                {-1, -1, -1}, // Вершина 1 (нет исходящих рёбер)
                {3, -1, -1}, // Вершина 2 → 3
                {-1, -1, -1} // Вершина 3 (нет исходящих рёбер)
        };

        int[][] result = new DemucronAlgorithm(adjacencyMatrix, n).execute();
        GraphAlgorithm.print(result);
        System.out.println();
    }
}
