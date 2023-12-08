package day8;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Accumulator {
    // State
    Map<String, String> left = new HashMap<>();
    Map<String, String> right = new HashMap<>();

    // Update state from parsed line
    public Accumulator update(ParsedLine parsedLine) {
        left.put(parsedLine.beginning, parsedLine.left);
        right.put(parsedLine.beginning, parsedLine.right);
        return this;
    }

    // Extract solution
    public String star1(String instructions, String value) {
        int steps = 0;
        String currentValue = value;
        while (!currentValue.endsWith("Z")) {
            if (instructions.charAt(steps % instructions.length()) == 'L') {
                currentValue = left.get(currentValue);
            } else {
                currentValue = right.get(currentValue);
            }
            steps++;
        }
        return String.valueOf(steps);
    }

    public String star2(String instructions) {
        List<String> currentValues = left.keySet().stream().filter(s -> s.endsWith("A")).toList();

        long lcm = 1;
        for (String value : currentValues) {
            int solvedSteps = Integer.parseInt(star1(instructions, value));
            System.out.println("Steps for " + value + " = " + solvedSteps);
            lcm = lcm(lcm, solvedSteps);
        }

        return String.valueOf(lcm);
    }

    // I just copied this from https://www.baeldung.com/java-least-common-multiple, life's too short
    public static long lcm(long number1, long number2) {
        if (number1 == 0 || number2 == 0) {
            return 0;
        }
        long absNumber1 = Math.abs(number1);
        long absNumber2 = Math.abs(number2);
        long absHigherNumber = Math.max(absNumber1, absNumber2);
        long absLowerNumber = Math.min(absNumber1, absNumber2);
        long lcm = absHigherNumber;
        while (lcm % absLowerNumber != 0) {
            lcm += absHigherNumber;
        }
        return lcm;
    }
}
