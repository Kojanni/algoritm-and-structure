package org.micro.kojanni.graph.spanning_tree;

import lombok.Getter;
import org.micro.kojanni.graph.Graph;

@Getter
public class WeightedGraph extends Graph {

    private final int[][] weights;

    public WeightedGraph(int[][] adjacencyMatrix, int[][] weights, int vertexCount) {
        super(adjacencyMatrix, vertexCount);
        this.weights = weights;
    }

    public Edge[] getAllEdges() {
        // Подсчитываем количество рёбер
        int edgeCount = 0;
        for (int i = 0; i < getVertexCount(); i++) {
            for (int j = 0; j < weights[i].length && weights[i][j] != -1; j++) {
                edgeCount++;
            }
        }

        // Собираем уникальные рёбра (каждое ребро встречается дважды в неориентированном графе)
        Edge[] edges = new Edge[edgeCount / 2];
        int[] idx = {0};
        for (int i = 0; i < getVertexCount(); i++) {
            int vertex = i;
            forEachEdge(vertex, (adjacent, weight) -> {
                if (vertex < adjacent) {
                    edges[idx[0]++] = new Edge(vertex, adjacent, weight);
                }
            });
        }
        return edges;
    }

    public void forEachEdge(int vertex, EdgeVisitor visitor) {
        int[] index = {0};
        forEachAdjacent(vertex, adjacent -> {
            visitor.visit(adjacent, weights[vertex][index[0]]);
            index[0]++;
        });
    }

    @FunctionalInterface
    public interface EdgeVisitor {
        void visit(int adjacent, int weight);
    }
}
