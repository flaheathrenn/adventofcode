package aoc2023.day11;

import java.util.ArrayList;
import java.util.List;

public class ParsedLine {

    // State
    List<Integer> galaxyIndexes = new ArrayList<>();

    // Parsing
    public ParsedLine(String line) {
        int index = line.indexOf("#");
        while (index != -1) {
            galaxyIndexes.add(index);
            index = line.indexOf("#", index + 1);
        }
    }

}