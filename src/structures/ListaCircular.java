package structures;

public class ListaCircular<T> {
    private static class Nodo<U> {
        U dato;
        Nodo<U> siguiente;
        Nodo(U d) { dato = d; }
    }

    private Nodo<T> cabeza;
    private int tamano = 0;

    public boolean isEmpty() { return cabeza == null; }
    public int size() { return tamano; }

    public void insert(T dato) {
        Nodo<T> n = new Nodo<>(dato);
        if (cabeza == null) {
            cabeza = n;
            n.siguiente = n;
        } else {
            Nodo<T> temp = cabeza;
            while (temp.siguiente != cabeza) temp = temp.siguiente;
            temp.siguiente = n;
            n.siguiente = cabeza;
        }
        tamano++;
    }

    public boolean delete(T dato) {
        if (cabeza == null) return false;
        Nodo<T> actual = cabeza;
        Nodo<T> previo = null;
        do {
            if ((actual.dato == null && dato == null) || (actual.dato != null && actual.dato.equals(dato))) {
                if (previo == null) { // eliminar cabeza
                    if (actual.siguiente == cabeza) { // único
                        cabeza = null;
                    } else {
                        // encontrar último
                        Nodo<T> temp = cabeza;
                        while (temp.siguiente != cabeza) temp = temp.siguiente;
                        cabeza = actual.siguiente;
                        temp.siguiente = cabeza;
                    }
                } else {
                    previo.siguiente = actual.siguiente;
                }
                tamano--;
                return true;
            }
            previo = actual;
            actual = actual.siguiente;
        } while (actual != cabeza);
        return false;
    }

    public boolean contains(T dato) {
        if (cabeza == null) return false;
        Nodo<T> temp = cabeza;
        do {
            if ((temp.dato == null && dato == null) || (temp.dato != null && temp.dato.equals(dato))) return true;
            temp = temp.siguiente;
        } while (temp != cabeza);
        return false;
    }

    public java.util.List<T> toList() {
        java.util.List<T> list = new java.util.ArrayList<>();
        if (cabeza == null) return list;
        Nodo<T> temp = cabeza;
        do {
            list.add(temp.dato);
            temp = temp.siguiente;
        } while (temp != cabeza);
        return list;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Rueda de Salsas: ");
        if (cabeza == null) return sb.append("(vacía)").toString();
        Nodo<T> temp = cabeza;
        do {
            sb.append("[").append(temp.dato).append("] -> ");
            temp = temp.siguiente;
        } while (temp != cabeza);
        sb.append("(vuelve al inicio)");
        return sb.toString();
    }
}