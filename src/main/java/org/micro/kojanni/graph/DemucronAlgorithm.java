package org.micro.kojanni.graph;

import org.micro.kojanni.base_structure.DynamicArray;

import java.util.Objects;

/**
 * Алгоритм Демукрона:
 * 1. Создать массив полустепеней захода всех вершин
 * 2. Находим вершины со степенью входа 0.
 * Если в какой-то момент не можем найти вершины со степенью 0, граф содержит цикл
 * 3. Вычесть из массива полустепеней захода «вклад» каждой вершины с нулевой полустепенью захода
 * 4. Повторяем процесс для следующего уровня
 */
public class DemucronAlgorithm extends GraphAlgorithm<int[][]> {

    public DemucronAlgorithm(Graph graph) {
        super(graph);
    }
    public DemucronAlgorithm(int[][] adjacencyMatrix, int n) {
        super(new Graph(adjacencyMatrix, n));
    }

    @Override
    public int[][] execute() {
        //Массив полустепеней захода всех вершин
        int[] inDegree = graph.calculateInDegrees();

        boolean[] processed = createVisitedArray();

        DynamicArray.VectorArray<int[]> levels = new DynamicArray.VectorArray<>();

        int processedCount = 0;
        while (processedCount < vertexCount) {
            //Находим вершины со степенью входа 0
            DynamicArray.VectorArray<Integer> currentLevel = new DynamicArray.VectorArray<>();

            for (int i = 0; i < vertexCount; i++) {
                if (!processed[i] && inDegree[i] == 0) {
                    currentLevel.add(i, currentLevel.size());
                }
            }

            // Нет вершины со степенью 0, граф содержит цикл
            if (currentLevel.size() == 0) {
                return null;
            }

            int[] levelArray = new int[currentLevel.size()];
            for (int i = 0; i < currentLevel.size(); i++) {
                int vertex = currentLevel.get(i);
                levelArray[i] = vertex;
                processed[vertex] = true;
                processedCount++;

                // Уменьшаем для всех смежных вершин
                forEachNeighbor(vertex, adjacent -> {
                    if (!Objects.equals(adjacent, -1)) {
                        inDegree[adjacent]--;
                    }
                });
            }

            levels.add(levelArray, levels.size());
        }

        return buildResult(levels);
    }

    private int[][] buildResult(DynamicArray.VectorArray<int[]> levels) {
        int[][] result = new int[levels.size()][];
        for (int i = 0; i < levels.size(); i++) {
            result[i] = levels.get(i);
        }
        return result;
    }
}
