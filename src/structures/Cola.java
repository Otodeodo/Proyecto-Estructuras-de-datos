package structures;

import java.util.NoSuchElementException;

public class Cola<T> {
    private static class Nodo<U> {
        U dato;
        Nodo<U> siguiente;
        Nodo(U d) { dato = d; }
    }

    private Nodo<T> frente;
    private Nodo<T> fin;
    private int tamano = 0;

    public void enqueue(T dato) {
        Nodo<T> n = new Nodo<>(dato);
        if (fin == null) {
            frente = fin = n;
        } else {
            fin.siguiente = n;
            fin = n;
        }
        tamano++;
    }

    public T dequeue() {
        if (isEmpty()) throw new NoSuchElementException("Cola vacía");
        T d = frente.dato;
        frente = frente.siguiente;
        if (frente == null) fin = null;
        tamano--;
        return d;
    }

    public T front() {
        if (isEmpty()) throw new NoSuchElementException("Cola vacía");
        return frente.dato;
    }

    public boolean isEmpty() { return frente == null; }
    public int size() { return tamano; }

    /**
     * Retorna los elementos de la cola en orden frente->fin.
     */
    public java.util.List<T> toList() {
        java.util.List<T> list = new java.util.ArrayList<>();
        Nodo<T> tmp = frente;
        while (tmp != null) {
            list.add(tmp.dato);
            tmp = tmp.siguiente;
        }
        return list;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Fila de Clientes (frente->fin): ");
        if (isEmpty()) return sb.append("(vacía)").toString();
        Nodo<T> tmp = frente;
        while (tmp != null) {
            sb.append("[").append(tmp.dato).append("]");
            if (tmp.siguiente != null) sb.append(" -> ");
            tmp = tmp.siguiente;
        }
        return sb.toString();
    }
}