package aoc2025.day10;

import java.util.HashSet;
import java.util.Set;
//import java.util.stream.Collectors;

public class Accumulator {
    // State
    long star1sol = 0;

    // Update state from parsed line
    public Accumulator update(ParsedLine parsedLine) {
        int buttonPresses = 0;
        Set<Short> statesAfterNPresses = Set.of((short) 0);
        Set<Short> allPreviousStates = new HashSet<>(statesAfterNPresses);
        while (!statesAfterNPresses.isEmpty()) {
            buttonPresses++;
            Set<Short> statesAfterNPlusOnePresses = new HashSet<>();
            for (short state : statesAfterNPresses) {
                for (short button : parsedLine.buttons) {
                    statesAfterNPlusOnePresses.add((short) (state ^ button));
                }
            }
            statesAfterNPlusOnePresses.removeAll(allPreviousStates); // repeat states can be ignored
            // System.out.printf("After %s button press(es), new possible light configurations are %s%n", buttonPresses, statesAfterNPlusOnePresses.stream().map(Integer::toBinaryString).collect(Collectors.joining(",", "[", "]")));
            if (statesAfterNPlusOnePresses.contains(parsedLine.indicatorLights)) {
                // System.out.printf("Matched %s in %s button press(es)%n", Integer.toBinaryString(parsedLine.indicatorLights), buttonPresses);
                star1sol += buttonPresses;
                break;
            }
            allPreviousStates.addAll(statesAfterNPlusOnePresses);
            statesAfterNPresses = statesAfterNPlusOnePresses;
        }
        return this;
    }

    // Extract solution
    public String star1() {
        return Long.toString(star1sol);
    }
}
