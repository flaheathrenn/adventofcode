package aoc2024.day7;

import java.util.List;

public class Accumulator {
    // State
    long star1solution = 0;
    long star2solution = 0;

    // Update state from parsed line
    public Accumulator update(ParsedLine parsedLine) {
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
            return target == elements.get(0);
        }
        long lastElement = elements.get(elements.size() - 1);
        if (target < lastElement) {
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
            return target == elements.get(0);
        }
        long lastElement = elements.get(elements.size() - 1);
        if (target < lastElement) {
            return false;
        }
        if (star2composable(target - lastElement, listWithoutLastElement(elements))) {
            return true;
        }
        if (target != lastElement && endsWith(target, lastElement) && star2composable(remove(target, lastElement), listWithoutLastElement(elements))) {
            return true;
        }
        if (target % lastElement == 0 && star2composable(target / lastElement, listWithoutLastElement(elements))) {
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
