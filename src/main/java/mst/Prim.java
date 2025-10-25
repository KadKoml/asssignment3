package mst;

import java.util.*;

public class Prim {
    public static class Result {
        public final List<Edge> mstEdges = new ArrayList<>();
        public double totalCost = 0;
        public long heapOps = 0;
        public long edgeConsiderations = 0;
        public long comparisons = 0;
        public long timeMs = 0;
    }

    public static Result run(Graph g, String startVertex) {
        long t0 = System.nanoTime();
        Result res = new Result();
        Set<String> visited = new HashSet<>();
        PriorityQueue<Edge> pq = new PriorityQueue<>();
        visited.add(startVertex);
        pq.addAll(g.adj.get(startVertex));
        res.heapOps += g.adj.get(startVertex).size();

        while (!pq.isEmpty() && res.mstEdges.size() < g.V() - 1) {
            Edge e = pq.poll();
            res.heapOps++;
            if (visited.contains(e.to)) {
                res.comparisons++;
                continue;
            }
            res.mstEdges.add(e);
            res.totalCost += e.weight;
            visited.add(e.to);
            for (Edge ex : g.adj.get(e.to)) {
                res.edgeConsiderations++;
                if (!visited.contains(ex.to)) {
                    pq.add(ex);
                    res.heapOps++;
                } else res.comparisons++;
            }
        }
        res.timeMs = (System.nanoTime() - t0) / 1_000_000;
        return res;
    }
}
