package day8;

import java.util.HashMap;
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
    public String star1(String instructions) {
        int steps = 0;
        String currentValue = "AAA";
        while (!currentValue.equals("ZZZ")) {
            if (instructions.charAt(steps % instructions.length()) == 'L') {
                currentValue = left.get(currentValue);
                System.out.println("Went left, new value is " + currentValue);
            } else {
                currentValue = right.get(currentValue);
                System.out.println("Went right, new value is " + currentValue);
            }
            steps++;
        }
        return String.valueOf(steps);
    }
}
