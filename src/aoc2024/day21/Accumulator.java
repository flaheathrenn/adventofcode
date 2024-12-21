package aoc2024.day21;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Accumulator {
    private static int ITERATIONS_STAR1 = 2;
    private static int ITERATIONS_STAR2 = 25;

    // State
    long complexity_star1 = 0;
    long complexity_star2 = 0;

    Map<String, Map<Integer, Long>> memo = new HashMap<>();

    // Update state from parsed line
    public Accumulator update(ParsedLine parsedLine) {
        String line = parsedLine.line;

        for (int i = 0; i < ITERATIONS_STAR1; i++) {
            line = expandString(line);
        }

        complexity_star1 += (long) parsedLine.numericPart * line.length();
        complexity_star2 += (long) parsedLine.numericPart * lengthAfterExpansions(parsedLine.line, ITERATIONS_STAR2);
        return this;
    }

    // Extract solution
    /**
     * Lots of manual preprocessing here: for each of the five numeric sequences I did the
     * conversion to the first directional sequence by hand (annotating the input with it),
     * and rejigged the sequences for which there were options until I got a minimal output.
     * Sorry!
     */
    public String star1() {
        return Long.toString(complexity_star1);
    }

    public String star2() {
        return Long.toString(complexity_star2);
    }

    private long lengthAfterExpansions(String string, int expansions) {
        if (expansions == 0) {
            return (long) string.length();
        }
        if (memo.containsKey(string) && memo.get(string).containsKey(expansions)) {
            return memo.get(string).get(expansions);
        }
        Pattern p = Pattern.compile("[^A]*A");
        Matcher m = p.matcher(expandString(string));
        long result = 0;
        while (m.find()) {
            String subunit = m.group();
            result += lengthAfterExpansions(subunit, expansions - 1);
        }
        if (!memo.containsKey(string)) {
            memo.put(string, new HashMap<>());
        }
        memo.get(string).put(expansions, result);
        return result;
    }

    private String expandString(String string) {
        StringBuilder result = new StringBuilder();
        String currentLocation = "A";
        for (String input : string.split("")) {
            result.append(directionalKeypadTransition(currentLocation, input));
            result.append("A");
            currentLocation = input;
        }
        return result.toString();
    }

    private String directionalKeypadTransition(String start, String end) {
        return switch (start) {
            case "A" -> switch (end) {
                case "<" -> "v<<";
                case "v" -> "<v";
                case ">" -> "v";
                case "^" -> "<";
                case "A" -> "";
                default -> throw new IllegalArgumentException();
            };
            case "^" -> switch (end) {
                case "<" -> "v<";
                case "v" -> "v";
                case ">" -> "v>";
                case "^" -> "";
                case "A" -> ">";
                default -> throw new IllegalArgumentException();
            };
            case ">" -> switch (end) {
                case "<" -> "<<";
                case "v" -> "<";
                case ">" -> "";
                case "^" -> "<^";
                case "A" -> "^";
                default -> throw new IllegalArgumentException();
            };
            case "v" -> switch (end) {
                case "<" -> "<";
                case "v" -> "";
                case ">" -> ">";
                case "^" -> "^";
                case "A" -> "^>";
                default -> throw new IllegalArgumentException();
            };
            case "<" -> switch (end) {
                case "<" -> "";
                case "v" -> ">";
                case ">" -> ">>";
                case "^" -> ">^";
                case "A" -> ">>^";
                default -> throw new IllegalArgumentException();
            };
            default -> throw new IllegalArgumentException();
        };
    }
}
