package day15;

public class ParsedLine {

    // State
    long acc = 0;

    // Parsing
    public ParsedLine(String line) {
        for (String step : line.split(",")) {
            acc += hash(step);
        }
    }

    private long hash(String string) {
        long hash = 0L;
        for (int i = 0; i < string.length(); i++) {
            hash = (hash + string.charAt(i)) * 17 % 256;
        }
        return hash;
    }

}