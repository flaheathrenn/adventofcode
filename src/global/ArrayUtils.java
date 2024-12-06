package global;

import java.util.Arrays;

public interface ArrayUtils {
    /**
     * Convert input's horizontal lines to vertical lines and vice versa
     */
    public static String[][] swapArrayOrientation(String[][] input) {
        // Precondition: input[0] exists
        String[][] result = new String[input[0].length][input.length];
        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input[0].length; j++) {
                result[j][i] = input[i][j];
            }
        }
        return result;
    }

    public static <T> T[][] deepCopyOf(T[][] original) {
        T[][] copy = Arrays.copyOf(original, original.length);
        for (int i = 0; i < copy.length; i++) {
            copy[i] = Arrays.copyOf(original[i], original[i].length);
        }
        return copy;
    }
}
