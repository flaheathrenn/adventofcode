package aoc2025.day10;

import java.awt.*;
import java.util.*;
import java.util.List;

public record ButtonSet(List<ButtonDefinition> initialButtonDefinitions, List<ButtonDefinition> buttonDefinitions) {

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

        ButtonSet buttonSet = new ButtonSet(buttonDefinitionsList, buttonDefinitionsList);
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
            // all buttons have fixed values, so check solution
            for (int index = 0; index < initialButtonDefinitions.size(); index++) {
                short valueAtIndex = buttonDefinitions.get(index).fixedValue;
                for (ButtonDefinition.Constraint initialConstraint : initialButtonDefinitions.get(index).constraints) {
                    short sumOfConstraintValues = valueAtIndex;
                    for (int referencedIndex : initialConstraint.otherButtonIndexes) {
                        sumOfConstraintValues += buttonDefinitions.get(referencedIndex).fixedValue;
                    }
                    if (initialConstraint.sum != sumOfConstraintValues) {
                        return null; // values don't meet constraint
                    }
                }
            }
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
            throw new IllegalArgumentException(); // shouldn't be trying this
        }
        // System.out.printf("Fixing button %d to value %d%n", index, value);
        newButtonDefinitions.set(index, toFix.fixValue(value));

        ButtonSet updatedButtonSet = new ButtonSet(buttonDefinitions, newButtonDefinitions);

        List<Integer> allAffectedIndices = toFix.constraints.stream().map(ButtonDefinition.Constraint::otherButtonIndexes).flatMap(List::stream).toList();
        for (Integer affectedIndex : allAffectedIndices) {
            ButtonDefinition toFixAffected = newButtonDefinitions.get(affectedIndex);
            ButtonDefinition newAffected = new ButtonDefinition();
            for (ButtonDefinition.Constraint oldConstraint : toFixAffected.constraints) {
                ButtonDefinition.Constraint newConstraint = oldConstraint.fixButton(index, value);
                if (newConstraint == null) {
                    return null; // can't do it
                }
                newAffected.addConstraint(newConstraint);
            }
            newButtonDefinitions.set(affectedIndex, newAffected);
            updatedButtonSet = new ButtonSet(buttonDefinitions, newButtonDefinitions);
            List<ButtonDefinition.Constraint> emptyConstraints = newAffected.constraints.stream().filter(c -> c.otherButtonIndexes.isEmpty()).toList();
            if (!emptyConstraints.isEmpty()) {
                // can fix value
                short newValue = emptyConstraints.getFirst().sum;
                if (emptyConstraints.stream().map(ButtonDefinition.Constraint::sum).anyMatch(s -> newValue != s)) {
                    return null; // invalid constraints
                }
                updatedButtonSet = updatedButtonSet.fixValue(affectedIndex, newValue);
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
