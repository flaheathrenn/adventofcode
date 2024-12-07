package aoc2024.day7;

import java.util.List;

public class Accumulator {
    // State
    long star1solution = 0;

    // Update state from parsed line
    public Accumulator update(ParsedLine parsedLine) {
        // System.out.println("Target " + parsedLine.target + " using numbers " + parsedLine.elements);
        if (composable(parsedLine.target, parsedLine.elements)) {
            star1solution += parsedLine.target;
        }
        return this;
    }

    private boolean composable(long target, List<Long> elements) {
        if (elements.size() == 1) {
            // System.out.println("Base case: does " + target + " equal only element " + elements.get(0) + "?");
            // System.out.println((target == elements.get(0)) ? "YES!" : "NO!");
            return target == elements.get(0);
        }
        long lastElement = elements.get(elements.size() - 1);
        if (target < lastElement) {
            // System.out.println("Base case: " + target + " is less than last element " + elements.get(0) + ", so FALSE");
            return false;
        }
        if (composable(target - lastElement, elements.subList(0, elements.size() - 1))) {
            return true;
        }
        if (target % lastElement != 0) { // last operation can't be *
            return false;
        }
        return composable(target / lastElement, elements.subList(0, elements.size() - 1));
    }

    // Extract solution
    public String star1() {
        return String.valueOf(star1solution);
    }
}
