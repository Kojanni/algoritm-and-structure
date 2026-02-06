package org.micro.kojanni.graph.spanning_tree;

import org.micro.kojanni.graph.GraphAlgorithm;

public class BoruvkaAlgorithm extends GraphAlgorithm<Edge[]> {

    private final WeightedGraph weightedGraph;

    public BoruvkaAlgorithm(WeightedGraph graph) {
        super(graph);
        this.weightedGraph = graph;
    }

    public BoruvkaAlgorithm(int[][] adjacencyMatrix, int[][] weights, int n) {
        this(new WeightedGraph(adjacencyMatrix, weights, n));
    }

    @Override
    public Edge[] execute() {
        UnionFind uf = new UnionFind(vertexCount);
        Edge[] allEdges = weightedGraph.getAllEdges();
        Edge[] mst = new Edge[vertexCount - 1];
        int mstIndex = 0;

        while (mstIndex < vertexCount - 1) {
            Edge[] cheapest = new Edge[vertexCount];

            // Находим минимальное ребро для каждой компоненты
            for (Edge edge : allEdges) {
                int set1 = uf.find(edge.v1);
                int set2 = uf.find(edge.v2);

                if (set1 != set2) {
                    if (cheapest[set1] == null || cheapest[set1].weight > edge.weight) {
                        cheapest[set1] = edge;
                    }
                    if (cheapest[set2] == null || cheapest[set2].weight > edge.weight) {
                        cheapest[set2] = edge;
                    }
                }
            }

            // Добавляем найденные минимальные рёбра
            int prevIndex = mstIndex;
            for (Edge edge : cheapest) {
                if (edge != null && uf.union(edge.v1, edge.v2)) {
                    mst[mstIndex++] = edge;
                }
            }
            
            // Если не добавлено ни одного ребра, граф несвязный
            if (mstIndex == prevIndex) {
                return null;
            }
        }

        return mst;
    }
}
