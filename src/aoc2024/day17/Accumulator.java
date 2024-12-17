package aoc2024.day17;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Accumulator {
    // State
    String programString;
    int initialRegisterAValue;
    Computer computer = new Computer();

    // Update state from parsed line
    public Accumulator update(ParsedLine parsedLine) {
        if (parsedLine.registerA != null) {
            initialRegisterAValue = parsedLine.registerA;
            computer.registerA = parsedLine.registerA;
        }
        if (parsedLine.registerB != null) {
            computer.registerB = parsedLine.registerB;
        }
        if (parsedLine.registerC != null) {
            computer.registerC = parsedLine.registerC;
        }
        if (parsedLine.programString != null) {
            programString = parsedLine.programString;
            computer.program = Arrays.stream(programString.split(",")).mapToInt(Integer::parseInt).toArray();
        }
        return this;
    }

    // Extract solution
    public String star1() {
        return computer.run();
    }

    public String star1Decompiled(long registerA) {
        // What my input program literally does.
        List<String> outputs = new ArrayList<>();
        long registerB = 0, registerC = 0;
        while (true) {
            registerB = registerA % 8; // i.e. last three binary digits
            registerB = registerB ^ 7; // i.e. 7 - current value
            registerC = registerA / Double.valueOf(Math.pow(2, registerB)).longValue();
            registerB = registerB ^ 7; // return to A % 8
            registerA = registerA / 8; // i.e. lop off the last three digits
            registerB = registerB ^ registerC;
            outputs.add(Long.toString(registerB % 8));
            // so broadly speaking, this is outputting the difference between the last three digits of A
            // and a pseudorandomly chosen prior three digits of A,
            // and then removing the last three digits of A
            if (registerA == 0) {
                break;
            }
        }
        return outputs.stream().collect(Collectors.joining(","));
    }

    public String star2() {
        // Key insight: if initial value A goes to output Y,Z, initial value (A * 8) + B goes to output X,Y,Z
        // i.e. adding three binary digits onto the end only results in a new first character of the output
        // so we start from the last character of the output and work backwards
        String desiredOutput = programString;
        System.out.println("Desired output: " + desiredOutput);
        long valueSoFar = 7; // if desired output ends in 0 we can start with 7
        int outputItemsFound = 1;
        Optional<Long> putativeSolution = matchMore(desiredOutput, outputItemsFound, valueSoFar);
        if (putativeSolution.isPresent()) {
            System.out.println("Does " + putativeSolution.get() + " produce " + desiredOutput + "?");
            System.out.println(star1Decompiled(putativeSolution.get()));
        }
        return putativeSolution.map(String::valueOf).orElse("N/A");
    }

    public Optional<Long> matchMore(String fullOutput, int outputItemsFound, long valueProducing) {
        // We know that running the program using valueProducing produces the last outputItemsFound items of fullOutput
        String nextDesiredOutput = fullOutput.substring(fullOutput.length() - (2 * outputItemsFound + 1), fullOutput.length());
        for (int i = 0; i < 8; i++) {
            long trialValue = valueProducing * 8 + i;
            String output = "";
            try {
                output = star1Decompiled(trialValue);
            } catch (ArithmeticException e) {
                System.out.println("Arithmetic exception on trial value " + trialValue);
            }
            if (output.equals(nextDesiredOutput)) {
                if (nextDesiredOutput.equals(fullOutput)) {
                    // we're done!
                    return Optional.of(trialValue);
                }
                // otherwise recurse back one step
                System.out.println("Found potential match: " + trialValue + " produces " + output);
                Optional<Long> maybeNextMatch = matchMore(fullOutput, outputItemsFound + 1, trialValue);
                if (maybeNextMatch.isPresent()) {
                    return maybeNextMatch;
                }
                // otherwise continue with the loop
            }
        }
        // no next value found
        return Optional.empty();

    }
}
