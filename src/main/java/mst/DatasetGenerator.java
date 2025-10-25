package mst;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class DatasetGenerator {
    private static final Random R = new Random();

    public static void main(String[] args) throws IOException {
        makeFile("input_small.json", 5, 4, 30);
        makeFile("input_medium.json", 10, 30, 300);
        makeFile("input_large.json", 10, 300, 1000);
        makeFile("input_extra.json", 5, 1000, 3000);
        System.out.println("All datasets generated.");
    }

    private static void makeFile(String filename, int count, int minV, int maxV) throws IOException {
        ObjectMapper M = new ObjectMapper();
        ObjectNode root = M.createObjectNode();
        ArrayNode graphs = M.createArrayNode();
        for (int i = 0; i < count; i++) {
            int v = minV + R.nextInt(Math.max(1, maxV - minV + 1));
            ArrayNode nodes = M.createArrayNode();
            List<String> labels = new ArrayList<>();
            for (int j = 0; j < v; j++) {
                String name = "N" + (j + 1);
                labels.add(name);
                nodes.add(name);
            }
            List<Edge> edges = new ArrayList<>();
            for (int j = 1; j < v; j++) {
                int to = j;
                int from = R.nextInt(j);
                double w = 1 + R.nextInt(100);
                edges.add(new Edge(labels.get(from), labels.get(to), w));
            }
            int extra = v + R.nextInt(Math.max(1, v));
            for (int e = 0; e < extra; e++) {
                int a = R.nextInt(v);
                int b = R.nextInt(v);
                if (a == b) continue;
                double w = 1 + R.nextInt(1000);
                edges.add(new Edge(labels.get(a), labels.get(b), w));
            }
            ObjectNode gnode = M.createObjectNode();
            gnode.put("id", i + 1);
            gnode.set("nodes", nodes);
            ArrayNode edgesNode = M.createArrayNode();
            for (Edge ed : edges) {
                ObjectNode en = M.createObjectNode();
                en.put("from", ed.from);
                en.put("to", ed.to);
                en.put("weight", ed.weight);
                edgesNode.add(en);
            }
            gnode.set("edges", edgesNode);
            graphs.add(gnode);
        }
        root.set("graphs", graphs);
        M.writerWithDefaultPrettyPrinter().writeValue(new File(filename), root);
        System.out.println("Created " + filename);
    }
}
