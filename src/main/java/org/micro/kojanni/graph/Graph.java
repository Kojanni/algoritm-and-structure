package org.micro.kojanni.graph;

import lombok.Getter;

import java.util.*;

/**
 * Базовый класс для представления графа в различных форматах.
 * Поддерживает двудольные графы и различные способы представления.
 */
public class Graph {
    
    private final int[][] adjacencyMatrix;
    @Getter
    private final int vertexCount;
    @Getter
    private final String[] vertexNames;

    public Graph(int[][] adjacencyMatrix, int vertexCount) {
        this(adjacencyMatrix, vertexCount, null);
    }

    public Graph(int[][] adjacencyMatrix, int vertexCount, String[] vertexNames) {
        if (adjacencyMatrix == null || vertexCount <= 0) {
            throw new IllegalArgumentException("Invalid graph parameters");
        }
        this.adjacencyMatrix = adjacencyMatrix;
        this.vertexCount = vertexCount;
        this.vertexNames = vertexNames != null ? vertexNames : generateDefaultNames(vertexCount);
    }

    private String[] generateDefaultNames(int count) {
        String[] names = new String[count];
        for (int i = 0; i < count; i++) {
            names[i] = String.valueOf((char) ('A' + i));
        }
        return names;
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

    /**
     * Возвращает множество вершин V.
     */
    public Set<String> getVertexSet() {
        return new LinkedHashSet<>(Arrays.asList(vertexNames));
    }

    /**
     * Возвращает множество рёбер E в формате "A-B".
     */
    public Set<String> getEdgeSet() {
        Set<String> edges = new LinkedHashSet<>();
        for (int i = 0; i < vertexCount; i++) {
            for (int adjacent : adjacencyMatrix[i]) {
                if (adjacent == -1) break;
                String edge1 = vertexNames[i] + "-" + vertexNames[adjacent];
                String edge2 = vertexNames[adjacent] + "-" + vertexNames[i];
                if (!edges.contains(edge2)) {
                    edges.add(edge1);
                }
            }
        }
        return edges;
    }

    /**
     * Возвращает матрицу смежности графа.
     */
    public int[][] getAdjacencyMatrixRepresentation() {
        int[][] matrix = new int[vertexCount][vertexCount];
        for (int i = 0; i < vertexCount; i++) {
            for (int adjacent : adjacencyMatrix[i]) {
                if (adjacent == -1) break;
                matrix[i][adjacent] = 1;
            }
        }
        return matrix;
    }

    /**
     * Возвращает матрицу инцидентности графа.
     * Строки - рёбра, столбцы - вершины.
     */
    public int[][] getIncidenceMatrix() {
        List<int[]> edges = new ArrayList<>();
        Set<String> processedEdges = new HashSet<>();
        
        for (int i = 0; i < vertexCount; i++) {
            for (int adjacent : adjacencyMatrix[i]) {
                if (adjacent == -1) break;
                String edgeKey = Math.min(i, adjacent) + "-" + Math.max(i, adjacent);
                if (!processedEdges.contains(edgeKey)) {
                    processedEdges.add(edgeKey);
                    edges.add(new int[]{i, adjacent});
                }
            }
        }
        
        int[][] matrix = new int[edges.size()][vertexCount];
        for (int i = 0; i < edges.size(); i++) {
            matrix[i][edges.get(i)[0]] = 1;
            matrix[i][edges.get(i)[1]] = 1;
        }
        return matrix;
    }

    /**
     * Возвращает список рёбер в виде пар вершин.
     */
    public List<String[]> getEdgeList() {
        List<String[]> edges = new ArrayList<>();
        Set<String> processedEdges = new HashSet<>();
        
        for (int i = 0; i < vertexCount; i++) {
            for (int adjacent : adjacencyMatrix[i]) {
                if (adjacent == -1) break;
                String edgeKey = Math.min(i, adjacent) + "-" + Math.max(i, adjacent);
                if (!processedEdges.contains(edgeKey)) {
                    processedEdges.add(edgeKey);
                    edges.add(new String[]{vertexNames[i], vertexNames[adjacent]});
                }
            }
        }
        return edges;
    }

    /**
     * Возвращает векторы смежности (массив фиксированной длины для каждой вершины).
     */
    public String[][] getAdjacencyVectors() {
        int maxDegree = 0;
        for (int i = 0; i < vertexCount; i++) {
            int degree = 0;
            for (int adjacent : adjacencyMatrix[i]) {
                if (adjacent == -1) break;
                degree++;
            }
            maxDegree = Math.max(maxDegree, degree);
        }
        
        String[][] vectors = new String[vertexCount][maxDegree];
        for (int i = 0; i < vertexCount; i++) {
            int idx = 0;
            for (int adjacent : adjacencyMatrix[i]) {
                if (adjacent == -1) break;
                vectors[i][idx++] = vertexNames[adjacent];
            }
            while (idx < maxDegree) {
                vectors[i][idx++] = "0";
            }
        }
        return vectors;
    }

    /**
     * Возвращает массивы смежности (массивы переменной длины).
     */
    public List<String>[] getAdjacencyArrays() {
        @SuppressWarnings("unchecked")
        List<String>[] arrays = new List[vertexCount];
        for (int i = 0; i < vertexCount; i++) {
            arrays[i] = new ArrayList<>();
            for (int adjacent : adjacencyMatrix[i]) {
                if (adjacent == -1) break;
                arrays[i].add(vertexNames[adjacent]);
            }
        }
        return arrays;
    }

    /**
     * Возвращает списки смежности в виде связанных списков.
     */
    public Map<String, LinkedList<String>> getAdjacencyLists() {
        Map<String, LinkedList<String>> lists = new LinkedHashMap<>();
        for (int i = 0; i < vertexCount; i++) {
            LinkedList<String> list = new LinkedList<>();
            for (int adjacent : adjacencyMatrix[i]) {
                if (adjacent == -1) break;
                list.add(vertexNames[adjacent]);
            }
            lists.put(vertexNames[i], list);
        }
        return lists;
    }

    /**
     * Возвращает структуру с оглавлением (CSR - Compressed Sparse Row).
     * Первый массив - оглавление (индексы начала списка смежности для каждой вершины).
     * Второй массив - все смежные вершины подряд.
     */
    public Object[] getCSRRepresentation() {
        List<Integer> adjacencyData = new ArrayList<>();
        int[] offsets = new int[vertexCount];
        
        for (int i = 0; i < vertexCount; i++) {
            offsets[i] = adjacencyData.size();
            for (int adjacent : adjacencyMatrix[i]) {
                if (adjacent == -1) break;
                adjacencyData.add(adjacent);
            }
        }
        
        int[] dataArray = adjacencyData.stream().mapToInt(Integer::intValue).toArray();
        return new Object[]{offsets, dataArray};
    }

    /**
     * Возвращает представление в виде списка вершин с их рёбрами.
     */
    public Map<String, List<String>> getVertexEdgeList() {
        Map<String, List<String>> vertexEdges = new LinkedHashMap<>();

        List<String[]> edges = getEdgeList();
        Map<String, Integer> edgeIndices = new HashMap<>();
        for (int i = 0; i < edges.size(); i++) {
            String[] edge = edges.get(i);
            String key1 = edge[0] + "-" + edge[1];
            String key2 = edge[1] + "-" + edge[0];
            edgeIndices.put(key1, i + 1);
            edgeIndices.put(key2, i + 1);
        }

        for (int i = 0; i < vertexCount; i++) {
            List<String> vertexEdgesList = new ArrayList<>();
            for (int adjacent : adjacencyMatrix[i]) {
                if (adjacent == -1) break;
                String edgeKey = vertexNames[i] + "-" + vertexNames[adjacent];
                Integer edgeNum = edgeIndices.get(edgeKey);
                if (edgeNum != null) {
                    vertexEdgesList.add(edgeNum + "-" + vertexNames[adjacent]);
                }
            }
            vertexEdges.put(vertexNames[i], vertexEdgesList);
        }
        
        return vertexEdges;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Graph with ").append(vertexCount).append(" vertices:\n");
        
        for (int i = 0; i < vertexCount; i++) {
            sb.append("Vertex ").append(vertexNames[i]).append(" -> [");
            boolean first = true;
            for (int adjacent : adjacencyMatrix[i]) {
                if (adjacent == -1) break;
                if (!first) sb.append(", ");
                sb.append(vertexNames[adjacent]);
                first = false;
            }
            sb.append("]\n");
        }
        
        return sb.toString();
    }
}
