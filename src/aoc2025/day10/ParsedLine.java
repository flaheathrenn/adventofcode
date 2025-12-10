package aoc2025.day10;

public class ParsedLine {
    /*
    Example line:
    [.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}
     */

    // State
    short indicatorLights;
    short[] buttons;
    short[][] buttonsForJoltage;
    short[] joltage;

    // Parsing
    public ParsedLine(String line) {
        String[] splitLine = line.split(" ");
        // indicator lights
        indicatorLights = indicatorLightsToShort(splitLine[0]);
        buttons = new short[splitLine.length - 2];
        buttonsForJoltage = new short[splitLine.length - 2][10];
        for (int i = 1; i < splitLine.length - 1; i++) {
            buttons[i - 1] = buttonToShort(splitLine[i]);
            buttonsForJoltage[i - 1] = buttonToJoltage(splitLine[i]);
        }
        joltage = joltageToShortArray(splitLine[splitLine.length - 1]);
    }

    private short[] buttonToJoltage(String button) {
        String[] buttonNumbers = button.substring(1, button.length() - 1).split(",");
        short[] result = new short[buttonNumbers.length];
        for (int i = 0; i < buttonNumbers.length; i++) {
            result[i] = Short.parseShort(buttonNumbers[i]);
        }
        return result;
    }

    private short[] joltageToShortArray(String joltageString) {
        String[] joltageNumbers = joltageString.substring(1, joltageString.length() - 1).split(",");
        short[] result = new short[joltageNumbers.length];
        for (int i = 0; i < joltageNumbers.length; i++) {
            result[i] = Short.parseShort(joltageNumbers[i]);
        }
        return result;
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