package global;

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
}
