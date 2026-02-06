package org.micro.kojanni.graph;

import org.micro.kojanni.base_structure.DynamicArray;
import org.micro.kojanni.base_structure.Stack;

/**
 * Алгоритм Тарьяна
 * Для каждой вершины осуществляем поиск в глубину
 * Если обнаружена вершина, из которой больше нет путей, помещаем её в стек
 * Заканчиваем цикл
 * Извлекаем вершины из стека в обратном порядке
 */
public class TarjanAlgorithm extends GraphAlgorithm<int[][]> {
    
    private int[] ids;
    private int[] low;
    private boolean[] onStack;
    private Stack<Integer> stack;
    private int time;
    private DynamicArray.VectorArray<DynamicArray.VectorArray<Integer>> components;
    
    public TarjanAlgorithm(Graph graph) {
        super(graph);
    }

    public TarjanAlgorithm(int[][] adjacencyMatrix, int n) {
        super(new Graph(adjacencyMatrix, n));
    }

    @Override
    public int[][] execute() {
        ids = createVertexArray(-1);
        low = createVertexArray(0);
        onStack = createVisitedArray();
        stack = new Stack(vertexCount);
        time = 0;
        components = new DynamicArray.VectorArray<>();

        for (int i = 0; i < vertexCount; i++) {
            if (ids[i] == -1) {
                dfs(i);
            }
        }

        return buildResult();
    }

    private void dfs(int v) {
        ids[v] = low[v] = time++;
        stack.push(v);
        onStack[v] = true;

        forEachNeighbor(v, neighbor -> {
            if (ids[neighbor] == -1) {
                dfs(neighbor);
                low[v] = Math.min(low[v], low[neighbor]);
            } else if (onStack[neighbor]) {
                low[v] = Math.min(low[v], ids[neighbor]);
            }
        });

        if (ids[v] == low[v]) {
            DynamicArray.VectorArray<Integer> component = new DynamicArray.VectorArray<>();
            int node;
            do {
                node = stack.pop();
                onStack[node] = false;
                component.add(node, component.size());
            } while (node != v);
            components.add(component, components.size());
        }
    }

    private int[][] buildResult() {
        int[][] result = new int[components.size()][];
        for (int i = 0; i < components.size(); i++) {
            DynamicArray.VectorArray<Integer> component = components.get(i);
            result[i] = new int[component.size()];
            for (int j = 0; j < component.size(); j++) {
                result[i][j] = component.get(j);
            }
        }
        return result;
    }
}
