package day14;

import java.util.ArrayList;
import java.util.List;

import global.ArrayUtils;

public class Accumulator {
    // State
    List<String[]> rows = new ArrayList<>();

    // Update state from parsed line
    public Accumulator update(ParsedLine parsedLine) {
        rows.add(parsedLine.splitLine);
        return this;
    }

    // Extract solution
    public String star1() {
        String[][] pattern = rows.toArray(new String[0][0]);
        pattern = ArrayUtils.swapArrayOrientation(pattern);
        final int rowLength = pattern[0].length;
        int acc = 0;
        for (String[] line : pattern) {
            String[] lineSectionsArray = String.join("", line).split("#");
            for (int i = 0; i < lineSectionsArray.length; i++) {
                int rockCount = 0;
                final String substring = lineSectionsArray[i];
                for (int j = 0; j < substring.length(); j++) {
                    if (substring.charAt(j) == 'O') {
                        rockCount++;
                    }
                }
                lineSectionsArray[i] = "O".repeat(rockCount) + ".".repeat(substring.length() - rockCount);
            }
            String row = String.join("#", lineSectionsArray);
            // System.out.println(row);
            
            for (int i = 0; i < row.length(); i++) {
                if (row.charAt(i) == 'O') {
                    acc += rowLength - i;
                }
            }
        }

        return String.valueOf(acc);
    }
}
