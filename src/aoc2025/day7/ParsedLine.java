package aoc2025.day7;

import java.util.ArrayList;
import java.util.List;

public class ParsedLine {

    // State
    int sPosition;
    int[] splitterPositions;

    // Parsing
    public ParsedLine(String line) {
        sPosition = line.indexOf("S");
        List<Integer> splitterPositionsList = new ArrayList<>();
        int nextSplitterPosition = -1;
        do {
            nextSplitterPosition = line.indexOf("^", nextSplitterPosition + 1);
            splitterPositionsList.add(nextSplitterPosition);
        } while (nextSplitterPosition != -1);
        splitterPositions = splitterPositionsList.stream().mapToInt(Integer::intValue).toArray();
    }

}