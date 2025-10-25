package mst;

import java.util.HashMap;
import java.util.Map;

public class DisjointSet {
    private final Map<String, String> parent = new HashMap<>();
    private final Map<String, Integer> rank = new HashMap<>();

    public long unions = 0;
    public long finds = 0;

    public DisjointSet(Iterable<String> elements) {
        for (String e : elements) {
            parent.put(e, e);
            rank.put(e, 0);
        }
    }

    public String find(String x) {
        finds++;
        String p = parent.get(x);
        if (!p.equals(x)) {
            String r = find(p);
            parent.put(x, r); // путь сжатия
            return r;
        }
        return p;
    }

    public boolean union(String a, String b) {
        unions++;
        String ra = find(a);
        String rb = find(b);
        if (ra.equals(rb)) return false;

        int rka = rank.get(ra);
        int rkb = rank.get(rb);

        if (rka < rkb) parent.put(ra, rb);
        else if (rkb < rka) parent.put(rb, ra);
        else {
            parent.put(rb, ra);
            rank.put(ra, rka + 1);
        }

        return true;
    }
}
