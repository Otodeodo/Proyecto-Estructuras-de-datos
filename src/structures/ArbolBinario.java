package structures;

import java.util.ArrayList;
import java.util.List;

public class ArbolBinario {
    private NodoArbol raiz;

    public void clear() { raiz = null; }

    public void insert(int valor) {
        raiz = insertRec(raiz, valor);
    }

    private NodoArbol insertRec(NodoArbol nodo, int valor) {
        if (nodo == null) return new NodoArbol(valor);
        if (valor < nodo.valor) nodo.izquierdo = insertRec(nodo.izquierdo, valor);
        else if (valor > nodo.valor) nodo.derecho = insertRec(nodo.derecho, valor);
        // iguales: no insertar duplicados
        return nodo;
    }

    public boolean search(int valor) {
        NodoArbol actual = raiz;
        while (actual != null) {
            if (valor == actual.valor) return true;
            actual = valor < actual.valor ? actual.izquierdo : actual.derecho;
        }
        return false;
    }

    public void delete(int valor) {
        raiz = deleteRec(raiz, valor);
    }

    private NodoArbol deleteRec(NodoArbol nodo, int valor) {
        if (nodo == null) return null;
        if (valor < nodo.valor) nodo.izquierdo = deleteRec(nodo.izquierdo, valor);
        else if (valor > nodo.valor) nodo.derecho = deleteRec(nodo.derecho, valor);
        else {
            // caso nodo encontrado
            if (nodo.izquierdo == null) return nodo.derecho;
            if (nodo.derecho == null) return nodo.izquierdo;
            // dos hijos: reemplazar por sucesor inorden
            int minVal = minValue(nodo.derecho);
            nodo.valor = minVal;
            nodo.derecho = deleteRec(nodo.derecho, minVal);
        }
        return nodo;
    }

    private int minValue(NodoArbol nodo) {
        while (nodo.izquierdo != null) nodo = nodo.izquierdo;
        return nodo.valor;
    }

    public List<Integer> inOrder() {
        List<Integer> res = new ArrayList<>();
        inOrderRec(raiz, res);
        return res;
    }

    private void inOrderRec(NodoArbol nodo, List<Integer> res) {
        if (nodo == null) return;
        inOrderRec(nodo.izquierdo, res);
        res.add(nodo.valor);
        inOrderRec(nodo.derecho, res);
    }

    public List<Integer> preOrder() {
        List<Integer> res = new ArrayList<>();
        preOrderRec(raiz, res);
        return res;
    }

    private void preOrderRec(NodoArbol nodo, List<Integer> res) {
        if (nodo == null) return;
        res.add(nodo.valor);
        preOrderRec(nodo.izquierdo, res);
        preOrderRec(nodo.derecho, res);
    }

    public List<Integer> postOrder() {
        List<Integer> res = new ArrayList<>();
        postOrderRec(raiz, res);
        return res;
    }

    private void postOrderRec(NodoArbol nodo, List<Integer> res) {
        if (nodo == null) return;
        postOrderRec(nodo.izquierdo, res);
        postOrderRec(nodo.derecho, res);
        res.add(nodo.valor);
    }

    // Datos para visualización
    public static class NodeData {
        public final int value;
        public final int depth;
        public NodeData(int value, int depth) { this.value = value; this.depth = depth; }
    }

    public static class Edge {
        public final int from;
        public final int to;
        public Edge(int from, int to) { this.from = from; this.to = to; }
    }

    public List<NodeData> nodesWithDepth() {
        List<NodeData> list = new ArrayList<>();
        collectNodes(raiz, 0, list);
        return list;
    }

    public List<Edge> edges() {
        List<Edge> list = new ArrayList<>();
        collectEdges(raiz, list);
        return list;
    }

    private void collectNodes(NodoArbol n, int depth, List<NodeData> out) {
        if (n == null) return;
        out.add(new NodeData(n.valor, depth));
        collectNodes(n.izquierdo, depth + 1, out);
        collectNodes(n.derecho, depth + 1, out);
    }

    private void collectEdges(NodoArbol n, List<Edge> out) {
        if (n == null) return;
        if (n.izquierdo != null) out.add(new Edge(n.valor, n.izquierdo.valor));
        if (n.derecho != null) out.add(new Edge(n.valor, n.derecho.valor));
        collectEdges(n.izquierdo, out);
        collectEdges(n.derecho, out);
    }

    // Ruta seguida al buscar un valor (incluye los nodos visitados)
    public List<Integer> pathTo(int valor) {
        List<Integer> path = new ArrayList<>();
        NodoArbol actual = raiz;
        while (actual != null) {
            path.add(actual.valor);
            if (valor == actual.valor) break;
            actual = valor < actual.valor ? actual.izquierdo : actual.derecho;
        }
        return path;
    }
}