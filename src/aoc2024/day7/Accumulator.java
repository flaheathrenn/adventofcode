package aoc2024.day7;

import java.util.List;

public class Accumulator {
    // State
    long star1solution = 0;
    long star2solution = 0;

    // Update state from parsed line
    public Accumulator update(ParsedLine parsedLine) {
        // System.out.println("Target " + parsedLine.target + " using numbers " + parsedLine.elements);
        if (star1composable(parsedLine.target, parsedLine.elements)) {
            star1solution += parsedLine.target;
        }
        if (star2composable(parsedLine.target, parsedLine.elements)) {
            star2solution += parsedLine.target;
        }
        return this;
    }

    private boolean star1composable(long target, List<Long> elements) {
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
        if (star1composable(target - lastElement, listWithoutLastElement(elements))) {
            return true;
        }
        if (target % lastElement == 0 && star1composable(target / lastElement, listWithoutLastElement(elements))) {
            return true;
        }
        return false;
    }

    private boolean star2composable(long target, List<Long> elements) {
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
        if (star2composable(target - lastElement, listWithoutLastElement(elements))) {
            return true;
        }
        if (target != lastElement && String.valueOf(target).endsWith(String.valueOf(lastElement)) && star2composable(remove(target, lastElement), listWithoutLastElement(elements))) {
            return true;
        }
        if (target % lastElement == 0 && star2composable(target / lastElement, listWithoutLastElement(elements))) {
            return true;
        }
        return false;
    }

    private long remove(long target, long element) {
        String targetString = String.valueOf(target);
        String elementString = String.valueOf(element);
        return Long.parseLong(targetString.substring(0, targetString.length() - elementString.length()));
    }

    private <T> List<T> listWithoutLastElement(List<T> longList) {
        return longList.subList(0, longList.size() - 1);
    }

    // Extract solution
    public String star1() {
        return String.valueOf(star1solution);
    }

    public String star2() {
        return String.valueOf(star2solution);
    }
}
