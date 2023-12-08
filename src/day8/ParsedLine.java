package day8;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParsedLine {

    // State
    String beginning;
    String left;
    String right;

    private static Pattern pattern = Pattern.compile("([A-Z]+) = \\(([A-Z]+), ([A-Z]+)\\)");

    // Parsing
    public ParsedLine(String line) {
        // AAA = (BBB, BBB)
        Matcher matcher = pattern.matcher(line);
        matcher.matches();
        beginning = matcher.group(1);
        left = matcher.group(2);
        right = matcher.group(3);
    }

}