package aoc2024.day24;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class Accumulator {
    // State
    Map<String, Boolean> wireValues = new HashMap<>();
    Queue<Gate> gateQueue = new LinkedList<>();

    // Update state from parsed line
    public Accumulator update(ParsedLine parsedLine) {
        if (parsedLine.isBlank) {
            // no op
        } else if (parsedLine.isInitialValue) {
            wireValues.put(parsedLine.wire, parsedLine.wireValue);
        } else if (parsedLine.isGate) {
            gateQueue.add(parsedLine.gate);
        }
        return this;
    }

    // Extract solution
    public String star1() {
        while (!gateQueue.isEmpty()) {
            Gate gate = gateQueue.poll();
            if (!gate.updateMapForGate(wireValues)) {
                gateQueue.add(gate); // put back on at the end
            }
        }

        // System.out.println(wireValues);

        List<String> zWires = new ArrayList<>(wireValues.keySet().stream().filter(x -> x.startsWith("z")).sorted().toList());
        Collections.reverse(zWires);
        long solution = 0L;
        for (String zWire : zWires) {
            // System.out.println(zWire + ": " + wireValues.get(zWire));
            solution *= 2;
            if (Boolean.TRUE.equals(wireValues.get(zWire))) {
                solution++;
            }
        }
        return Long.toString(solution);
    }
}
