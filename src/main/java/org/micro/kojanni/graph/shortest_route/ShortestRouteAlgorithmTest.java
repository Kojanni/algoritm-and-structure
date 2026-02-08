package org.micro.kojanni.graph.shortest_route;

public class ShortestRouteAlgorithmTest {

    public static void main(String[] args) {
        testSimpleGraph();
        testComplexGraph();
        testLinearGraph();
        testTriangleGraph();
        testDisconnectedGraph();
    }

    /**
     * Простой граф с 4 вершинами
     * Схема:
     * 0 -(4)- 1
     * |     / |
     * (3) (1)  (2)
     * | /     |
     * 2 -(4)- 3
     * <p>
     * Кратчайшие пути от вершины 0:
     * - 0 -> 0: 0
     * - 0 -> 1: 4
     * - 0 -> 2: 3
     * - 0 -> 3: 6
     */
    private static void testSimpleGraph() {
        System.out.println("Простой граф (4 вершины)");
        System.out.println("Граф: 0-1(4), 0-2(3), 1-2(1), 1-3(2), 2-3(4)");
        System.out.println();

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

        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(adjacency, weights, n, 0);
        dijkstra.execute();
        printFullPaths(dijkstra, 0);

        FloydWarshallAlgorithm floyd = new FloydWarshallAlgorithm(adjacency, weights, n);
        int[][] distances = floyd.execute();
        printDistanceMatrix(distances);
        System.out.println();
    }

    /**
     * Сложный граф с 5 вершинами
     * Схема:
     * 0 -(2)- 1
     * |     / |
     * (3) (8)  (5)
     * | /     |
     * 2 -(7)- 3
     * \      |
     * (9)  (6)
     * \  |
     * 4
     * <p>
     * Кратчайшие пути от вершины 0:
     * - 0 -> 1: 2
     * - 0 -> 2: 3
     * - 0 -> 3: 7 (через 1)
     * - 0 -> 4: 12 (через 2)
     */
    private static void testComplexGraph() {
        System.out.println("Сложный граф (5 вершин)");
        System.out.println("Граф: 0-1(2), 0-2(3), 1-2(8), 1-3(5), 2-3(7), 2-4(9), 3-4(6)");
        System.out.println();

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

        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(adjacency, weights, n, 0);
        dijkstra.execute();
        printFullPaths(dijkstra, 0);

        FloydWarshallAlgorithm floyd = new FloydWarshallAlgorithm(adjacency, weights, n);
        int[][] distances = floyd.execute();
        printDistanceMatrix(distances);
        System.out.println();
    }

    /**
     * Линейный граф - цепочка вершин
     * Схема:
     *     0 -(1)- 1 -(2)- 2 -(3)- 3 -(4)- 4
     * 
     * Кратчайшие пути от вершины 0:
     * - 0 -> 1: 1
     * - 0 -> 2: 3
     * - 0 -> 3: 6
     * - 0 -> 4: 10
     */
    private static void testLinearGraph() {
        System.out.println("Линейный граф (цепочка из 5 вершин)");
        System.out.println("Граф: 0-1(1), 1-2(2), 2-3(3), 3-4(4)");
        System.out.println();

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

        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(adjacency, weights, n, 0);
        dijkstra.execute();
        printFullPaths(dijkstra, 0);

        FloydWarshallAlgorithm floyd = new FloydWarshallAlgorithm(adjacency, weights, n);
        int[][] distances = floyd.execute();
        printDistanceMatrix(distances);
        System.out.println();
    }

    /**
     * Треугольный граф с разными весами
     * Схема:
     *        0
     *       / \
     *     (1) (10)
     *     /     \
     *    1 -(2)- 2
     * 
     * Кратчайшие пути от вершины 0:
     * - 0 -> 1: 1
     * - 0 -> 2: 3 (через 1, а не напрямую)
     */
    private static void testTriangleGraph() {
        System.out.println("Треугольный граф");
        System.out.println("Граф: 0-1(1), 0-2(10), 1-2(2)");
        System.out.println();

        int n = 3;
        int[][] adjacency = {
                {1, 2, -1},
                {0, 2, -1},
                {0, 1, -1}
        };
        int[][] weights = {
                {1, 10, -1},
                {1, 2, -1},
                {10, 2, -1}
        };

        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(adjacency, weights, n, 0);
        dijkstra.execute();
        printFullPaths(dijkstra, 0);

        FloydWarshallAlgorithm floyd = new FloydWarshallAlgorithm(adjacency, weights, n);
        int[][] distances = floyd.execute();
        printDistanceMatrix(distances);
        System.out.println();
    }

    /**
     * Несвязный граф - две отдельные компоненты
     * Схема:
     *     0 -(1)- 1        2 -(2)- 3
     *    (компонента 1)   (компонента 2)
     * 
     * Ожидаемый результат: null (граф несвязный, невозможно достичь всех вершин)
     */
    private static void testDisconnectedGraph() {
        System.out.println("Несвязный граф");
        System.out.println("Граф: 0-1(1), 2-3(2) (две компоненты)");
        System.out.println();

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

        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(adjacency, weights, n, 0);
        dijkstra.execute();
        printFullPaths(dijkstra, 0);

        FloydWarshallAlgorithm floyd = new FloydWarshallAlgorithm(adjacency, weights, n);
        int[][] distances = floyd.execute();
        printDistanceMatrix(distances);
        System.out.println("(∞ означает, что путь не существует)");
        System.out.println();
    }

    private static void printFullPaths(DijkstraAlgorithm dijkstra, int start) {
        int[] distances = dijkstra.getDistances();
        int[] parents = dijkstra.getParents();
        
        for (int v = 0; v < distances.length; v++) {
            if (distances[v] == Integer.MAX_VALUE) {
                System.out.println("  " + start + " -> " + v + ": недостижима");
            } else {
                System.out.print("  " + start + " -> " + v + ": ");
                printPath(parents, start, v);
                System.out.println(" (расстояние: " + distances[v] + ")");
            }
        }
    }

    private static void printPath(int[] parents, int start, int end) {
        if (end == start) {
            System.out.print(start);
            return;
        }

        // Восстанавливаем путь от конца к началу
        java.util.List<Integer> path = new java.util.ArrayList<>();
        int current = end;
        while (current != -1) {
            path.add(current);
            current = parents[current];
        }

        // Выводим путь от начала к концу
        for (int i = path.size() - 1; i >= 0; i--) {
            System.out.print(path.get(i));
            if (i > 0) {
                System.out.print(" -> ");
            }
        }
    }

    private static void printDistanceMatrix(int[][] distances) {
        int n = distances.length;
        
        System.out.print("     ");
        for (int j = 0; j < n; j++) {
            System.out.printf("%5d", j);
        }
        System.out.println();
        
        for (int i = 0; i < n; i++) {
            System.out.printf("%5d", i);
            for (int j = 0; j < n; j++) {
                if (distances[i][j] == Integer.MAX_VALUE) {
                    System.out.print("    ∞");
                } else {
                    System.out.printf("%5d", distances[i][j]);
                }
            }
            System.out.println();
        }
    }
}
