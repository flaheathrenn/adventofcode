package aoc2023.day17;

public class ParsedLine {

    // State
    int[] values;

    // Parsing
    public ParsedLine(String line) {
        String[] characters = line.split("");
        values = new int[line.length()];
        for (int i = 0; i < values.length; i++) {
            values[i] = Integer.parseInt(characters[i]);
        }
    }

}