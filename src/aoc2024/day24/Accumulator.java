package aoc2024.day24;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class Accumulator {
    // State
    Map<String, Boolean> wireValues = new HashMap<>();
    Queue<Gate> gateQueue = new LinkedList<>();
    Map<String, Gate> gatesControllingWire = new HashMap<>();

    // Update state from parsed line
    public Accumulator update(ParsedLine parsedLine) {
        if (parsedLine.isBlank) {
            // no op
        } else if (parsedLine.isInitialValue) {
            wireValues.put(parsedLine.wire, parsedLine.wireValue);
        } else if (parsedLine.isGate) {
            gateQueue.add(parsedLine.gate);
            gatesControllingWire.put(parsedLine.gate.outputWire(), parsedLine.gate);
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

        // List<String> xWires = new ArrayList<>(
        // wireValues.keySet().stream().filter(x ->
        // x.startsWith("x")).sorted().toList());
        // Collections.reverse(xWires);
        // long xValue = 0L;
        // for (String xWire : xWires) {
        // xValue *= 2;
        // if (Boolean.TRUE.equals(wireValues.get(xWire))) {
        // xValue++;
        // }
        // }

        // List<String> yWires = new ArrayList<>(
        // wireValues.keySet().stream().filter(x ->
        // x.startsWith("y")).sorted().toList());
        // Collections.reverse(yWires);
        // long yValue = 0L;
        // for (String yWire : yWires) {
        // yValue *= 2;
        // if (Boolean.TRUE.equals(wireValues.get(yWire))) {
        // yValue++;
        // }
        // }

        // List<String> zWires = new ArrayList<>(
        // wireValues.keySet().stream().filter(x ->
        // x.startsWith("z")).sorted().toList());
        // Collections.reverse(zWires);
        // long solution = 0L;
        // for (String zWire : zWires) {
        // // System.out.println(zWire + ": " + wireValues.get(zWire));
        // solution *= 2;
        // if (Boolean.TRUE.equals(wireValues.get(zWire))) {
        // solution++;
        // }
        // }

        // System.out.println("x value: " + Long.toBinaryString(xValue));
        // System.out.println("y value: " + Long.toBinaryString(yValue));
        // System.out.println("z value: " + Long.toBinaryString(solution));
        // System.out.println("expected z value: " + Long.toBinaryString(xValue +
        // yValue));
        // System.out.println("errors: " + Long.toBinaryString(solution ^ (xValue +
        // yValue)));

        // return Long.toString(solution);

        // Set<Gate> gatesTo44 = gatesToWire("z44");
        // Set<Gate> gatesTo23 = gatesToWire("z23");
        // gatesTo44.retainAll(gatesTo23);
        // System.out.println(gatesTo44);

        List<String> zWires = new ArrayList<>(
                wireValues.keySet().stream().filter(x -> x.startsWith("z")).sorted().toList());
        for (int i = 0; i < 46; i++) {
            String wireNumber = (i < 10 ? "0" : "") + Integer.toString(i);
            Set<String> inputs = ultimateInputs("z" + wireNumber);
            for (int j = 0; j <= i; j++) {
                String subordinateWireNumber = (j < 10 ? "0" : "") + Integer.toString(j);
                if (!inputs.remove("x" + subordinateWireNumber)) {
                    System.out.println("z" + wireNumber + " should depend on x" + subordinateWireNumber + " but doesn't");
                }
                if (!inputs.remove("y" + subordinateWireNumber)) {
                    System.out.println("z" + wireNumber + " should depend on y" + subordinateWireNumber + " but doesn't");
                }
            }
            if (!inputs.isEmpty()) {
                System.out.println("z" + wireNumber + " shouldn't depend on " + inputs + " but does");
            }
        }

        return "";
    }

    private Set<Gate> gatesToWire(String wire) {
        if (!gatesControllingWire.containsKey(wire)) {
            return Collections.emptySet();
        }
        Gate mainGate = gatesControllingWire.get(wire);
        Set<Gate> result = new HashSet<>();
        result.add(mainGate);
        result.addAll(gatesToWire(mainGate.inputWire()));
        result.addAll(gatesToWire(mainGate.inputWire2()));
        return result;
    }

    private Set<String> ultimateInputs(String wire) {
        if (!gatesControllingWire.containsKey(wire)) {
            return Collections.emptySet();
        }
        Set<String> result = new HashSet<>();
        Gate mainGate = gatesControllingWire.get(wire);
        if (mainGate.inputWire().startsWith("x") || mainGate.inputWire().startsWith("y")) {
            result.add(mainGate.inputWire());
        } else {
            result.addAll(ultimateInputs(mainGate.inputWire()));
        }
        if (mainGate.inputWire2().startsWith("x") || mainGate.inputWire2().startsWith("y")) {
            result.add(mainGate.inputWire2());
        } else {
            result.addAll(ultimateInputs(mainGate.inputWire2()));
        }
        return result;
    }
}
