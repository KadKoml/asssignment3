package mst;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class IOUtil {
    private static final ObjectMapper M = new ObjectMapper();

    public static List<Graph> readGraphsFromJson(File f) throws IOException {
        JsonNode root = M.readTree(f);
        List<Graph> graphs = new ArrayList<>();
        for (JsonNode gnode : root.get("graphs")) {
            List<String> nodes = new ArrayList<>();
            for (JsonNode n : gnode.get("nodes")) nodes.add(n.asText());
            List<Edge> edges = new ArrayList<>();
            for (JsonNode en : gnode.get("edges")) {
                edges.add(new Edge(en.get("from").asText(), en.get("to").asText(), en.get("weight").asDouble()));
            }
            graphs.add(new Graph(nodes, edges));
        }
        return graphs;
    }

    public static ObjectNode resultToJson(List<Graph> graphs, List<Prim.Result> primResults, List<Kruskal.Result> kruskalResults) {
        ObjectNode root = M.createObjectNode();
        ArrayNode results = M.createArrayNode();
        for (int i = 0; i < graphs.size(); i++) {
            Graph g = graphs.get(i);
            Prim.Result pr = primResults.get(i);
            Kruskal.Result kr = kruskalResults.get(i);

            ObjectNode gr = M.createObjectNode();
            gr.put("graph_id", i + 1);
            ObjectNode inputStats = M.createObjectNode();
            inputStats.put("vertices", g.V());
            inputStats.put("edges", g.E());
            gr.set("input_stats", inputStats);

            ObjectNode pnode = M.createObjectNode();
            ArrayNode pEdges = M.createArrayNode();
            for (Edge e : pr.mstEdges) {
                ObjectNode eobj = M.createObjectNode();
                eobj.put("from", e.from);
                eobj.put("to", e.to);
                eobj.put("weight", e.weight);
                pEdges.add(eobj);
            }
            pnode.set("mst_edges", pEdges);
            pnode.put("total_cost", pr.totalCost);
            pnode.put("operations_count", pr.heapOps + pr.edgeConsiderations + pr.comparisons);
            pnode.put("execution_time_ms", pr.timeMs);
            gr.set("prim", pnode);

            ObjectNode knode = M.createObjectNode();
            ArrayNode kEdges = M.createArrayNode();
            for (Edge e : kr.mstEdges) {
                ObjectNode eobj = M.createObjectNode();
                eobj.put("from", e.from);
                eobj.put("to", e.to);
                eobj.put("weight", e.weight);
                kEdges.add(eobj);
            }
            knode.set("mst_edges", kEdges);
            knode.put("total_cost", kr.totalCost);
            knode.put("operations_count", kr.comparisons + kr.finds + kr.unions);
            knode.put("execution_time_ms", kr.timeMs);
            gr.set("kruskal", knode);

            results.add(gr);
        }
        root.set("results", results);
        return root;
    }

    public static void writeJsonToFile(JsonNode node, File out) throws IOException {
        M.writerWithDefaultPrettyPrinter().writeValue(out, node);
    }
}
