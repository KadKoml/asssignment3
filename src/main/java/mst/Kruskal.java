package mst;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Kruskal {
    public static class Result {
        public final List<Edge> mstEdges = new ArrayList<>();
        public double totalCost = 0;
        public long comparisons = 0;
        public long unions = 0;
        public long finds = 0;
        public long timeMs = 0;
    }

    public static Result run(Graph g) {
        long t0 = System.nanoTime();
        Result res = new Result();
        List<Edge> edges = new ArrayList<>(g.edges);
        Collections.sort(edges);
        DisjointSet ds = new DisjointSet(g.vertices);
        for (Edge e : edges) {
            res.comparisons++;
            String ra = ds.find(e.from);
            String rb = ds.find(e.to);
            res.finds = ds.finds;
            if (!ra.equals(rb)) {
                ds.union(ra, rb);
                res.unions = ds.unions;
                res.mstEdges.add(e);
                res.totalCost += e.weight;
                if (res.mstEdges.size() == g.V() - 1) break;
            }
        }
        res.finds = ds.finds;
        res.unions = ds.unions;
        res.timeMs = (System.nanoTime() - t0) / 1_000_000;
        return res;
    }
}
