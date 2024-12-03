package aoc2024.day3;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParsedLine {

    // State
    long accumulator = 0;

    // Parsing
    public ParsedLine(String line) {
        Pattern mulPattern = Pattern.compile("mul\\((\\d{1,3}),(\\d{1,3})\\)");
        Matcher m = mulPattern.matcher(line);
        while (m.find()) {
            accumulator += Integer.parseInt(m.group(1)) * Integer.parseInt(m.group(2));
        }
    }
}