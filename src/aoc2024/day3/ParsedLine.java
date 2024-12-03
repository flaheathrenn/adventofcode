package aoc2024.day3;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParsedLine {

    // State
    long accumulator = 0;
    boolean enabled = true;

    // Parsing
    public ParsedLine(String line) {
        Pattern mulPattern = Pattern.compile("mul\\((\\d{1,3}),(\\d{1,3})\\)|do\\(\\)|don't\\(\\)");
        Matcher m = mulPattern.matcher(line);
        while (m.find()) {
            if (m.group(0).equals("do()")) {
                enabled = true;
            } else if (m.group(0).equals("don't()")) {
                enabled = false;
            } else if (enabled) {
                accumulator += Integer.parseInt(m.group(1)) * Integer.parseInt(m.group(2));
            }
        }
    }
}