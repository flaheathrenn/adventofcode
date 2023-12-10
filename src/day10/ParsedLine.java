package day10;

public class ParsedLine {

    // State
    String[] items;
    int sIndex;

    // Parsing
    public ParsedLine(String line) {
        this.items = line.split("");
        this.sIndex = line.indexOf("S");
    }

}