package structures;

import java.util.EmptyStackException;

public class Pila<T> {
    private static class Nodo<U> {
        U dato;
        Nodo<U> siguiente;
        Nodo(U d) { dato = d; }
    }

    private Nodo<T> tope;
    private int tamano = 0;

    public void push(T dato) {
        Nodo<T> n = new Nodo<>(dato);
        n.siguiente = tope;
        tope = n;
        tamano++;
    }

    public T pop() {
        if (isEmpty()) throw new EmptyStackException();
        T d = tope.dato;
        tope = tope.siguiente;
        tamano--;
        return d;
    }

    public T peek() {
        if (isEmpty()) throw new EmptyStackException();
        return tope.dato;
    }

    public boolean isEmpty() { return tope == null; }

    public int size() { return tamano; }

    /**
     * Retorna los elementos de la pila en orden tope->base.
     */
    public java.util.List<T> toList() {
        java.util.List<T> list = new java.util.ArrayList<>();
        Nodo<T> tmp = tope;
        while (tmp != null) {
            list.add(tmp.dato);
            tmp = tmp.siguiente;
        }
        return list;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Pila de Tortillas (tope->base): ");
        if (isEmpty()) return sb.append("(vacía)").toString();
        Nodo<T> tmp = tope;
        while (tmp != null) {
            sb.append("[").append(tmp.dato).append("]");
            if (tmp.siguiente != null) sb.append(" -> ");
            tmp = tmp.siguiente;
        }
        return sb.toString();
    }
}