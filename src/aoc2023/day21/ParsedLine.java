package aoc2023.day21;

public class ParsedLine {

    // State
    String[] row;
    int sRank;

    // Parsing
    public ParsedLine(String line) {
        row = line.split("");
        sRank = line.indexOf("S");

    }

}