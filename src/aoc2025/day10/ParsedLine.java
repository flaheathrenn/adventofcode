package aoc2025.day10;

public class ParsedLine {
    /*
    Example line:
    [.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}
     */

    // State
    short indicatorLights;
    short[] buttons;

    Vector joltageGoal;
    Vector[] joltageButtons;

    // Parsing
    public ParsedLine(String line) {
        String[] splitLine = line.split(" ");
        // indicator lights
        indicatorLights = indicatorLightsToShort(splitLine[0]);
        joltageGoal = joltageAsVector(splitLine[splitLine.length - 1]);
        buttons = new short[splitLine.length - 2];
        joltageButtons = new Vector[splitLine.length - 2];
        for (int i = 1; i < splitLine.length - 1; i++) {
            buttons[i - 1] = buttonToShort(splitLine[i]);
            joltageButtons[i - 1] = buttonAsVector(splitLine[i], joltageGoal.elements().length);
        }
    }

    private Vector joltageAsVector(String s) {
        String[] splits = s.substring(1, s.length() - 1).split(",");
        int[] vectorBacking = new int[splits.length];
        for (int i = 0; i < splits.length; i++) {
            vectorBacking[i] = Integer.parseInt(splits[i]);
        }
        return new Vector(vectorBacking);
    }

    private Vector buttonAsVector(String s, int vectorLength) {
        String[] splits = s.substring(1, s.length() - 1).split(",");
        int[] vectorBacking = new int[vectorLength];
        for (String index : splits) {
            vectorBacking[Integer.parseInt(index)] = 1;
        }
        return new Vector(vectorBacking);
    }

    private short buttonToShort(String button) {
        short result = 0;
        for (String s : button.split("[(),]")) {
            if (s.isBlank()) {
                continue;
            }
            result |= (short) (1 << Integer.parseInt(s));
        }
        // System.out.printf("Mapped %s to %s%n", button, Integer.toBinaryString(result));
        return result;
    }

    private short indicatorLightsToShort(String indicatorString) {
        String removeBrackets = indicatorString.substring(1, indicatorString.length() - 1);
        short result = 0;
        for (int i = 0; i < removeBrackets.length(); i++) {
            if (removeBrackets.charAt(i) == '#') {
                result |= (short) (1 << i);
            }
        }
        // System.out.printf("Mapped %s to %s%n", indicatorString, Integer.toBinaryString(result));
        return result;
    }

}