package aoc2025.day10;

import java.util.*;
//import java.util.stream.Collectors;

public class Accumulator {
    // State
    long star1sol = 0;
    long star2sol = 0;

    // Update state from parsed line
    public Accumulator update(ParsedLine parsedLine) {
        star1sol += calculateStarOneButtonPresses(parsedLine.buttons, parsedLine.indicatorLights);
        star2sol += calculateStarTwoButtonPresses(parsedLine.joltageButtons, parsedLine.joltageGoal);
        return this;
    }

    private long calculateStarOneButtonPresses(short[] buttons, short indicatorLights) {
        int buttonPresses = 0;
        Set<Short> statesAfterNPresses = Set.of((short) 0);
        Set<Short> allPreviousStates = new HashSet<>(statesAfterNPresses);
        while (!statesAfterNPresses.isEmpty()) {
            buttonPresses++;
            Set<Short> statesAfterNPlusOnePresses = new HashSet<>();
            for (short state : statesAfterNPresses) {
                for (short button : buttons) {
                    statesAfterNPlusOnePresses.add((short) (state ^ button));
                }
            }
            statesAfterNPlusOnePresses.removeAll(allPreviousStates); // repeat states can be ignored
            // System.out.printf("After %s button press(es), new possible light configurations are %s%n", buttonPresses, statesAfterNPlusOnePresses.stream().map(Integer::toBinaryString).collect(Collectors.joining(",", "[", "]")));
            if (statesAfterNPlusOnePresses.contains(indicatorLights)) {
                // System.out.printf("Matched %s in %s button press(es)%n", Integer.toBinaryString(parsedLine.indicatorLights), buttonPresses);
                return buttonPresses;
            }
            allPreviousStates.addAll(statesAfterNPlusOnePresses);
            statesAfterNPresses = statesAfterNPlusOnePresses;
        }
        throw new IllegalStateException();
    }

    private long calculateStarTwoButtonPresses(Vector[] joltageButtons, Vector joltageGoal) {
//        SortedSet<Vector> buttons = new TreeSet<>(List.of(joltageButtons));
//        long result = calculateStarTwoButtonPresses(buttons, joltageGoal);
//        System.out.println("Star 2 result: " + result);
//        return result;

        return 0L;
    }

    private static long calculateStarTwoButtonPresses(SortedSet<Vector> parts, Vector goal) {
        if (goal.isZeroVector()) {
            return 0L; // no more button presses required
        }
        if (parts.isEmpty()) {
            return Integer.MAX_VALUE;
        }

        Vector totalParts = parts.stream().reduce(new Vector(new int[goal.elements().length]), Vector::add);
        for (int i = 0; i < totalParts.elements().length; i++) {
            if (totalParts.elements()[i] == 1) {
                // only one part-vector contributes to this part of joltage
                int finalI = i;
                Optional<Vector> contributingPart = parts.stream().filter(part -> part.elements()[finalI] == 1).findFirst();
                return contributingPart.map(part -> {
                    Vector.VectorDivisionResult divisionResult = goal.divideBy(part);
                    SortedSet<Vector> otherParts = new TreeSet<>(parts);
                    otherParts.remove(part);
                    return divisionResult.result() + calculateStarTwoButtonPresses(otherParts, divisionResult.remainder());
                }).orElse((long) Integer.MAX_VALUE);
            }
        }

        Vector widestVector = parts.removeLast();
        Vector.VectorDivisionResult divisionResult = goal.divideBy(widestVector);
        if (divisionResult.result() == 0) {
            return calculateStarTwoButtonPresses(parts, goal);
        }
        long minimum = Integer.MAX_VALUE;
        for (int i = 0; i <= divisionResult.result(); i++) {
            long thisAnswer = i + calculateStarTwoButtonPresses(new TreeSet<>(parts), goal.subtract(widestVector.times(i)));
            minimum = Long.min(minimum, thisAnswer);
        }
        return minimum;
    }


    // Extract solution
    public String star1() {
        return Long.toString(star1sol);
    }

    public String star2() {
        return Long.toString(star2sol);
    }
}
