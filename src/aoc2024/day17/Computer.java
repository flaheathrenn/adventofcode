package aoc2024.day17;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class Computer {
    int registerA, registerB, registerC;
    int instructionPointer = 0;
    int[] program;
    List<String> outputs = new ArrayList<>();

    String run() {
        while (instructionPointer < program.length) {
            int instruction = program[instructionPointer];
            int operand = program[instructionPointer + 1];
            if (!executeInstruction(instruction, operand)) {
                instructionPointer += 2;
            }
        }
        // System.out.println(String.format("State after execution: A=%d B=%d C=%d", registerA, registerB, registerC));
        return outputs.stream().collect(Collectors.joining(","));
    }

    void reinitialise(int registerA) {
        this.registerA = registerA;
        this.registerB = 0;
        this.registerC = 0;
        this.instructionPointer = 0;
        this.outputs = new ArrayList<>();
    }

    /**
     * @return true if jump executed, false otherwise
     */
    private boolean executeInstruction(int instruction, int operand) {
        return switch (instruction) {
            case 0: {
                // adv
                int numerator = registerA;
                int denominator = Double.valueOf(Math.pow(2, comboOperand(operand))).intValue();
                registerA = numerator / denominator;
                yield false;
            }
            case 1: {
                // bxl
                registerB = registerB ^ operand;
                yield false;
            }
            case 2: {
                // bst
                registerB = comboOperand(operand) % 8;
                yield false;
            }
            case 3: {
                // jnz
                if (registerA == 0) {
                    yield false;
                }
                instructionPointer = operand;
                yield true;
            }
            case 4: {
                // bxc
                registerB = registerB ^ registerC;
                yield false;
            }
            case 5: {
                // out
                outputs.add(Integer.toString(comboOperand(operand) % 8));
                yield false;
            }
            case 6: {
                // bdv
                int numerator = registerA;
                int denominator = Double.valueOf(Math.pow(2, comboOperand(operand))).intValue();
                registerB = numerator / denominator;
                yield false;
            }
            case 7: {
                // bdv
                int numerator = registerA;
                int denominator = Double.valueOf(Math.pow(2, comboOperand(operand))).intValue();
                registerC = numerator / denominator;
                yield false;
            }
            default: {
                throw new IllegalArgumentException();
            }
        };
    }

    private int comboOperand(int operand) {
        return switch (operand) {
            case 1, 2, 3 -> operand;
            case 4 -> registerA;
            case 5 -> registerB;
            case 6 -> registerC;
            default -> throw new IllegalArgumentException();
        };
    }
}
