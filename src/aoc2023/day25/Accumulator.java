package aoc2023.day25;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Accumulator {
    // State
    List<String> nodes = new ArrayList<>();
    List<Set<String>> connections = new ArrayList<>();

    // Update state from parsed line
    public Accumulator update(ParsedLine parsedLine) {
        String left = parsedLine.items[0];
        nodes.add(left);
        for (int i = 1; i < parsedLine.items.length; i++) {
            connections.add(Set.of(left, parsedLine.items[i]));
            nodes.add(parsedLine.items[i]);
        }
        return this;
    }

    // Extract solution
    public String star1() {
        System.out.println("There are " + nodes.size() + " nodes with a total of " + connections.size()
                + " connections between them");

        Map<Set<String>, Integer> connectionFrequency = new HashMap<>();

        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            System.out.print(".");
            String node = nodes.get(random.nextInt(nodes.size() - 1));
            String otherNode = nodes.get(random.nextInt(nodes.size() - 1));
            if (node.equals(otherNode)) {
                continue;
            }
            try {
                Set<Set<String>> path = findPath(node, otherNode, Collections.emptySet());
                for (Set<String> connection : path) {
                    connectionFrequency.put(connection,
                            connectionFrequency.containsKey(connection) ? connectionFrequency.get(connection) + 1
                                    : 1);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        List<Map.Entry<Set<String>, Integer>> entrySetSorted = connectionFrequency.entrySet().stream()
                .sorted((e1, e2) -> Integer.compare(e1.getValue(), e2.getValue())).toList();
        for (Map.Entry<Set<String>, Integer> entry : entrySetSorted) {
            System.out.println("Connection " + entry.getKey() + " used " + entry.getValue() + " times");
        }

        return "";
    }

    private Set<Set<String>> findPath(String node, String otherNode, Set<Set<String>> usedConnections) {
        Collections.shuffle(connections);
        for (Set<String> connection : connections) {
            if (!connection.contains(node)) {
                continue;
            }
            if (usedConnections.contains(connection)) {
                continue;
            }
            if (connection.contains(otherNode)) {
                // found connection
                return Set.of(connection);
            } else {
                String intermediateNode = connection.stream().filter(n -> !n.equals(node)).findAny()
                        .orElseThrow(IllegalStateException::new);
                Set<Set<String>> path = new HashSet<>();
                path.add(connection);
                if (path.size() > 10) {
                    break;
                }
                Set<Set<String>> newUsedConnections = new HashSet<>();
                newUsedConnections.addAll(usedConnections);
                newUsedConnections.add(connection);
                try {
                    Set<Set<String>> restOfPath = findPath(intermediateNode, otherNode, newUsedConnections);
                    if (restOfPath.isEmpty()) {
                        continue;
                    } else {
                        path.addAll(restOfPath);
                        return path;
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        return Collections.emptySet();
    }
}
