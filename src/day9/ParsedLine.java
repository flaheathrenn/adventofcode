package day9;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ParsedLine {

    // State
    List<Integer> integers = new ArrayList<>();

    private static final Pattern INT_PATTERN = Pattern.compile("-?\\d+");

    // Parsing
    public ParsedLine(String line) {
        // For reasons I don't understand, a - right at the start of a line doesn't get matched
        Matcher matcher = INT_PATTERN.matcher(" " + line);
        matcher.matches();
        while (matcher.find()) {
            integers.add(Integer.parseInt(matcher.group()));
        }
        Collections.reverse(integers); // this line is the difference between part 1 and part 2
    }
}