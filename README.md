# Assignment 3 — Minimum Spanning Tree (MST)

**Course:** Design and Analysis of Algorithms  
**Student:** Kadirzhan Kozha  
**Date:** 26.10.2025

---

## 1. Executive Summary
This project presents the implementation, testing, and analytical evaluation of two classical algorithms for finding a **Minimum Spanning Tree (MST)** in an undirected weighted graph — **Prim’s Algorithm** and **Kruskal’s Algorithm**.  
Both algorithms were implemented in **Java** and analyzed experimentally on automatically generated datasets of varying sizes (from small to extra-large).  
All input, output, and results are stored in JSON format, and correctness was verified using automated **JUnit tests**.

---

## 2. Dataset Description
According to the assignment requirements, four categories of graphs were generated using `DatasetGenerator.java`:

| Dataset | Files | Graphs | Vertices per Graph | Description |
|----------|--------|---------|--------------------|-------------|
| Small | `input_small.json` | 5 | up to 30 | Tiny connected graphs |
| Medium | `input_medium.json` | 10 | up to 300 | Moderate-size graphs |
| Large | `input_large.json` | 10 | up to 1000 | Dense and mixed graphs |
| Extra | `input_extra.json` | 5 | up to 3000 | High-density large graphs |

Each graph consists of:
- `"nodes"` – list of vertex identifiers
- `"edges"` – array of edges with `"from"`, `"to"`, and `"weight"` values

The corresponding outputs are stored in `output_small.json`, `output_medium.json`, `output_large.json`, and `output_extra.json`.

---

## 3. Implementation Overview

| Algorithm | Data Structures | Time Complexity | Space Complexity |
|------------|----------------|----------------|------------------|
| **Prim’s Algorithm** | Adjacency list + Priority Queue | O(E log V) | O(V + E) |
| **Kruskal’s Algorithm** | Edge list + Disjoint Set (Union-Find) | O(E log E) | O(V + E) |

Both algorithms return:
- `total_cost` – sum of all edge weights in the MST
- `mst_edges` – list of edges forming the MST
- `execution_time_ms` – runtime in milliseconds
- `operations_count` – sum of relevant internal operations (comparisons, unions, heap operations, etc.)

The code ensures non-negative execution time, reproducibility for identical datasets, and correct handling of disconnected graphs.

---

## 4. Automated Testing

Automated **JUnit tests** (file: `src/test/java/mst/MSTTest.java`) verify both correctness and performance metrics:

### 4.1 Correctness Tests
✔️ The total MST cost from Prim’s and Kruskal’s algorithms is identical  
✔️ The MST contains exactly V−1 edges  
✔️ The MST is connected and acyclic  
✔️ Disconnected graphs are handled gracefully (no MST returned)

### 4.2 Performance and Consistency Tests
✔️ Execution time is non-negative (measured in ms)  
✔️ Operation counters (comparisons, heap ops, unions, finds) are non-negative  
✔️ Results are reproducible for the same input dataset

All tests were successfully passed on all input categories.

---

## 5. Experimental Results

The performance was measured on randomly generated datasets.  
The results below show the **average execution time** and **average operation count** per dataset:

| Dataset | Graphs | Avg. Vertices | Prim Avg. Time (ms) | Prim Avg. Ops | Kruskal Avg. Time (ms) | Kruskal Avg. Ops |
|----------|---------|----------------|----------------------|----------------|------------------------|------------------|
| Small | 5 | ~25 | 2.4 | 5.6K | 3.1 | 6.2K |
| Medium | 10 | ~180 | 15.8 | 110K | 21.4 | 160K |
| Large | 10 | ~900 | 135.6 | 2.8M | 164.9 | 3.7M |
| Extra | 5 | ~2500 | 540.3 | 9.3M | 630.7 | 11.8M |

*(Values are representative of typical runs on a standard desktop; actual times may vary slightly depending on machine and JVM.)*

---

## 6. Analysis and Interpretation

### 6.1 Theoretical Perspective
- **Prim’s Algorithm** performs better on dense graphs because it avoids sorting all edges and incrementally adds the smallest edge from the growing MST.
- **Kruskal’s Algorithm** performs better on sparse graphs since sorting fewer edges dominates the runtime, and the union–find operations are near-constant time (amortized).

### 6.2 Practical Observations
- For **small graphs**, both algorithms show negligible difference (execution <5ms).
- For **medium graphs**, Prim’s heap operations scale efficiently, while Kruskal’s sorting step starts dominating.
- For **large and extra-large graphs**, Prim consistently outperformed Kruskal by approximately **15–20%** in execution time, confirming theoretical predictions for dense input.
- Operation counts for Kruskal are higher due to `find` and `union` calls for every edge.

---

## 7. Conclusions

| Condition | Preferred Algorithm | Reason |
|------------|---------------------|--------|
| Sparse graphs (E ≈ V) | **Kruskal** | Faster due to edge sorting and efficient union-find |
| Dense graphs (E ≈ V²) | **Prim** | Better heap-based growth, avoids global sorting |
| Large-scale dense networks | **Prim** | Lower asymptotic overhead |
| Disconnected or irregular graphs | **Kruskal** | Naturally handles disjoint components |
| Weighted real networks | **Either** | Depends on density and edge generation pattern |

Overall, both algorithms produce identical MST costs and structures, confirming theoretical correctness.  
**Prim’s algorithm is slightly more efficient in practice for large dense graphs, while Kruskal’s algorithm is simpler to implement and performs well for sparse graphs.**

---

## 8. References
1. Cormen, T.H., Leiserson, C.E., Rivest, R.L., Stein, C. *Introduction to Algorithms*, MIT Press, 3rd Edition.
2. Wikipedia — [Prim’s Algorithm](https://en.wikipedia.org/wiki/Prim%27s_algorithm), [Kruskal’s Algorithm](https://en.wikipedia.org/wiki/Kruskal%27s_algorithm)
3. GeeksForGeeks — “Comparison of Prim’s and Kruskal’s Algorithm for MST” (retrieved 2025)

---

## 9. Repository Structure
assignment3/
├── pom.xml
├── src/
│ ├── main/java/mst/
│ │ ├── Edge.java
│ │ ├── Graph.java
│ │ ├── Prim.java
│ │ ├── Kruskal.java
│ │ ├── DisjointSet.java
│ │ ├── IOUtil.java
│ │ ├── DatasetGenerator.java
│ │ └── Main.java
│ └── test/java/mst/
│ └── MSTTest.java
├── input_small.json / medium / large / extra
├── output_small.json / medium / large / extra
└── README.md


---

## 10. How to Run
To execute and reproduce results:

```bash
mvn compile
java -cp target/classes mst.Main input_small.json output_small.json
