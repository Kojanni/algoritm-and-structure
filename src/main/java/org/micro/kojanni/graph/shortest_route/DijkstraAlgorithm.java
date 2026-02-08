package org.micro.kojanni.graph.shortest_route;


import org.micro.kojanni.base_structure.PriorityQueues;
import org.micro.kojanni.graph.GraphAlgorithm;
import org.micro.kojanni.graph.spanning_tree.Edge;
import org.micro.kojanni.graph.spanning_tree.WeightedGraph;

/**
 * Находит кратчайшие пути от одной из вершин графа до всех остальных.
 * - Метка исходной вершины A полагается равной 0, метки остальных вершин— бесконечности.
 * Расстояния от исходной вершины до других вершин пока неизвестны.
 * Все вершины графа помечаются как не посещённые.
 * - Из не посещённых вершин выбирается вершина с наименьшей меткой. Для неё рассматриваются соседние вершины.
 * Для каждого соседа, отмеченного как не посещённая вершина, считается длина пути как сумма
 * метки рассматриваемой вершины и длины ребра, соединяющего рассматриваемую вершину с этим соседом.
 * Если полученное значение длины меньше значения метки соседа, заменяется значение метки полученным значением длины.
 * Рассмотрев всех соседей, рассматриваемая вершина помечается как посещённая.
 * - Если все вершины посещены, алгоритм завершается, иначе повторяется
 */
public class DijkstraAlgorithm extends GraphAlgorithm<Edge[]> {
    
    private final WeightedGraph weightedGraph;

    private final int startVertex;

    public DijkstraAlgorithm(WeightedGraph graph, int startVertex) {
        super(graph);
        this.weightedGraph = graph;
        this.startVertex = startVertex;
        validateVertex(startVertex);
    }

    public DijkstraAlgorithm(WeightedGraph graph) {
        this(graph, 0);
    }

    public DijkstraAlgorithm(int[][] adjacency, int[][] weights, int vertexCount, int startVertex) {
        this(new WeightedGraph(adjacency, weights, vertexCount), startVertex);
    }

    @Override
    public Edge[] execute() {
        // Метка исходной вершины A полагается равной 0, метки остальных вершин — бесконечности
        int[] distance = createVertexArray(Integer.MAX_VALUE);
        int[] parent = createVertexArray(-1);
        boolean[] visited = createVisitedArray();

        distance[startVertex] = 0;

        // Выбор вершины с наименьшей меткой - Создаём приоритетную очередь
        PriorityQueues.PriorityQueue<VertexDistance> pq = new PriorityQueues.PriorityQueue<>(vertexCount);
        pq.enqueue(new VertexDistance(startVertex, 0), 0);

        int processedVertices = 0;

        while (!pq.isEmpty()) {
            VertexDistance current = pq.dequeue();
            int u = current.vertex;

            if (visited[u]) {
                continue;
            }

            visited[u] = true;
            processedVertices++;

            final int currentVertex = u;
            final int currentDistance = distance[u];

            //Рассмотрение соседних вершин
            weightedGraph.forEachEdge(u, (adjacent, weight) -> {
                //Релаксация рёбер
                if (!visited[adjacent] && currentDistance != Integer.MAX_VALUE) {
                    int newDistance = currentDistance + weight;
                    if (newDistance < distance[adjacent]) {
                        distance[adjacent] = newDistance;
                        parent[adjacent] = currentVertex;

                        // Добавляем в очередь с отрицательным приоритетом (для минимальной кучи)
                        pq.enqueue(new VertexDistance(adjacent, newDistance), -newDistance);
                    }
                }
            });
        }

        if (processedVertices < vertexCount) {
            return null; // граф несвязный
        }

        return buildEdgeTree(parent);
    }

    private Edge[] buildEdgeTree(int[] parent) {
        Edge[] result = new Edge[vertexCount - 1];
        int edgeCount = 0;

        for (int v = 0; v < vertexCount; v++) {
            if (v != startVertex && parent[v] != -1) {
                int u = parent[v];
                int weight = getEdgeWeight(u, v);
                result[edgeCount++] = new Edge(u, v, weight);
            }
        }

        return result;
    }

    private int getEdgeWeight(int from, int to) {
        int[] weight = {-1};
        weightedGraph.forEachEdge(from, (adjacent, w) -> {
            if (adjacent == to) {
                weight[0] = w;
            }
        });
        return weight[0];
    }

    /**
     * Возвращает массив расстояний от стартовой вершины до всех остальных
     */
    public int[] getDistances() {
        int[] distance = createVertexArray(Integer.MAX_VALUE);
        boolean[] visited = createVisitedArray();
        
        distance[startVertex] = 0;
        
        PriorityQueues.PriorityQueue<VertexDistance> pq = new PriorityQueues.PriorityQueue<>(vertexCount);
        pq.enqueue(new VertexDistance(startVertex, 0), 0);
        
        while (!pq.isEmpty()) {
            VertexDistance current = pq.dequeue();
            int u = current.vertex;
            
            if (visited[u]) {
                continue;
            }
            
            visited[u] = true;

            final int currentDistance = distance[u];
            
            weightedGraph.forEachEdge(u, (adjacent, weight) -> {
                if (!visited[adjacent] && currentDistance != Integer.MAX_VALUE) {
                    int newDistance = currentDistance + weight;
                    if (newDistance < distance[adjacent]) {
                        distance[adjacent] = newDistance;
                        pq.enqueue(new VertexDistance(adjacent, newDistance), -newDistance);
                    }
                }
            });
        }
        
        return distance;
    }

    /**
     * Возвращает массив родителей для восстановления путей
     */
    public int[] getParents() {
        int[] distance = createVertexArray(Integer.MAX_VALUE);
        int[] parent = createVertexArray(-1);
        boolean[] visited = createVisitedArray();
        
        distance[startVertex] = 0;
        
        PriorityQueues.PriorityQueue<VertexDistance> pq = new PriorityQueues.PriorityQueue<>(vertexCount);
        pq.enqueue(new VertexDistance(startVertex, 0), 0);
        
        while (!pq.isEmpty()) {
            VertexDistance current = pq.dequeue();
            int u = current.vertex;
            
            if (visited[u]) {
                continue;
            }
            
            visited[u] = true;
            
            final int currentVertex = u;
            final int currentDistance = distance[u];
            
            weightedGraph.forEachEdge(u, (adjacent, weight) -> {
                if (!visited[adjacent] && currentDistance != Integer.MAX_VALUE) {
                    int newDistance = currentDistance + weight;
                    if (newDistance < distance[adjacent]) {
                        distance[adjacent] = newDistance;
                        parent[adjacent] = currentVertex;
                        pq.enqueue(new VertexDistance(adjacent, newDistance), -newDistance);
                    }
                }
            });
        }
        
        return parent;
    }

    /**
     * Узел для хранения в приоритетной очереди
     */
    private static class VertexDistance {
        final int vertex;
        final int distance;

        VertexDistance(int vertex, int distance) {
            this.vertex = vertex;
            this.distance = distance;
        }
    }
}
