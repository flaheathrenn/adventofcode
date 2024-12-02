package aoc2024.day2;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParsedLine {

    // State
    List<Long> differences = new ArrayList<>();

    // Parsing
    public ParsedLine(String line) {
        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(line);
        Long lastNumber = null;
        while (m.find()) {
            if (lastNumber == null) {
                lastNumber = Long.parseLong(m.group());
                continue;
            }
            long thisNumber = Long.parseLong(m.group());
            differences.add(thisNumber - lastNumber);
            lastNumber = thisNumber;
        } // end of loop
    }

}