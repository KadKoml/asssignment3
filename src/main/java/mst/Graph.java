package mst;

import java.util.*;

public class Graph {
    public final List<String> vertices;
    public final List<Edge> edges;
    public final Map<String, List<Edge>> adj;

    public Graph(List<String> vertices, List<Edge> edges) {
        this.vertices = new ArrayList<>(vertices);
        this.edges = new ArrayList<>(edges);
        this.adj = new HashMap<>();

        for (String v : vertices) {
            adj.put(v, new ArrayList<>());
        }

        for (Edge e : edges) {
            adj.get(e.from).add(e);
            adj.get(e.to).add(new Edge(e.to, e.from, e.weight));
        }
    }

    public int V() { return vertices.size(); }
    public int E() { return edges.size(); }
}
