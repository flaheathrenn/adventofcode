package day16;

public class ParsedLine {

    // State
    String[] row;

    // Parsing
    public ParsedLine(String line) {
        row = line.split("");
    }

}