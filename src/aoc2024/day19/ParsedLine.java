package aoc2024.day19;

public class ParsedLine {

    // State
    boolean skip;
    String[] existingTowels;
    String design;

    // Parsing
    public ParsedLine(String line) {
        if (line.isEmpty()) {
            this.skip = true;
        } else if (line.contains(",")) {
            this.existingTowels = line.split(", ");
        } else {
            this.design = line;
        }
    }

}