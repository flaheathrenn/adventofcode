package aoc2023.day25;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Accumulator {
    // State
    Set<String> nodes = new HashSet<>();
    Set<Set<String>> connections = new HashSet<>();

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

        // Map<Integer, Integer> cardinalityMap = new HashMap<>();
        // for (String node : nodes) {
        //     long cardinality = connections.stream().filter(c -> c.contains(node)).count();
        //     if (!cardinalityMap.containsKey((int) cardinality)) {
        //         cardinalityMap.put((int) cardinality, 1);
        //     } else {
        //         cardinalityMap.put((int) cardinality, cardinalityMap.get((int) cardinality) +  1);
        //     }
        // }
        // System.out.println(cardinalityMap);

        Set<String> inGroup = new HashSet<>();
        Set<String> outGroup = new HashSet<>();
        for (String thisNode : nodes) {
            Map<Integer, Integer> matchingZerosCount = new HashMap<>();
            for (String otherNode: nodes) {
                if (thisNode.equals(otherNode)) {
                    continue;
                }
                int matchingZeros = 0;
                for (String node : nodes) {
                    if (node.equals(thisNode) || node.equals(otherNode)) {
                        continue;
                    }
                    if (!connections.contains(Set.of(thisNode, node)) && !connections.contains(Set.of(otherNode, node))) {
                        matchingZeros++;
                    }
                }
                matchingZerosCount.put(matchingZeros, matchingZerosCount.containsKey(matchingZeros) ? matchingZerosCount.get(matchingZeros) + 1 : 1);
            }
            int lows = matchingZerosCount.get(1538) + matchingZerosCount.get(1539);
            int highs = matchingZerosCount.get(1540);
            if (lows < highs) {
                inGroup.add(thisNode);
            } else {
                outGroup.add(thisNode);
            }

            // have prospective groups
            for (String igNode : inGroup) {
                for (String ogNode : outGroup) {
                    if (connections.contains(Set.of(igNode, ogNode))) {
                        System.out.println("HIT");
                    }
                }
            }
        }

        return Integer.toString(inGroup.size() * outGroup.size());

        // Set<String> inGroup = Set.of("bvb", "hfx", "jqt", "ntq", "rhn", "xhk");
        // List<String> nodesOrdered = nodes.stream().sorted((s1, s2) -> {
        //     if (inGroup.contains(s1) && !inGroup.contains(s2)) {
        //         return -1;
        //     }
        //     if (!inGroup.contains(s1) && inGroup.contains(s2)) {
        //         return 1;
        //     }
        //     return s1.compareTo(s2);
        // }).toList();
        // System.out.println(nodesOrdered);
        // for (String me : nodesOrdered) {
        //     for (String you : nodesOrdered) {
        //         if (me.equals(you)) {
        //             System.out.print("-");
        //         } else {
        //             System.out.print(connections.contains(Set.of(me, you)) ? "1" : "0");
        //         }
        //     }
        //     System.out.println();
        // }

        // List<Set<String>> connectionsSorted = new ArrayList<>(connections);
        // for (Set<String> connection1 : connectionsSorted) {
        //     for (Set<String> connection2 : connectionsSorted) {
        //         if (connection1.equals(connection2)) {
        //             continue;
        //         }
        //         for (Set<String> connection3 : connectionsSorted) {
        //             if (connection3.equals(connection1) || connection3.equals(connection2)) {
        //                 continue;
        //             }
        //             Set<Set<String>> reducedConnections = new HashSet<>(connections);
        //             reducedConnections.remove(connection1);
        //             reducedConnections.remove(connection2);
        //             reducedConnections.remove(connection3);
        //             int maxReachable = maxReachable(nodes, reducedConnections);
        //             if (maxReachable(nodes, reducedConnections) != nodes.size()) {
        //                 return Integer.toString(maxReachable * (nodes.size() - maxReachable));
        //             }
        //         }
        //     }
        // }
        // return "";
    }

    // private int maxReachable(Set<String> allNodes, Set<Set<String>> reducedConnections) {
    //     String startNode = allNodes.stream().findAny().orElseThrow(IllegalStateException::new);
    //     Set<String> visitedNodes = new HashSet<>();
    //     Queue<String> nodesToVisit = new LinkedList<>();
    //     nodesToVisit.add(startNode);
    //     while (nodesToVisit.size() != 0) {
    //         String visit = nodesToVisit.poll();
    //         visitedNodes.add(visit);
    //         reducedConnections.stream().filter(c -> c.contains(visit)).forEach(c -> {
    //             String other = c.stream().filter(n -> !visit.equals(n)).findAny().orElseThrow(IllegalStateException::new);
    //             if (!visitedNodes.contains(other)) {
    //                 nodesToVisit.add(other);
    //             }
    //         });
    //     }
    //     return visitedNodes.size();
    // }
}
