package structures;

import java.util.*;

public class Grafo {
    // Adyacencia con pesos: para cada vértice, un mapa destino->peso
    private final Map<String, Map<String, Integer>> adyacenciaPesos = new HashMap<>();

    public void addVertex(String id) {
        adyacenciaPesos.putIfAbsent(id, new HashMap<>());
    }

    // Método original: arista sin peso explícito (peso=1)
    public void addEdge(String from, String to, boolean bidirectional) {
        addEdge(from, to, 1, bidirectional);
    }

    // Nuevo: arista con peso
    public void addEdge(String from, String to, int peso, boolean bidirectional) {
        addVertex(from);
        addVertex(to);
        adyacenciaPesos.get(from).put(to, peso);
        if (bidirectional) {
            adyacenciaPesos.get(to).put(from, peso);
        }
    }

    public List<String> bfs(String start) {
        List<String> orden = new ArrayList<>();
        if (!adyacenciaPesos.containsKey(start))
            return orden;
        Set<String> visitado = new HashSet<>();
        Queue<String> q = new ArrayDeque<>();
        visitado.add(start);
        q.add(start);
        while (!q.isEmpty()) {
            String v = q.poll();
            orden.add(v);
            for (String w : adyacenciaPesos.getOrDefault(v, Collections.emptyMap()).keySet()) {
                if (!visitado.contains(w)) {
                    visitado.add(w);
                    q.add(w);
                }
            }
        }
        return orden;
    }

    public List<String> dfs(String start) {
        List<String> orden = new ArrayList<>();
        Set<String> visitado = new HashSet<>();
        dfsRec(start, visitado, orden);
        return orden;
    }

    private void dfsRec(String v, Set<String> visitado, List<String> orden) {
        if (!adyacenciaPesos.containsKey(v) || visitado.contains(v))
            return;
        visitado.add(v);
        orden.add(v);
        for (String w : adyacenciaPesos.getOrDefault(v, Collections.emptyMap()).keySet()) {
            if (!visitado.contains(w))
                dfsRec(w, visitado, orden);
        }
    }

    // Vista simple (sin pesos) para componentes que la usan
    public Map<String, List<String>> getAdyacencia() {
        Map<String, List<String>> simple = new HashMap<>();
        for (Map.Entry<String, Map<String, Integer>> e : adyacenciaPesos.entrySet()) {
            simple.put(e.getKey(), new ArrayList<>(e.getValue().keySet()));
        }
        return simple;
    }

    // Acceso al peso de una arista
    public Integer getPeso(String from, String to) {
        Map<String, Integer> m = adyacenciaPesos.get(from);
        return m == null ? null : m.get(to);
    }

    public Map<String, Map<String, Integer>> getAdyacenciaConPesos() {
        return adyacenciaPesos;
    }

    public void clear() {
        adyacenciaPesos.clear();
    }

    // Clase interna para representar aristas
    public static class Edge implements Comparable<Edge> {
        public String src, dest;
        public int weight;

        public Edge(String src, String dest, int weight) {
            this.src = src;
            this.dest = dest;
            this.weight = weight;
        }

        @Override
        public int compareTo(Edge other) {
            return Integer.compare(this.weight, other.weight);
        }

        @Override
        public String toString() {
            return src + "-" + dest + "(" + weight + ")";
        }
    }

    // Resultado de Kruskal
    public static class MSTResult {
        public final List<Edge> edges;
        public final int totalWeight;

        public MSTResult(List<Edge> edges, int totalWeight) {
            this.edges = edges;
            this.totalWeight = totalWeight;
        }
    }

    // Algoritmo de Kruskal para MST
    public MSTResult kruskalMST() {
        List<Edge> allEdges = new ArrayList<>();
        // Recolectar todas las aristas. Como es no dirigido (o tratado como tal para
        // MST usualmente),
        // debemos tener cuidado de no duplicar si el grafo es bidireccional en la
        // estructura.
        // Aquí asumiremos que si existe A->B y B->A con el mismo peso, es la misma
        // arista.
        // Para simplificar, agregamos todas y el Union-Find se encarga de los ciclos.
        Set<String> added = new HashSet<>();

        for (Map.Entry<String, Map<String, Integer>> e : adyacenciaPesos.entrySet()) {
            String u = e.getKey();
            for (Map.Entry<String, Integer> w : e.getValue().entrySet()) {
                String v = w.getKey();
                int peso = w.getValue();
                // Ordenar u y v para evitar duplicados A-B y B-A
                String k = u.compareTo(v) < 0 ? u + "-" + v : v + "-" + u;
                if (!added.contains(k)) {
                    allEdges.add(new Edge(u, v, peso));
                    added.add(k);
                }
            }
        }

        Collections.sort(allEdges);

        // Union-Find
        Map<String, String> parent = new HashMap<>();
        for (String v : adyacenciaPesos.keySet())
            parent.put(v, v);

        List<Edge> mstEdges = new ArrayList<>();
        int mstWeight = 0;

        for (Edge edge : allEdges) {
            String rootU = find(parent, edge.src);
            String rootV = find(parent, edge.dest);

            if (!rootU.equals(rootV)) {
                mstEdges.add(edge);
                mstWeight += edge.weight;
                parent.put(rootU, rootV);
            }
        }

        return new MSTResult(mstEdges, mstWeight);
    }

    private String find(Map<String, String> parent, String i) {
        if (!parent.get(i).equals(i)) {
            parent.put(i, find(parent, parent.get(i)));
        }
        return parent.get(i);
    }
}