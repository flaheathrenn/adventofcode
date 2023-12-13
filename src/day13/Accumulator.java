package day13;

import java.util.Arrays;

public class Accumulator {
    // State
    long accStar1 = 0L;
    long accStar2 = 0L;
    int currentPatternLine = 0;
    String[][] currentPattern;

    // Update state from parsed line
    public Accumulator update(ParsedLine parsedLine) {
        if (parsedLine.empty) {
            String[][] trimmedCurrentPattern = new String[currentPatternLine][currentPattern[0].length];
            System.arraycopy(currentPattern, 0, trimmedCurrentPattern, 0, currentPatternLine);
            accStar1 += processPattern(trimmedCurrentPattern, 0L); // Star 1
            accStar2 += processPatternStar2(trimmedCurrentPattern); // Star 2 result
            currentPattern = null;
            currentPatternLine = 0;
        } else if (currentPattern == null) {
            currentPattern = new String[20][parsedLine.splitLine.length];
            currentPatternLine = 0;
            currentPattern[0] = parsedLine.splitLine;
            currentPatternLine++;
        } else {
            currentPattern[currentPatternLine++] = parsedLine.splitLine;
        }
        return this;
    }

    private long processPatternStar2(String[][] pattern) {
        long initialResult = processPattern(pattern, 0L);
        for (int i = 0; i < pattern.length; i++) {
            for (int j = 0; j < pattern[i].length; j++) {
                // treat as smudge and flip
                if (pattern[i][j].equals("#")) {
                    pattern[i][j] = ".";
                } else {
                    pattern[i][j] = "#";
                }

                long newResult = processPattern(pattern, initialResult);
                if (newResult != 0 && newResult != initialResult) {
                    // System.out.println("Found smudge at (" + (i+1) + "," + (j+1) + "), new result " + newResult);
                    return newResult;
                }

                // flip back
                if (pattern[i][j].equals("#")) {
                    pattern[i][j] = ".";
                } else {
                    pattern[i][j] = "#";
                }
            }
        }
        System.out.println("Couldn't find smudge");
        return 0L;
    }

    private long processPattern(String[][] pattern, long exclude) {

        // Print memory contents for debugging purposes
        // for (int i = 0; i < pattern.length; i++) {
        //     for (int j = 0; j < pattern[i].length; j++) {
        //         System.out.print(pattern[i][j]);
        //     }
        //     System.out.println();
        // }
        // System.out.println();

        // If two consecutive horizontal lines are identical, check for a reflection
        // i is zero-indexed but actual line numbers are one-indexed
        for (int i = 0; i < pattern.length - 1; i++) {
            if (Arrays.equals(pattern[i], pattern[i + 1])) {
                // System.out.println("Horizontal lines " + (i + 1) + " and " + (i + 2) + " match, checking for reflection...");
                if (checkReflection(pattern, i)) {
                    // System.out.println("Horizontal reflection found after line " + (i + 1));
                    if ((i + 1) * 100 == exclude) {
                        continue;
                    }
                    return (i + 1) * 100; // times 100 for horizontal
                }
            }
        }

        pattern = swapArrayOrientation(pattern);

        // Now the same process again will find a vertical reflection
        for (int i = 0; i < pattern.length - 1; i++) {
            if (Arrays.equals(pattern[i], pattern[i + 1])) {
                // System.out.println("Vertical lines " + (i + 1) + " and " + (i + 2) + " match, checking for reflection...");
                if (checkReflection(pattern, i)) {
                    // System.out.println("Vertical reflection found after line " + (i + 1));
                    if (i + 1 == exclude) {
                        continue;
                    }
                    return (i + 1);
                }
            }
        }
        // System.out.println("No reflection found");
        return 0;
    }

    /**
     * Check if a pattern has a horizontal line of reflection
     * 
     * @param pattern   pattern to check
     * @param rowsAbove int number of rows to go down before encountering a
     *                  horizontal line of reflection
     * @return true if a line of reflection exists there, false otherwise
     */
    private boolean checkReflection(String[][] pattern, int rowsAbove) {
        // Precondition: we know already that lines (rowsAbove) and (rowsAbove+1) are
        // identical
        int topLineIndex = rowsAbove - 1;
        int bottomLineIndex = rowsAbove + 2;
        while (topLineIndex > -1 && bottomLineIndex < pattern.length) {
            if (!Arrays.equals(pattern[topLineIndex], pattern[bottomLineIndex])) {
                // note that our array indices are zero-indexed but actual output should be
                // one-indexed
                // System.out.println(
                //         "Line " + (topLineIndex + 1) + " doesn't match line " + (bottomLineIndex + 1) + ", aborting");
                return false;
            }
            topLineIndex--;
            bottomLineIndex++;
        }
        return true;
    }

    /**
     * Convert input's horizontal lines to vertical lines and vice versa
     */
    private String[][] swapArrayOrientation(String[][] input) {
        // Precondition: input[0] exists
        String[][] result = new String[input[0].length][input.length];
        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input[0].length; j++) {
                result[j][i] = input[i][j];
            }
        }
        return result;
    }

    // Extract solution
    public String star1() {
        return String.valueOf(accStar1);
    }

    public String star2() {
        return String.valueOf(accStar2);
    }
}
