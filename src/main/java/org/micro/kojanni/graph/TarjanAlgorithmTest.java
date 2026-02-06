package org.micro.kojanni.graph;

public class TarjanAlgorithmTest {

    public static void main(String[] args) {
        testSimpleSCC();
        testMultipleSCCs();
        testSingleVertex();
        testComplexGraph();
        testLinearGraph();
    }

    /**
     * Простой граф с одной сильно связной компонентой
     * <p>
     * Граф: 0 → 1 → 2 → 0 (цикл)
     * <p>
     * Ожидается: 1 компонента {0, 1, 2}
     */
    private static void testSimpleSCC() {
        System.out.println("Простой граф с циклом");
        System.out.println("Граф: 0→1→2→0");

        int n = 3;
        int[][] adjacencyMatrix = {
                {1, -1, -1}, // 0 → 1
                {2, -1, -1}, // 1 → 2
                {0, -1, -1}  // 2 → 0
        };

        int[][] sccs = new TarjanAlgorithm(adjacencyMatrix, n).execute();
        GraphAlgorithm.print(sccs);
        System.out.println();
    }

    /**
     * Граф с несколькими сильно связными компонентами
     * <p>
     * Граф:
     * 0 → 1 → 2 → 0  (первая компонента)
     * 2 → 3 → 4 → 3  (вторая компонента)
     * <p>
     * Ожидается: 2 компоненты
     */
    private static void testMultipleSCCs() {
        System.out.println("Граф с несколькими компонентами");
        System.out.println("Граф: 0→1→2→0, 2→3→4→3");

        int n = 5;
        int[][] adjacencyMatrix = {
                {1, -1, -1},    // 0 → 1
                {2, -1, -1},    // 1 → 2
                {0, 3, -1},     // 2 → 0, 3
                {4, -1, -1},    // 3 → 4
                {3, -1, -1}     // 4 → 3
        };

        int[][] sccs = new TarjanAlgorithm(adjacencyMatrix, n).execute();
        GraphAlgorithm.print(sccs);
        System.out.println();
    }

    /**
     * Граф с одной вершиной
     */
    private static void testSingleVertex() {
        System.out.println("Граф с одной вершиной");
        System.out.println("Граф: 0 (изолированная вершина)");

        int n = 1;
        int[][] adjacencyMatrix = {
                {-1, -1, -1} // 0
        };

        int[][] sccs = new TarjanAlgorithm(adjacencyMatrix, n).execute();
        GraphAlgorithm.print(sccs);
        System.out.println();
    }

    /**
     * Сложный граф
     * <p>
     * Граф:
     * 0 → 1 → 2
     * ↑   ↓   ↓
     * 5 ← 4 ← 3
     * ↓
     * 6 → 7
     * <p>
     * Компоненты:
     * - {0, 1, 2, 3, 4, 5} - большой цикл
     * - {6}
     * - {7}
     */
    private static void testComplexGraph() {
        System.out.println("Сложный граф");
        System.out.println("Граф: 0→1→2→3→4→5→0, 5→6→7");

        int n = 8;
        int[][] adjacencyMatrix = {
                {1, -1, -1},    // 0 → 1
                {2, 4, -1},     // 1 → 2, 4
                {3, -1, -1},    // 2 → 3
                {4, -1, -1},    // 3 → 4
                {5, -1, -1},    // 4 → 5
                {0, 6, -1},     // 5 → 0, 6
                {7, -1, -1},    // 6 → 7
                {-1, -1, -1}    // 7
        };

        int[][] sccs = new TarjanAlgorithm(adjacencyMatrix, n).execute();
        GraphAlgorithm.print(sccs);
        System.out.println();
    }

    /**
     * Линейный граф
     * <p>
     * Граф: 0 → 1 → 2 → 3
     * <p>
     * Ожидается: 4 компоненты (каждая вершина - отдельная компонента)
     */
    private static void testLinearGraph() {
        System.out.println("Линейный граф");
        System.out.println("Граф: 0→1→2→3");

        int n = 4;
        int[][] adjacencyMatrix = {
                {1, -1, -1},    // 0 → 1
                {2, -1, -1},    // 1 → 2
                {3, -1, -1},    // 2 → 3
                {-1, -1, -1}    // 3
        };

        int[][] sccs = new TarjanAlgorithm(adjacencyMatrix, n).execute();
        GraphAlgorithm.print(sccs);
        System.out.println();
    }
}
