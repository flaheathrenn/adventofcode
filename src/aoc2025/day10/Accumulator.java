package aoc2025.day10;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
//import java.util.stream.Collectors;

public class Accumulator {
    // State
    long star1sol = 0;
    long star2sol = 0;

    // Update state from parsed line
    public Accumulator update(ParsedLine parsedLine) {
        star1sol += calculateStarOneButtonPresses(parsedLine.buttons, parsedLine.indicatorLights);
        star2sol += calculateStarTwoButtonPresses(parsedLine.buttonsForJoltage, parsedLine.joltage);
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

    private long calculateStarTwoButtonPresses(short[][] buttons, short[] joltageGoal) {

        // Initialise ButtonSet
        // Initialise button definitions
        List<ButtonSet.ButtonDefinition> buttonDefinitionsList = new ArrayList<>(buttons.length);
        for (int i = 0; i < buttons.length; i++) {
            buttonDefinitionsList.add(new ButtonSet.ButtonDefinition());
        }
        // Each element in the joltage goal array represents a constraint on buttons
        for (int i = 0; i < joltageGoal.length; i++) {
            // Make list of buttons that affect this particular element
            List<Integer> buttonIndexesForJoltageElement = new ArrayList<>(joltageGoal.length);
            for (int j = 0; j < buttons.length; j++) {
                for (short buttonAffects : buttons[j]) {
                    if (buttonAffects == i) {
                        buttonIndexesForJoltageElement.add(j);
                    }
                }
            }
            // Now add constraints to these buttons
            for (Integer buttonIndex : buttonIndexesForJoltageElement) {
                buttonDefinitionsList.get(buttonIndex).addConstraint(new ButtonSet.ButtonDefinition.Constraint(
                        buttonIndexesForJoltageElement.stream().filter(value -> value != buttonIndex).collect(Collectors.toList()),
                        joltageGoal[i]
                ));
            }
        }

        ButtonSet buttonSet = new ButtonSet(buttonDefinitionsList);
        ButtonSet solution = buttonSet.solve();
        long buttonPresses = 0;
        if (solution == null) {
            System.out.println("No solution found");
        } else {
            System.out.println("Found solution:");
            for (int i = 0; i < solution.buttonDefinitions().size(); i++) {
                System.out.printf("Push button %d %d times%n", i, solution.buttonDefinitions().get(i).fixedValue);
                buttonPresses += solution.buttonDefinitions().get(i).fixedValue;
            }
        }

        return buttonPresses;
    }


    // Extract solution
    public String star1() {
        return Long.toString(star1sol);
    }

    public String star2() {
        return Long.toString(star2sol);
    }
}
