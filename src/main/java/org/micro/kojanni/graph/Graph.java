package org.micro.kojanni.graph;

import lombok.Getter;

import java.util.Objects;

/**
 * Базовый класс для представления графа в формате вектора смежности.
 */
public class Graph {
    
    private final int[][] adjacencyMatrix;
    @Getter
    private final int vertexCount;

    public Graph(int[][] adjacencyMatrix, int vertexCount) {
        if (adjacencyMatrix == null || vertexCount <= 0) {
            throw new IllegalArgumentException("Invalid graph parameters");
        }
        this.adjacencyMatrix = adjacencyMatrix;
        this.vertexCount = vertexCount;
    }

    public int[] calculateInDegrees() {
        int[] inDegree = new int[vertexCount];
        
        for (int i = 0; i < vertexCount; i++) {
            for (int adjacent : adjacencyMatrix[i]) {
                if (Objects.equals(adjacent, -1)) {
                    break;
                }
                inDegree[adjacent]++;
            }
        }
        
        return inDegree;
    }

    public void forEachAdjacent(int vertex, VertexVisitor visitor) {
        if (vertex < 0 || vertex >= vertexCount) {
            return;
        }
        
        for (int adjacent : adjacencyMatrix[vertex]) {
            if (adjacent == -1) {
                break;
            }
            visitor.visit(adjacent);
        }
    }

    @FunctionalInterface
    public interface VertexVisitor {
        void visit(int vertex);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Graph with ").append(vertexCount).append(" vertices:\n");
        
        for (int i = 0; i < vertexCount; i++) {
            sb.append("Vertex ").append(i).append(" -> [");
            for (int j = 0; j < adjacencyMatrix[i].length; j++) {
                if (j > 0) {
                    sb.append(", ");
                }
                sb.append(adjacencyMatrix[i][j]);
            }
            sb.append("]\n");
        }
        
        return sb.toString();
    }
}
