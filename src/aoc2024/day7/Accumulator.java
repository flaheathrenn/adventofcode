package aoc2024.day7;

import java.util.List;

public class Accumulator {
    // State
    long star1solution = 0;
    long star2solution = 0;

    // Update state from parsed line
    public Accumulator update(ParsedLine parsedLine) {
        if (composable(parsedLine.target, parsedLine.elements, false)) {
            star1solution += parsedLine.target;
        }
        if (composable(parsedLine.target, parsedLine.elements, true)) {
            star2solution += parsedLine.target;
        }
        return this;
    }

    private boolean composable(long target, List<Long> elements, boolean isStar2) {
        if (elements.size() == 1) {
            return target == elements.get(0);
        }
        long lastElement = elements.get(elements.size() - 1);
        if (target < lastElement) {
            return false;
        }
        if (composable(target - lastElement, listWithoutLastElement(elements), isStar2)) {
            return true;
        }
        if (isStar2 && endsWith(target, lastElement) && composable(remove(target, lastElement), listWithoutLastElement(elements), isStar2)) {
            return true;
        }
        if (target % lastElement == 0 && composable(target / lastElement, listWithoutLastElement(elements), isStar2)) {
            return true;
        }
        return false;
    }

    private boolean endsWith(long target, long element) {
        return target % Math.pow(10, Math.round(Math.ceil(Math.log10(element)))) == element;
    }

    private long remove(long target, long element) {
        return (target - element) / Math.round(Math.pow(10, Math.ceil(Math.log10(element))));
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
