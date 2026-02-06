package org.micro.kojanni.graph.spanning_tree;

import org.micro.kojanni.graph.GraphAlgorithm;

/**
 * Алгоритм Краскала:
 * - все рёбра графа сортируются по весу
 * - из всех рёбер, добавление которых к уже имеющемуся множеству не вызовет появление в нём цикла,
 * выбирается ребро минимального веса и добавляется к уже имеющемуся множеству
 * - когда таких рёбер больше нет, алгоритм завершён.
 */
public class KruskalAlgorithm extends GraphAlgorithm<Edge[]> {

    private final WeightedGraph weightedGraph;

    public KruskalAlgorithm(WeightedGraph graph) {
        super(graph);
        this.weightedGraph = graph;
    }

    public KruskalAlgorithm(int[][] adjacencyMatrix, int[][] weights, int n) {
        this(new WeightedGraph(adjacencyMatrix, weights, n));
    }

    @Override
    public Edge[] execute() {
        Edge[] allEdges = weightedGraph.getAllEdges();
        sortEdges(allEdges);

        UnionFind uf = new UnionFind(vertexCount);
        Edge[] mst = new Edge[vertexCount - 1];
        int mstIndex = 0;

        for (Edge edge : allEdges) {
            if (uf.union(edge.v1, edge.v2)) {
                mst[mstIndex++] = edge;
            }
        }

        if (mstIndex < vertexCount - 1) {
            return null;
        }

        return mst;
    }

    private void sortEdges(Edge[] edges) {
        for (int i = 0; i < edges.length - 1; i++) {
            for (int j = 0; j < edges.length - i - 1; j++) {
                if (edges[j].weight > edges[j + 1].weight) {
                    Edge temp = edges[j];
                    edges[j] = edges[j + 1];
                    edges[j + 1] = temp;
                }
            }
        }
    }
}
