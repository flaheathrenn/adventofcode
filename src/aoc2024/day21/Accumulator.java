package aoc2024.day21;

public class Accumulator {
    // State
    long complexity = 0;

    // Update state from parsed line
    public Accumulator update(ParsedLine parsedLine) {
        System.out.println(parsedLine.numericPart + " * " + (expandString(expandString(parsedLine.line))).length());
        complexity += parsedLine.numericPart * (expandString(expandString(parsedLine.line))).length();
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
        return Long.toString(complexity);
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
                case "v" -> "v<";
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
