package org.micro.kojanni.graph;

import java.util.Arrays;

/**
 * Демонстрация различных представлений двудольного графа A(3,4) с 5 рёбрами.
 * Граф: V = {A, B, C, D, E, F, G}, E = {A-D, A-F, B-D, B-E, C-E}
 */
public class GraphRepresentationDemo {

    public static void main(String[] args) {
        System.out.println("ДВУДОЛЬНЫЙ ГРАФ A(3,4) С 5 РЁБРАМИ");

        String[] vertexNames = {"A", "B", "C", "D", "E", "F", "G"};
        int[][] adjacencyMatrix = {
            {3, 5, -1, -1},
            {3, 4, -1, -1},
            {4, -1, -1, -1},
            {0, 1, -1, -1},
            {1, 2, -1, -1},
            {0, -1, -1, -1},
            {-1, -1, -1, -1}
        };

        Graph graph = new Graph(adjacencyMatrix, 7, vertexNames);
        System.out.println();

        printVertexAndEdgeSets(graph);
        printAdjacencyMatrix(graph);
        printIncidenceMatrix(graph);
        printEdgeList(graph);
        printAdjacencyVectors(graph);
        printAdjacencyArrays(graph);
        printAdjacencyLists(graph);
        printCSRRepresentation(graph);
        printVertexEdgeList(graph);
    }

    private static void printVertexAndEdgeSets(Graph graph) {
        System.out.println("Перечисление множеств");
        System.out.println("V = " + graph.getVertexSet());
        System.out.println("E = " + graph.getEdgeSet());

        System.out.println();
    }

    private static void printAdjacencyMatrix(Graph graph) {
        System.out.println("Матрица смежности");
        int[][] matrix = graph.getAdjacencyMatrixRepresentation();
        String[] names = {"A", "B", "C", "D", "E", "F", "G"};

        System.out.print(" | ");
        for (String name : names) {
            System.out.print(name + " ");
        }
        System.out.println();

        System.out.print("-|");
        for (int i = 0; i < names.length; i++) {
            System.out.print("--");
        }
        System.out.println();

        for (int i = 0; i < matrix.length; i++) {
            System.out.print(names[i] + "| ");
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }

        System.out.println();
    }

    private static void printIncidenceMatrix(Graph graph) {
        System.out.println("Матрица инцидентности");
        int[][] matrix = graph.getIncidenceMatrix();
        String[] names = {"A", "B", "C", "D", "E", "F", "G"};

        System.out.print(" | ");
        for (String name : names) {
            System.out.print(name + " ");
        }
        System.out.println();

        System.out.print("-|");
        for (int i = 0; i < names.length; i++) {
            System.out.print("--");
        }
        System.out.println();

        for (int i = 0; i < matrix.length; i++) {
            System.out.print((i + 1) + "| ");
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }

        System.out.println();
    }

    private static void printEdgeList(Graph graph) {
        System.out.println("Перечень рёбер");
        java.util.List<String[]> edges = graph.getEdgeList();

        for (int i = 0; i < edges.size(); i++) {
            String[] edge = edges.get(i);
            System.out.printf("%d| %s  %s%n", (i + 1), edge[0], edge[1]);
        }

        System.out.println();
    }

    private static void printAdjacencyVectors(Graph graph) {
        System.out.println("Векторы смежности");
        String[][] vectors = graph.getAdjacencyVectors();
        String[] vertexNames = graph.getVertexNames();

        for (int i = 0; i < vectors.length; i++) {
            System.out.print(vertexNames[i] + "| ");
            for (String v : vectors[i]) {
                System.out.print(v + " ");
            }
            System.out.println();
        }

        System.out.println();
    }

    private static void printAdjacencyArrays(Graph graph) {
        System.out.println("Массивы смежности");
        java.util.List<String>[] arrays = graph.getAdjacencyArrays();

        for (int i = 0; i < arrays.length; i++) {
            System.out.print(graph.getVertexNames()[i] + "| ");
            for (String v : arrays[i]) {
                System.out.print(v + " ");
            }
            System.out.println();
        }

        System.out.println();
    }

    private static void printAdjacencyLists(Graph graph) {
        System.out.println("Списки смежности");
        java.util.Map<String, java.util.LinkedList<String>> lists = graph.getAdjacencyLists();

        for (java.util.Map.Entry<String, java.util.LinkedList<String>> entry : lists.entrySet()) {
            System.out.print(entry.getKey() + "| ");
            StringBuilder joiner = new StringBuilder();
            for (String v : entry.getValue()) {
                joiner.append(v).append(" - ");
            }
            int split = joiner.lastIndexOf(" - ");
            if (split > 0) joiner.delete(split, split - 1 + " - ".length());
            System.out.println(joiner);
        }

        System.out.println();
    }

    private static void printCSRRepresentation(Graph graph) {
        System.out.println("Структура с оглавлением");
        Object[] csr = graph.getCSRRepresentation();
        int[] offsets = (int[]) csr[0];
        int[] data = (int[]) csr[1];

        String[] vertexNames = graph.getVertexNames();
        Arrays.stream(vertexNames).forEach(v -> System.out.printf("%-6s",v));
        System.out.println();
        for (int offset : offsets) {
            System.out.printf("%-6s", offset);
        }
        System.out.println();

        System.out.println("-------------------------");

        for (int i = 0; i < data.length; i++) {
            System.out.print(i + " ");
        }
        System.out.println();

        for (int d : data) {
            System.out.print(vertexNames[d] + " ");
        }

        System.out.println();
        System.out.println();
    }

    private static void printVertexEdgeList(Graph graph) {
        System.out.println("Список вершин и список рёбер");
        java.util.Map<String, java.util.List<String>> vertexEdges = graph.getVertexEdgeList();

        int maxEdges = vertexEdges.values().stream()
            .mapToInt(java.util.List::size)
            .max()
            .orElse(0);

        for (java.util.Map.Entry<String, java.util.List<String>> entry : vertexEdges.entrySet()) {
            System.out.printf("%-6s", entry.getKey());
        }
        System.out.println();

        for (int i = 0; i < vertexEdges.size(); i++) {
            System.out.print("------");
        }
        System.out.println();

        for (int row = 0; row < maxEdges; row++) {
            for (java.util.Map.Entry<String, java.util.List<String>> entry : vertexEdges.entrySet()) {
                java.util.List<String> edges = entry.getValue();
                if (row < edges.size()) {
                    System.out.printf("%-6s", edges.get(row));
                } else {
                    System.out.printf("%-6s", "");
                }
            }
            System.out.println();
        }
    }
}
