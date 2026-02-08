package org.micro.kojanni.graph.shortest_route;

import org.micro.kojanni.graph.GraphAlgorithm;
import org.micro.kojanni.graph.spanning_tree.WeightedGraph;

/**
 * - Формируется матрица смежности исходного графа.
 * Если дуга между вершинами отсутствует, то её вес выбирается бесконечно большим.
 * Диагональные элементы матрицы равны 0.
 * - Выполняется цикл V раз для каждой вершины vi
 * - Вычисляется длина пути, проходящего через vi вершину (если не образуется цикл), фиксируется наименьшее значение
 */
public class FloydWarshallAlgorithm extends GraphAlgorithm<int[][]> {

    private final WeightedGraph weightedGraph;

    public FloydWarshallAlgorithm(WeightedGraph graph) {
        super(graph);
        this.weightedGraph = graph;
    }

    public FloydWarshallAlgorithm(int[][] adjacency, int[][] weights, int vertexCount) {
        this(new WeightedGraph(adjacency, weights, vertexCount));
    }

    @Override
    public int[][] execute() {
        System.out.println(getAlgorithmName() + ":");
        
        // Формируется матрица смежности исходного графа
        int[][] distance = initializeDistanceMatrix();
        
        // Выполняется цикл V раз для каждой вершины vi
        for (int k = 0; k < vertexCount; k++) {
            for (int i = 0; i < vertexCount; i++) {
                for (int j = 0; j < vertexCount; j++) {
                    // Вычисляется длина пути, проходящего через vi вершину
                    if (distance[i][k] != Integer.MAX_VALUE && 
                        distance[k][j] != Integer.MAX_VALUE) {
                        int newDistance = distance[i][k] + distance[k][j];
                        // Фиксируется наименьшее значение
                        if (newDistance < distance[i][j]) {
                            distance[i][j] = newDistance;
                        }
                    }
                }
            }
        }
        
        return distance;
    }

    private int[][] initializeDistanceMatrix() {
        int[][] distance = new int[vertexCount][vertexCount];

        for (int i = 0; i < vertexCount; i++) {
            for (int j = 0; j < vertexCount; j++) {
                if (i == j) {
                    // Диагональные элементы матрицы равны 0
                    distance[i][j] = 0;
                } else {
                    // Если дуга между вершинами отсутствует, то её вес выбирается бесконечно большим
                    distance[i][j] = Integer.MAX_VALUE;
                }
            }
        }

        // Заполняем веса существующих рёбер
        for (int i = 0; i < vertexCount; i++) {
            int currentVertex = i;
            weightedGraph.forEachEdge(currentVertex, (adjacent, weight) -> distance[currentVertex][adjacent] = weight);
        }

        return distance;
    }
}
