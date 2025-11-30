package algorithms;

public class InsertionSort {
    public static int[] insertionSort(int[] input) {
        if (input == null)
            return new int[0];
        int[] arr = input.clone();
        int n = arr.length;
        for (int i = 1; i < n; ++i) {
            int key = arr[i];
            int j = i - 1;

            /*
             * Mover los elementos del arreglo[0..i-1], que son
             * mayores que key, a una posición adelante de su
             * posición actual
             */
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j = j - 1;
            }
            arr[j + 1] = key;
        }
        return arr;
    }
}
