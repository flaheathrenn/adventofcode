package aoc2024.day24;

import aoc2024.day24.Gate.GateType;

public class ParsedLine {

    // State
    boolean isInitialValue = false;
    boolean isBlank = false;
    boolean isGate = false;

    String wire;
    boolean wireValue;

    Gate gate;

    // Parsing
    public ParsedLine(String line) {
        if (line.isEmpty()) {
            isBlank = true;
            return;
        }

        if (line.contains(":")) {
            isInitialValue = true;
            String[] splitLine = line.split(": ");
            wire = splitLine[0];
            wireValue = splitLine[1].equals("1");
            return;
        }

        if (line.contains("->")) {
            isGate = true;
            String[] splitLine = line.split(" ");
            gate = new Gate(splitLine[0], splitLine[2], GateType.valueOf(splitLine[1]), splitLine[4]);
            // ntg XOR fgs -> mjb
        }
    }

}