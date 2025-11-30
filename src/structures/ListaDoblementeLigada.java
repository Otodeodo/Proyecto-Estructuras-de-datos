package structures;

import java.util.ArrayList;
import java.util.List;

public class ListaDoblementeLigada<T> {
    private static class Nodo<U> {
        U dato;
        Nodo<U> siguiente;
        Nodo<U> anterior;

        Nodo(U d) {
            dato = d;
        }
    }

    private Nodo<T> cabeza;
    private Nodo<T> cola;
    private int tamano = 0;

    public boolean isEmpty() {
        return cabeza == null;
    }

    public int size() {
        return tamano;
    }

    public void insert(T dato) {
        Nodo<T> n = new Nodo<>(dato);
        if (cabeza == null) {
            cabeza = cola = n;
        } else {
            cola.siguiente = n;
            n.anterior = cola;
            cola = n;
        }
        tamano++;
    }

    public boolean delete(T dato) {
        if (cabeza == null)
            return false;

        Nodo<T> actual = cabeza;
        while (actual != null) {
            if ((actual.dato == null && dato == null) || (actual.dato != null && actual.dato.equals(dato))) {
                if (actual == cabeza) {
                    cabeza = actual.siguiente;
                    if (cabeza != null)
                        cabeza.anterior = null;
                    else
                        cola = null;
                } else if (actual == cola) {
                    cola = actual.anterior;
                    if (cola != null)
                        cola.siguiente = null;
                    else
                        cabeza = null;
                } else {
                    actual.anterior.siguiente = actual.siguiente;
                    actual.siguiente.anterior = actual.anterior;
                }
                tamano--;
                return true;
            }
            actual = actual.siguiente;
        }
        return false;
    }

    public boolean contains(T dato) {
        return indexOf(dato) != -1;
    }

    public int indexOf(T dato) {
        Nodo<T> actual = cabeza;
        int index = 0;
        while (actual != null) {
            if ((actual.dato == null && dato == null) || (actual.dato != null && actual.dato.equals(dato))) {
                return index;
            }
            actual = actual.siguiente;
            index++;
        }
        return -1;
    }

    public List<T> toList() {
        List<T> list = new ArrayList<>();
        Nodo<T> actual = cabeza;
        while (actual != null) {
            list.add(actual.dato);
            actual = actual.siguiente;
        }
        return list;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Lista Doble: ");
        if (cabeza == null)
            return sb.append("(vacía)").toString();
        Nodo<T> actual = cabeza;
        while (actual != null) {
            sb.append("[").append(actual.dato).append("] <-> ");
            actual = actual.siguiente;
        }
        sb.append("null");
        return sb.toString();
    }
}
