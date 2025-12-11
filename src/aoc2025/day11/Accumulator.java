package aoc2025.day11;

import java.util.*;

public class Accumulator {
    // State
    Map<String, List<String>> inputs = new HashMap<>();

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

    private Long pathsFromTo(String start, String end) {
        Map<String, Long> pathsTo = new HashMap<>();
        pathsTo.put(start, 1L); // one path to start node
        while (!pathsTo.containsKey(end)) {
            node: for (Map.Entry<String, List<String>> entry : inputs.entrySet()) {
                if (pathsTo.containsKey(entry.getKey())) {
                    // already calculated paths to here
                    continue;
                }
                long paths = 0;
                for (String input : entry.getValue()) {
                    if (!pathsTo.containsKey(input)) {
                        // still not calculated paths to all inputs
                        continue node;
                    }
                    paths += pathsTo.get(input);
                }
                pathsTo.put(entry.getKey(), paths);
            }
        }
        return pathsTo.get(end);
    }

    // Extract solution
    public String star1() {
        return Long.toString(pathsFromTo("you", "out"));
    }

    // Extract solution
    public String star2() {
        // I established that the path goes svr -> fft -> dac -> out rather than svr -> dac -> fft -> out
        // using the highly scientific computational process known as 'guessing'
        return Long.toString(pathsFromTo("svr", "fft") * pathsFromTo("fft", "dac") * pathsFromTo("dac", "out"));
    }
}
