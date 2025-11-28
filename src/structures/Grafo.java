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
        if (!adyacenciaPesos.containsKey(start)) return orden;
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
        if (!adyacenciaPesos.containsKey(v) || visitado.contains(v)) return;
        visitado.add(v);
        orden.add(v);
        for (String w : adyacenciaPesos.getOrDefault(v, Collections.emptyMap()).keySet()) {
            if (!visitado.contains(w)) dfsRec(w, visitado, orden);
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

    // Resultado de camino mínimo
    public static class PathResult {
        public final List<String> path;
        public final int cost;
        public PathResult(List<String> path, int cost) {
            this.path = path;
            this.cost = cost;
        }
    }

    // Dijkstra para camino más barato de start a target
    public PathResult shortestPath(String start, String target) {
        if (!adyacenciaPesos.containsKey(start) || !adyacenciaPesos.containsKey(target)) {
            return new PathResult(Collections.emptyList(), -1);
        }

        Map<String, Integer> dist = new HashMap<>();
        Map<String, String> prev = new HashMap<>();
        for (String v : adyacenciaPesos.keySet()) dist.put(v, Integer.MAX_VALUE);
        dist.put(start, 0);

        PriorityQueue<String> pq = new PriorityQueue<>(Comparator.comparingInt(dist::get));
        pq.add(start);

        while (!pq.isEmpty()) {
            String v = pq.poll();
            int dv = dist.get(v);
            if (v.equals(target)) break;
            for (Map.Entry<String, Integer> e : adyacenciaPesos.getOrDefault(v, Collections.emptyMap()).entrySet()) {
                String w = e.getKey();
                int peso = e.getValue();
                int nd = dv == Integer.MAX_VALUE ? Integer.MAX_VALUE : dv + peso;
                if (nd < dist.getOrDefault(w, Integer.MAX_VALUE)) {
                    dist.put(w, nd);
                    prev.put(w, v);
                    pq.remove(w); // actualizar prioridad si existía
                    pq.add(w);
                }
            }
        }

        if (dist.getOrDefault(target, Integer.MAX_VALUE) == Integer.MAX_VALUE) {
            return new PathResult(Collections.emptyList(), -1);
        }
        // reconstruir camino
        LinkedList<String> path = new LinkedList<>();
        String cur = target;
        while (cur != null) {
            path.addFirst(cur);
            cur = prev.get(cur);
        }
        return new PathResult(path, dist.get(target));
    }
}