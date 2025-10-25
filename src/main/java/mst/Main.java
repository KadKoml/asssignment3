package mst;

import com.fasterxml.jackson.databind.JsonNode;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println("Usage: java -jar assignment3.jar <input.json> <output.json>");
            return;
        }

        File in = new File(args[0]);
        File out = new File(args[1]);

        List<Graph> graphs = IOUtil.readGraphsFromJson(in);
        List<Prim.Result> primResults = new ArrayList<>();
        List<Kruskal.Result> kruskalResults = new ArrayList<>();

        for (Graph g : graphs) {
            Prim.Result pr = Prim.run(g, g.vertices.get(0));
            Kruskal.Result kr = Kruskal.run(g);
            primResults.add(pr);
            kruskalResults.add(kr);
        }

        JsonNode root = IOUtil.resultToJson(graphs, primResults, kruskalResults);
        IOUtil.writeJsonToFile(root, out);

        System.out.println("Results written to " + out.getAbsolutePath());
    }
}
