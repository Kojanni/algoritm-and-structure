package org.micro.kojanni.graph;

/**
 * Абстрактный базовый класс для графовых алгоритмов.
 *
 * @param <R> тип результата выполнения алгоритма
 */
public abstract class GraphAlgorithm<R> {

    protected final Graph graph;
    protected final int vertexCount;

    protected GraphAlgorithm(Graph graph) {
        if (graph == null) {
            throw new IllegalArgumentException("Graph cannot be null");
        }
        this.graph = graph;
        this.vertexCount = graph.getVertexCount();
    }

    public abstract R execute();

    protected boolean isValidVertex(int vertex) {
        return vertex >= 0 && vertex < vertexCount;
    }

    protected void validateVertex(int vertex) {
        if (!isValidVertex(vertex)) {
            throw new IllegalArgumentException(
                    "Invalid vertex: " + vertex + ". Valid range: [0, " + (vertexCount - 1) + "]"
            );
        }
    }

    /**
     * Обходит всех соседей вершины
     */
    protected void forEachNeighbor(int vertex, Graph.VertexVisitor visitor) {
        validateVertex(vertex);
        graph.forEachAdjacent(vertex, visitor);
    }

    /**
     * Создание массива для отслеживания посещённых вершин.
     * Заполнен false
     */
    protected boolean[] createVisitedArray() {
        return new boolean[vertexCount];
    }

    /**
     * Создаёт массив int для хранения данных о вершинах.
     *
     * @param defaultValue значение по умолчанию для инициализации
     * @return массив размером vertexCount
     */
    protected int[] createVertexArray(int defaultValue) {
        int[] array = new int[vertexCount];
        if (defaultValue != 0) {
            for (int i = 0; i < vertexCount; i++) {
                array[i] = defaultValue;
            }
        }
        return array;
    }

    public String getAlgorithmName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String toString() {
        return getAlgorithmName() + " on graph with " + vertexCount + " vertices";
    }

    public static void print(int[][] result) {
        if (result == null) {
            System.out.println("Результат пустой или содержит цикл.");
            return;
        }

        System.out.println("Найдено элементов: " + result.length);
        for (int i = 0; i < result.length; i++) {
            System.out.print((i + 1) + ": {");
            for (int j = 0; j < result[i].length; j++) {
                if (j > 0) System.out.print(", ");
                System.out.print(result[i][j]);
            }
            System.out.println("}");
        }
    }
}
