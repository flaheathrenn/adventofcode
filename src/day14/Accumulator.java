package day14;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        final String inputPattern = patternToSingleLine(pattern);

        // Run the spin cycle process, filling the memo until we detect a cycle
        Map<String, String> memo = new HashMap<>();
        String oldPattern = inputPattern;
        for (long spins = 0; spins < 100000; spins++) {
            if (memo.containsKey(oldPattern)) {
                System.out.println("Found memo after " + spins + " spins!");
                // We have captured a whole cycle now
                break;
            }

            // Spin cycle
            pattern = ArrayUtils.swapArrayOrientation(pattern); // now north is on left
            tiltLeft(pattern); // tilt to north
            pattern = ArrayUtils.swapArrayOrientation(pattern); // now north is on top
            tiltLeft(pattern); // tilt to west
            pattern = ArrayUtils.swapArrayOrientation(pattern); // now north is on left
            tiltRight(pattern); // tilt to south
            pattern = ArrayUtils.swapArrayOrientation(pattern); // now north is on top
            tiltRight(pattern); // tilt to east

            String patternAsSingleLine = patternToSingleLine(pattern);
            memo.put(oldPattern, patternAsSingleLine);
            oldPattern = patternAsSingleLine;
        }

        // Work out where the cycle starts and its length
        CycleDetector.CycleInformation info = CycleDetector.detectCycleInMemo(memo, inputPattern);

        // What is the 1000000000th entry?
        long afterThisManyCycles = 1000000000;
        long lowestEquivalent = info.cycleStart() + ((afterThisManyCycles - info.cycleStart()) % info.cycleLength());
        System.out.println(lowestEquivalent);
        String result = inputPattern;
        for (int i = 0; i < lowestEquivalent; i++) {
            result = memo.get(result);
        }
        // System.out.println(result);

        String[] finalResultRows = result.split("~");
        // north is on the top

        int acc = 0;
        for (int patternRow = 0; patternRow < finalResultRows.length; patternRow++) {
            String row = finalResultRows[patternRow];
            final int rowLength = row.length();
            for (int j = 0; j < rowLength; j++) {
                if (row.charAt(j) == 'O') {
                    acc += finalResultRows.length - patternRow;
                }
            }
        }

        return String.valueOf(acc);
    }

    private String patternToSingleLine(String[][] pattern) {
        return Arrays.stream(pattern).map(row -> String.join("", row)).collect(Collectors.joining("~"));
    }

    private void tiltLeft(String[][] pattern) {
        tilt(pattern, true);
    }

    private void tiltRight(String[][] pattern) {
        tilt(pattern, false);
    }

    private void tilt(String[][] pattern, boolean left) {
        for (int patternRow = 0; patternRow < pattern.length; patternRow++) {
            String[] lineSectionsArray = String.join("", pattern[patternRow]).split("#", -1);
            for (int i = 0; i < lineSectionsArray.length; i++) {
                int rockCount = 0;
                final String substring = lineSectionsArray[i];
                for (int j = 0; j < substring.length(); j++) {
                    if (substring.charAt(j) == 'O') {
                        rockCount++;
                    }
                }
                if (left) {
                    lineSectionsArray[i] = "O".repeat(rockCount) + ".".repeat(substring.length() - rockCount);
                } else {
                    lineSectionsArray[i] = ".".repeat(substring.length() - rockCount) + "O".repeat(rockCount);

                }
            }
            pattern[patternRow] = String.join("#", lineSectionsArray).split("");
        }
    }
}
