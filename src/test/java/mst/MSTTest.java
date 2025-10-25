package mst;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class MSTTest {

    private static List<Graph> allGraphs;

    @BeforeAll
    public static void load() throws Exception {
        File f = new File("input_small.json");
        if (f.exists()) {
            allGraphs = IOUtil.readGraphsFromJson(f);
        } else {
            allGraphs = new ArrayList<>();

            List<String> nodes = Arrays.asList("A","B","C","D");
            List<Edge> edges = new ArrayList<>();
            edges.add(new Edge("A","B",1));
            edges.add(new Edge("B","C",2));
            edges.add(new Edge("C","D",3));
            edges.add(new Edge("A","D",4));
            allGraphs.add(new Graph(nodes, edges));
        }
    }

    private void assertConnectedAndNoCycles(Graph g, List<Edge> mstEdges) {
        int V = g.V();
        // edges count
        assertTrue(mstEdges.size() <= Math.max(0, V - 1));
        if (V == 0) return;
        if (mstEdges.size() < V - 1) {

            return;
        }

        Map<String, List<String>> adj = new HashMap<>();
        for (String v : g.vertices) adj.put(v, new ArrayList<>());
        for (Edge e : mstEdges) {
            adj.get(e.from).add(e.to);
            adj.get(e.to).add(e.from);
        }
        Set<String> visited = new HashSet<>();
        Deque<String> dq = new ArrayDeque<>();
        dq.add(g.vertices.get(0));
        visited.add(g.vertices.get(0));
        while (!dq.isEmpty()) {
            String cur = dq.poll();
            for (String to : adj.get(cur)) if (!visited.contains(to)) {
                visited.add(to);
                dq.add(to);
            }
        }
        assertEquals(V, visited.size(), "MST should connect all vertices");

        assertEquals(V - 1, mstEdges.size(), "MST must have V-1 edges for connected graph");
    }

    @Test
    public void correctnessAndPerformanceOnSampleGraphs() {
        for (Graph g : allGraphs) {
            Prim.Result p = Prim.run(g, g.vertices.get(0));
            Kruskal.Result k = Kruskal.run(g);


            assertEquals(p.totalCost, k.totalCost, 1e-6, "Prim and Kruskal must give equal total cost");


            assertTrue(p.mstEdges.size() <= Math.max(0, g.V() - 1));
            assertTrue(k.mstEdges.size() <= Math.max(0, g.V() - 1));


            assertConnectedAndNoCycles(g, p.mstEdges);
            assertConnectedAndNoCycles(g, k.mstEdges);


            assertTrue(p.timeMs >= 0);
            assertTrue(k.timeMs >= 0);

            assertTrue(p.heapOps >= 0);
            assertTrue(p.edgeConsiderations >= 0);
            assertTrue(p.comparisons >= 0);

            assertTrue(k.comparisons >= 0);
            assertTrue(k.finds >= 0);
            assertTrue(k.unions >= 0);
        }
    }

    @Test
    public void reproducibilityTest() {
        Graph g = allGraphs.get(0);
        Prim.Result p1 = Prim.run(g, g.vertices.get(0));
        Prim.Result p2 = Prim.run(g, g.vertices.get(0));
        Kruskal.Result k1 = Kruskal.run(g);
        Kruskal.Result k2 = Kruskal.run(g);


        assertEquals(p1.totalCost, p2.totalCost, 1e-9);
        assertEquals(k1.totalCost, k2.totalCost, 1e-9);
        assertEquals(p1.mstEdges.size(), p2.mstEdges.size());
        assertEquals(k1.mstEdges.size(), k2.mstEdges.size());
    }

    @Test
    public void disconnectedGraphHandledGracefully() {

        List<String> nodes = Arrays.asList("A","B","C","D","E");
        List<Edge> edges = new ArrayList<>();

        edges.add(new Edge("A","B",1));

        edges.add(new Edge("C","D",2));
        edges.add(new Edge("D","E",3));
        Graph g = new Graph(nodes, edges);

        Prim.Result p = Prim.run(g, "A");
        Kruskal.Result k = Kruskal.run(g);


        assertTrue(p.mstEdges.size() < g.V() - 1 || k.mstEdges.size() < g.V() - 1);
    }
}
