package org.micro.kojanni.graph.spanning_tree;

public class Edge {
    public final int v1;
    public final int v2;
    public final int weight;

    public Edge(int v1, int v2, int weight) {
        this.v1 = v1;
        this.v2 = v2;
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "(" + v1 + "-" + v2 + ":" + weight + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Edge edge)) return false;
        return (v1 == edge.v1 && v2 == edge.v2) || (v1 == edge.v2 && v2 == edge.v1);
    }

    @Override
    public int hashCode() {
        return v1 + v2;
    }
}
