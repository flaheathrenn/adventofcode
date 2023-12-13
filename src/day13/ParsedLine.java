package day13;

public class ParsedLine {

    // State
    String[] splitLine;
    boolean empty;

    // Parsing
    public ParsedLine(String line) {
        empty = line.isBlank();
        if (!empty) {
            splitLine = line.split("");
        }
    }

}