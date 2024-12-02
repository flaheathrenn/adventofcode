package aoc2024.day2;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParsedLine {

    // State
    boolean recordSafe = false;

    // Parsing
    public ParsedLine(String line) {
        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(line);
        Long lastNumber = null;
        Boolean isIncreasing = null;
        while (m.find()) {
            if (lastNumber == null) {
                lastNumber = Long.parseLong(m.group());
                continue;
            }
            long thisNumber = Long.parseLong(m.group());
            if (thisNumber == lastNumber) { // record unsafe
                return;
            }
            if (isIncreasing == null) {
                isIncreasing = thisNumber > lastNumber;
            } else {
                if (isIncreasing != thisNumber > lastNumber) { // record unsafe
                    return;
                }
            }
            long diff = Math.abs(thisNumber - lastNumber);
            if (diff > 3) { // record unsafe
                return;
            }
            lastNumber = thisNumber;
        } // end of loop
        recordSafe = true;
    }

}