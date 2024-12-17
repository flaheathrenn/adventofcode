package aoc2024.day17;

public class ParsedLine {

    // State
    Integer registerA, registerB, registerC;
    String programString;

    // Parsing
    public ParsedLine(String line) {
        if (line.startsWith("Register A: ")) {
            registerA = Integer.parseInt(line.substring(12));
            return;
        }
        if (line.startsWith("Register B: ")) {
            registerB = Integer.parseInt(line.substring(12));
            return;
        }
        if (line.startsWith("Register C: ")) {
            registerC = Integer.parseInt(line.substring(12));
            return;
        }
        if (line.startsWith("Register A: ")) {
            registerA = Integer.parseInt(line.substring(12));
            return;
        }
        if (line.startsWith("Program: ")) {
            programString = line.substring(9);
            return;
        }
    }

}