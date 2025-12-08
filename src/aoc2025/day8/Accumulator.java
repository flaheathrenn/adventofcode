package aoc2025.day8;

import java.util.*;
import java.util.stream.Collectors;

public class Accumulator {
    // State
    Set<ParsedLine.JunctionBox> junctionBoxSet = new HashSet<>();
    SortedSet<JunctionBoxPair> junctionBoxPairs = new TreeSet<>();
    Set<Circuit> circuits = new HashSet<>();

    // Update state from parsed line
    public Accumulator update(ParsedLine parsedLine) {
        for (ParsedLine.JunctionBox existingJunctionBox : junctionBoxSet) {
            junctionBoxPairs.add(new JunctionBoxPair(existingJunctionBox, parsedLine.junctionBox));
        }
        junctionBoxSet.add(parsedLine.junctionBox);
        circuits.add(new Circuit(parsedLine.junctionBox));
        return this;
    }

    public record JunctionBoxPair(long distanceSquared, ParsedLine.JunctionBox first, ParsedLine.JunctionBox second) implements Comparable<JunctionBoxPair> {
        public JunctionBoxPair(ParsedLine.JunctionBox first, ParsedLine.JunctionBox second) {
            this(first.distanceSquared(second), first, second);
        }

        @Override
        public int compareTo(JunctionBoxPair o) {
            return Long.compare(this.distanceSquared, o.distanceSquared);
        }

        @Override
        public String toString() {
            return String.format("%s-%s", first, second);
        }
    }

    public record Circuit(Set<ParsedLine.JunctionBox> junctionBoxes) {
        public Circuit(ParsedLine.JunctionBox junctionBox) {
            this(new HashSet<>());
            this.junctionBoxes.add(junctionBox);
        }

        public boolean add(ParsedLine.JunctionBox junctionBox) {
            return this.junctionBoxes.add(junctionBox);
        }

        public boolean addAll(Circuit other) {
            return this.junctionBoxes.addAll(other.junctionBoxes);
        }

        public boolean contains(ParsedLine.JunctionBox junctionBox) {
            return this.junctionBoxes().contains(junctionBox);
        }

        @Override
        public String toString() {
            return junctionBoxes.toString();
        }
    }

    // Extract solution
    public String star1() {
        final int NUMBER_OF_CONNECTIONS_TO_MAKE = 1000; // 10 for test  input
        for (int i = 1; i <= NUMBER_OF_CONNECTIONS_TO_MAKE; i++) {
            JunctionBoxPair closestPair = junctionBoxPairs.removeFirst();
            Circuit circuitOfFirst = circuits.stream().filter(c -> c.contains(closestPair.first)).findAny().orElseThrow(IllegalStateException::new);
            Circuit circuitOfSecond = circuits.stream().filter(c -> c.contains(closestPair.second)).findAny().orElseThrow(IllegalStateException::new);
            if (circuitOfFirst.equals(circuitOfSecond)) {
                continue; // already connected
            }
            circuitOfFirst.addAll(circuitOfSecond); // combine circuits
            // For some reason probably related to the implementation of HashSet, can't just use circuits_star1.remove(circuitOfSecond)
            circuits = circuits.stream().filter(c -> !circuitOfSecond.equals(c)).collect(Collectors.toSet());
        }
        return String.valueOf(circuits.stream().map(Circuit::junctionBoxes).map(Set::size).sorted(Comparator.reverseOrder()).mapToInt(i->i).limit(3).reduce(1, (i1, i2) -> i1 * i2));
    }

    public String star2() {
        while (!junctionBoxPairs.isEmpty()) {
            JunctionBoxPair closestPair = junctionBoxPairs.removeFirst();
            Circuit circuitOfFirst = circuits.stream().filter(c -> c.contains(closestPair.first)).findAny().orElseThrow(IllegalStateException::new);
            Circuit circuitOfSecond = circuits.stream().filter(c -> c.contains(closestPair.second)).findAny().orElseThrow(IllegalStateException::new);
            if (circuitOfFirst.equals(circuitOfSecond)) {
                // already connected
                continue;
            }
            circuitOfFirst.addAll(circuitOfSecond); // combine circuits
            circuits = circuits.stream().filter(c -> !circuitOfSecond.equals(c)).collect(Collectors.toSet());
            if (circuits.size() == 1) {
                return String.valueOf(closestPair.first.x() * closestPair.second.x());
            }
        }
        return "ERROR";
    }
}
