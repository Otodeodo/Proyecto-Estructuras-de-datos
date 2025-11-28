package utils;

public class ArrayUtils {
    public static int[] parseIntArray(String input) throws NumberFormatException {
        if (input == null || input.trim().isEmpty()) {
            return new int[0];
        }
        String[] parts = input.split(",");
        java.util.List<Integer> values = new java.util.ArrayList<>();
        for (String part : parts) {
            String p = part.trim();
            if (p.isEmpty()) {
                // Ignorar elementos vacíos provocados por comas dobles o espacios
                continue;
            }
            values.add(Integer.parseInt(p));
        }
        int[] arr = new int[values.size()];
        for (int i = 0; i < values.size(); i++) {
            arr[i] = values.get(i);
        }
        return arr;
    }

    public static String toString(int[] arr) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < arr.length; i++) {
            sb.append(arr[i]);
            if (i < arr.length - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
}