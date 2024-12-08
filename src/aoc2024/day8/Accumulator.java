package aoc2024.day8;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Accumulator {
    // State
    int currentRow = 0;
    int maxX = 0;
    int maxY = 0;
    Map<String, List<Node>> nodes = new HashMap<>();
    Set<Node> antiNodeSet = new HashSet<>();

    // Update state from parsed line
    public Accumulator update(ParsedLine parsedLine) {
        for (int i = 0; i < parsedLine.row.length; i++) {
            String freq = parsedLine.row[i];
            if (freq.equals(".")) {
                continue;
            }
            Node newNode = new Node(currentRow, i);
            if (nodes.containsKey(freq)) {
                nodes.get(freq).add(newNode);
            } else {
                List<Node> nodeList = new ArrayList<>();
                nodeList.add(newNode);
                nodes.put(freq, nodeList);
            }
        }
        currentRow++;
        maxX = parsedLine.row.length - 1;
        maxY = currentRow - 1;
        return this;
    }

    // Extract solution
    public String star(boolean isStar2) {
        for (List<Node> freqNodes : nodes.values()) {
            for (int i = 0; i < freqNodes.size(); i++) {
                for (int j = i + 1; j < freqNodes.size(); j++) {
                    if (isStar2) {
                        antiNodeSet.addAll(freqNodes.get(i).antiNodesWith(freqNodes.get(j), maxX, maxY));
                    } else {
                        antiNodeSet.addAll(freqNodes.get(i).antiNodesWith(freqNodes.get(j)));
                    }
                }
            }
        }
        if (!isStar2) { // not necessary if star2
            antiNodeSet.removeIf(x -> !x.isInBounds(maxX, maxY));
        }
        return String.valueOf(antiNodeSet.size());
    }
}
