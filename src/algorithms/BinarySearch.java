package algorithms;

public class BinarySearch {
    // Devuelve índice si encuentra, -1 si no.
    public static int binarySearch(int[] arr, int target) {
        int low = 0, high = arr.length - 1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (arr[mid] == target) return mid;
            if (arr[mid] < target) low = mid + 1; else high = mid - 1;
        }
        return -1;
    }
}