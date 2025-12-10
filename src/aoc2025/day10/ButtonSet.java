package aoc2025.day10;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record ButtonSet(List<ButtonDefinition> buttonDefinitions) {

    static void main(String[] args) {
        // (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}
        List<ButtonDefinition> buttonDefinitionsList = new ArrayList<>();
        // Add six button definitions to list
        for (int i = 0; i < 6; i++) {
            buttonDefinitionsList.add(new ButtonDefinition());
        }
        // Buttons 4 and 5 must be pressed a total of 3 times
        buttonDefinitionsList.get(4).addConstraint(new ButtonDefinition.Constraint(List.of(5), (short) 3));
        buttonDefinitionsList.get(5).addConstraint(new ButtonDefinition.Constraint(List.of(4), (short) 3));
        // Buttons 1 and 5 must be pressed a total of 5 times
        buttonDefinitionsList.get(1).addConstraint(new ButtonDefinition.Constraint(List.of(5), (short) 5));
        buttonDefinitionsList.get(5).addConstraint(new ButtonDefinition.Constraint(List.of(1), (short) 5));
        // Buttons 2, 3 and 4 must be pressed a total of 4 times
        buttonDefinitionsList.get(2).addConstraint(new ButtonDefinition.Constraint(List.of(3,4), (short) 4));
        buttonDefinitionsList.get(3).addConstraint(new ButtonDefinition.Constraint(List.of(2,4), (short) 4));
        buttonDefinitionsList.get(4).addConstraint(new ButtonDefinition.Constraint(List.of(2,3), (short) 4));
        // Buttons 0, 1 and 3 must be pressed a total of 7 times
        buttonDefinitionsList.get(0).addConstraint(new ButtonDefinition.Constraint(List.of(1,3), (short) 7));
        buttonDefinitionsList.get(1).addConstraint(new ButtonDefinition.Constraint(List.of(0,3), (short) 7));
        buttonDefinitionsList.get(3).addConstraint(new ButtonDefinition.Constraint(List.of(0,1), (short) 7));

        ButtonSet buttonSet = new ButtonSet(buttonDefinitionsList);
        ButtonSet solution = buttonSet.solve();
        if (solution == null) {
            System.out.println("No solution found");
        } else {
            System.out.println("Found solution:");
            for (int i = 0; i < solution.buttonDefinitions.size(); i++) {
                System.out.printf("Push button %d %d times%n", i, solution.buttonDefinitions.get(i).fixedValue);
            }
        }
    }

    public ButtonSet solve() {
        Optional<ButtonDefinition> unfixed = buttonDefinitions.stream().filter(b -> !b.hasFixedValue).findFirst();
        if (unfixed.isEmpty()) {
            // all buttons have fixed values
            return this;
        }
        ButtonDefinition toFix = unfixed.get();
        int firstUnfixedIndex = buttonDefinitions.indexOf(toFix);
        for (short potentialValue = 0; potentialValue <= toFix.maxValue; potentialValue++) {
            ButtonSet fixedButtonSet = this.fixValue(firstUnfixedIndex, potentialValue);
            if (fixedButtonSet == null) {
                continue; // not valid
            }
            fixedButtonSet = fixedButtonSet.solve();
            if (fixedButtonSet == null) {
                continue;
            }
            return fixedButtonSet;
        }
        // no possible solution
        return null;
    }

    public ButtonSet fixValue(int index, short value) {
        List<ButtonDefinition> newButtonDefinitions = new ArrayList<>(buttonDefinitions);
        ButtonDefinition toFix = newButtonDefinitions.get(index);
        if (toFix.hasFixedValue) {
            System.out.printf("Trying to fix button %d with already fixed value %d%n", index, toFix.fixedValue);
            return null; // can't do it
        }
        System.out.printf("Fixing button %d to value %d%n", index, value);
        newButtonDefinitions.set(index, toFix.fixValue(value));
        for (int i = 0; i < newButtonDefinitions.size(); i++) {
            if (i == index) {
                continue;
            }
            ButtonDefinition buttonI = newButtonDefinitions.get(i);
            if (!buttonI.hasFixedValue) {
                ButtonDefinition newButtonI = new ButtonDefinition();
                for (ButtonDefinition.Constraint oldC : buttonI.constraints) {
                    ButtonDefinition.Constraint newC = oldC.fixButton(index, value);
                    if (newC == null) {
                        System.out.printf("Value invalid%n");
                        return null; // can't make this fix
                    }
                    newButtonI.addConstraint(newC);
                }
                newButtonDefinitions.set(i, newButtonI);
            }
        }
        ButtonSet updatedButtonSet = new ButtonSet(newButtonDefinitions);
        for (int i = 0; i < updatedButtonSet.buttonDefinitions.size(); i++) {
            if (i == index) {
                continue;
            }
            ButtonDefinition bd = updatedButtonSet.buttonDefinitions.get(i);
            System.out.printf("Checking button definition index %d def %s%n", index, bd);
            if (bd.hasFixedValue) {
                continue;
            }
            for (ButtonDefinition.Constraint c : bd.constraints) {
                if (c.otherButtonIndexes.isEmpty()) {
                    System.out.printf("Button %d now has fixed value %d, fixing%n", i, c.sum);
                    ButtonSet withNewFixedValue = updatedButtonSet.fixValue(i, c.sum);
                    if (withNewFixedValue == null) {
                        return null; // invalid
                    }
                    updatedButtonSet = withNewFixedValue;
                }
            }
        }
        return updatedButtonSet;
    }

    public static class ButtonDefinition {
        boolean hasFixedValue = false;
        short fixedValue = 0;
        short maxValue = 0;
        List<Constraint> constraints = new ArrayList<>();

        public void addConstraint(Constraint c) {
            this.constraints.add(c);
            if (c.sum > maxValue) {
                maxValue = c.sum;
            }
        }

        public ButtonDefinition fixValue(short value) {
            ButtonDefinition toReturn = new ButtonDefinition();
            toReturn.hasFixedValue = true;
            toReturn.fixedValue = value;
            return toReturn;
        }

        public record Constraint(List<Integer> otherButtonIndexes, short sum) {
            public Constraint fixButton(int index, short value) {
                if (!otherButtonIndexes.contains(index)) {
                    return this; // no change necessary
                }
                if (sum < value) {
                    return null; // invalid
                }
                List<Integer> newOtherButtonIndexes = new ArrayList<>(otherButtonIndexes);
                newOtherButtonIndexes.removeIf(i -> i == index);
                return new Constraint(newOtherButtonIndexes, (short) (sum - value));
            }
        }
    }
}
