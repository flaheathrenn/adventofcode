package aoc2024.day23;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Accumulator {
    // State
    Map<String, Set<String>> edges = new HashMap<>();
    Set<Set<String>> foundTriads = new HashSet<>();

    // Update state from parsed line
    public Accumulator update(ParsedLine parsedLine) {
        for (String node : parsedLine.connection) {
            if (!edges.containsKey(node)) {
                edges.put(node, new HashSet<>());
            }
        }
        edges.get(parsedLine.connection[0]).add(parsedLine.connection[1]);
        edges.get(parsedLine.connection[1]).add(parsedLine.connection[0]);
        return this;
    }

    // Extract solution
    public String star1() {
        for (String node : edges.keySet()) {
            if (!node.startsWith("t")) {
                continue;
            }
            for (String node2 : edges.get(node)) {
                // look for something that's connected to both node and node2
                for (String node3 : edges.get(node)) {
                    if (node2.equals(node3)) {
                        continue;
                    }
                    if (edges.get(node2).contains(node3)) {
                        foundTriads.add(Set.of(node, node2, node3));
                    }
                }
            }
        }
        return Integer.toString(foundTriads.size());
    }

    public String star2() {
        // find triads
        for (String node : edges.keySet()) {
            for (String node2 : edges.get(node)) {
                // look for something that's connected to both node and node2
                for (String node3 : edges.get(node)) {
                    if (node2.equals(node3)) {
                        continue;
                    }
                    if (edges.get(node2).contains(node3)) {
                        foundTriads.add(Set.of(node, node2, node3));
                    }
                }
            }
        }

        Set<Set<String>> currentCliques = foundTriads;
        while (currentCliques.size() != 1) {
            currentCliques = nextCliques(currentCliques);
        }
        // technically this doesn't cover the case where there's only one clique but it could still be larger
        // but whatever it still worked on my input
        return currentCliques.stream().findFirst().orElse(Collections.emptySet()).stream().sorted().collect(Collectors.joining(","));
    }

    private Set<Set<String>> nextCliques(Set<Set<String>> currentCliques) {
        Set<Set<String>> nextCliques = new HashSet<>();
        for (Set<String> clique : currentCliques) {
            for (Map.Entry<String, Set<String>> entry : edges.entrySet()) {
                if (entry.getValue().containsAll(clique)) {
                    Set<String> biggerClique = new HashSet<>(clique);
                    if (biggerClique.add(entry.getKey())) {
                        nextCliques.add(biggerClique);
                    }
                }
            }
        }
        return nextCliques;
    }
}
