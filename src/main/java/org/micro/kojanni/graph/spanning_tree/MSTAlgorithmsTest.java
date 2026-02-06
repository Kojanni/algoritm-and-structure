package org.micro.kojanni.graph.spanning_tree;

/**
 * - Краскал: сортирует рёбра по весу и добавляет их, избегая циклов (Union-Find)
 * - Прима: жадно растит дерево от начальной вершины (приоритетная очередь)
 * - Борувка: параллельно добавляет минимальные рёбра для каждой компоненты связности
 */
public class MSTAlgorithmsTest {

    public static void main(String[] args) {
        testSimpleGraph();
        testComplexGraph();
        testLinearGraph();
        testSingleEdgeGraph();
        testDisconnectedGraph();
    }

    /**
     * Простой граф с 4 вершинами.
     * Схема:
     *        0 -(4)- 1
     *        |     / |
     *       (3) (1)  (2)
     *        | /     |
     *        2 -(4)- 3
     * 
     * Рёбра: 0-1(4), 0-2(3), 1-2(1), 1-3(2), 2-3(4)
     * Ожидаемый MST: 1-2(1), 1-3(2), 0-2(3), общий вес = 6
     */
    private static void testSimpleGraph() {
        System.out.println("Тест 1: Простой граф");
        System.out.println("Граф: 0-1(4), 0-2(3), 1-2(1), 1-3(2), 2-3(4)");

        int n = 4;
        int[][] adjacency = {
                {1, 2, -1},
                {0, 2, 3, -1},
                {0, 1, 3, -1},
                {1, 2, -1}
        };
        int[][] weights = {
                {4, 3, -1},
                {4, 1, 2, -1},
                {3, 1, 4, -1},
                {2, 4, -1}
        };

        runAllAlgorithms(adjacency, weights, n);
        System.out.println();
    }

    /**
     * Сложный граф с 5 вершинами и множественными рёбрами.
     * Схема:
     *        0 -(2)- 1
     *        |     / |
     *       (3) (8)  (5)
     *        | /     |
     *        2 -(7)- 3
     *         \      |
     *          (9)  (6)
     *             \  |
     *               4
     * Рёбра: 0-1(2), 0-2(3), 1-2(8), 1-3(5), 2-3(7), 2-4(9), 3-4(6)
     * Ожидаемый MST: 0-1(2), 0-2(3), 1-3(5), 3-4(6), общий вес = 16
     */
    private static void testComplexGraph() {
        System.out.println("Тест 2: Сложный граф");
        System.out.println("Граф: 5 вершин с множественными рёбрами");

        int n = 5;
        int[][] adjacency = {
                {1, 2, -1},
                {0, 2, 3, -1},
                {0, 1, 3, 4, -1},
                {1, 2, 4, -1},
                {2, 3, -1}
        };
        int[][] weights = {
                {2, 3, -1},
                {2, 8, 5, -1},
                {3, 8, 7, 9, -1},
                {5, 7, 6, -1},
                {9, 6, -1}
        };

        runAllAlgorithms(adjacency, weights, n);
        System.out.println();
    }

    /**
     * Линейный граф - цепочка вершин.
     * Схема:
     *     0 -(1)- 1 -(2)- 2 -(3)- 3 -(4)- 4
     * 
     * Рёбра: 0-1(1), 1-2(2), 2-3(3), 3-4(4)
     * Ожидаемый MST: все рёбра (уже дерево), общий вес = 10
     */
    private static void testLinearGraph() {
        System.out.println("Тест 3: Линейный граф");
        System.out.println("Граф: 0-1(1), 1-2(2), 2-3(3), 3-4(4)");

        int n = 5;
        int[][] adjacency = {
                {1, -1},
                {0, 2, -1},
                {1, 3, -1},
                {2, 4, -1},
                {3, -1}
        };
        int[][] weights = {
                {1, -1},
                {1, 2, -1},
                {2, 3, -1},
                {3, 4, -1},
                {4, -1}
        };

        runAllAlgorithms(adjacency, weights, n);
        System.out.println();
    }

    /**
     * Минимальный граф с одним ребром.
     * Схема:
     *     0 ----(5)---- 1
     * 
     * Рёбра: 0-1(5)
     * Ожидаемый MST: 0-1(5), общий вес = 5
     */
    private static void testSingleEdgeGraph() {
        System.out.println("Тест 4: Граф с одним ребром");
        System.out.println("Граф: 0-1(5)");

        int n = 2;
        int[][] adjacency = {
                {1, -1},
                {0, -1}
        };
        int[][] weights = {
                {5, -1},
                {5, -1}
        };

        runAllAlgorithms(adjacency, weights, n);
        System.out.println();
    }

    /**
     * Несвязный граф - две отдельные компоненты.
     * Схема:
     *     0 -(1)- 1        2 -(2)- 3
     *    (компонента 1)   (компонента 2)
     * 
     * Рёбра: 0-1(1), 2-3(2)
     * Ожидаемый результат: null (граф несвязный, MST не существует)
     */
    private static void testDisconnectedGraph() {
        System.out.println("Тест 5: Несвязный граф");
        System.out.println("Граф: 0-1(1), 2-3(2) (две компоненты)");

        int n = 4;
        int[][] adjacency = {
                {1, -1},
                {0, -1},
                {3, -1},
                {2, -1}
        };
        int[][] weights = {
                {1, -1},
                {1, -1},
                {2, -1},
                {2, -1}
        };

        runAllAlgorithms(adjacency, weights, n);
        System.out.println();
    }

    private static void runAllAlgorithms(int[][] adjacency, int[][] weights, int n) {
        System.out.println("Алгоритм Краскала");
        Edge[] kruskalResult = new KruskalAlgorithm(adjacency, weights, n).execute();
        printMST(kruskalResult);

        System.out.println("Алгоритм Прима");
        Edge[] primResult = new PrimAlgorithm(adjacency, weights, n).execute();
        printMST(primResult);

        System.out.println("Алгоритм Борувки");
        Edge[] boruvkaResult = new BoruvkaAlgorithm(adjacency, weights, n).execute();
        printMST(boruvkaResult);
    }

    private static void printMST(Edge[] mst) {
        if (mst == null) {
            System.out.println("MST не найден (граф несвязный)");
            return;
        }

        int totalWeight = 0;
        System.out.print("Рёбра MST: ");
        for (int i = 0; i < mst.length; i++) {
            if (i > 0) System.out.print(", ");
            System.out.print(mst[i]);
            totalWeight += mst[i].weight;
        }
        System.out.println("\nОбщий вес MST: " + totalWeight);
    }
}
