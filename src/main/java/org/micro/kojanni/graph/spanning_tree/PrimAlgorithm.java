package org.micro.kojanni.graph.spanning_tree;


import org.micro.kojanni.base_structure.PriorityQueues;
import org.micro.kojanni.graph.GraphAlgorithm;

public class PrimAlgorithm extends GraphAlgorithm<Edge[]> {

    private final WeightedGraph weightedGraph;

    public PrimAlgorithm(WeightedGraph graph) {
        super(graph);
        this.weightedGraph = graph;
    }

    public PrimAlgorithm(int[][] adjacencyMatrix, int[][] weights, int n) {
        this(new WeightedGraph(adjacencyMatrix, weights, n));
    }

    @Override
    public Edge[] execute() {
        boolean[] inMST = new boolean[vertexCount];
        Edge[] mst = new Edge[vertexCount - 1];
        int mstIndex = 0;

        PriorityQueues.PriorityQueue<Edge> pq = new PriorityQueues.PriorityQueue<>();
        
        inMST[0] = true;
        addEdgesToQueue(0, inMST, pq);

        while (!pq.isEmpty() && mstIndex < vertexCount - 1) {
            Edge edge = pq.dequeue();
            int vertex = edge.v2;

            if (inMST[vertex]) {
                continue;
            }

            inMST[vertex] = true;
            mst[mstIndex++] = edge;

            addEdgesToQueue(vertex, inMST, pq);
        }

        if (mstIndex < vertexCount - 1) {
            return null;
        }

        return mst;
    }

    private void addEdgesToQueue(int vertex, boolean[] inMST, PriorityQueues.PriorityQueue<Edge> pq) {
        weightedGraph.forEachEdge(vertex, (adjacent, weight) -> {
            if (!inMST[adjacent]) {
                pq.enqueue(new Edge(vertex, adjacent, weight), -weight);
            }
        });
    }
}
