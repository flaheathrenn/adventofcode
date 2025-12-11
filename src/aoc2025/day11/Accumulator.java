package aoc2025.day11;

import java.util.*;

public class Accumulator {
    // State
    Map<String, List<String>> inputs = new HashMap<>();
    Map<String, Integer> pathsTo = new HashMap<>();

    // Update state from parsed line
    public Accumulator update(ParsedLine parsedLine) {
        if (!inputs.containsKey(parsedLine.input)) {
            inputs.put(parsedLine.input, new ArrayList<>());
        }
        for (String output : parsedLine.outputs) {
            if (!inputs.containsKey(output)) {
                inputs.put(output, new ArrayList<>());
            }
            inputs.get(output).add(parsedLine.input);
        }
        return this;
    }

    // Extract solution
    public String star1() {
        System.out.println(inputs);
        pathsTo.put("you", 1); // one path to start node
        while (!pathsTo.containsKey("out")) {
            node: for (Map.Entry<String, List<String>> entry : inputs.entrySet()) {
                if (pathsTo.containsKey(entry.getKey())) {
                    // already calculated paths to here
                    continue;
                }
                int paths = 0;
                for (String input : entry.getValue()) {
                    if (!pathsTo.containsKey(input)) {
                        // still not calculated paths to all inputs
                        continue node;
                    }
                    paths += pathsTo.get(input);
                }
                pathsTo.put(entry.getKey(), paths);
                System.out.println(pathsTo);
            }
        }
        return Integer.toString(pathsTo.get("out"));
    }
}
