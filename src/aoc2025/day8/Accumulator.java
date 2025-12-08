package aoc2025.day8;

import java.util.*;
import java.util.stream.Collectors;

public class Accumulator {
    // State
    Set<ParsedLine.JunctionBox> junctionBoxSet = new HashSet<>();
    SortedSet<JunctionBoxPair> junctionBoxPairs_star1 = new TreeSet<>();
    SortedSet<JunctionBoxPair> junctionBoxPairs_star2 = new TreeSet<>();
    Set<Circuit> circuits_star1 = new HashSet<>();
    Set<Circuit> circuits_star2 = new HashSet<>();

    // Update state from parsed line
    public Accumulator update(ParsedLine parsedLine) {
        for (ParsedLine.JunctionBox existingJunctionBox : junctionBoxSet) {
            junctionBoxPairs_star1.add(new JunctionBoxPair(existingJunctionBox, parsedLine.junctionBox));
            junctionBoxPairs_star2.add(new JunctionBoxPair(existingJunctionBox, parsedLine.junctionBox));
        }
        junctionBoxSet.add(parsedLine.junctionBox);
        circuits_star1.add(new Circuit(parsedLine.junctionBox));
        circuits_star2.add(new Circuit(parsedLine.junctionBox));
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
            JunctionBoxPair closestPair = junctionBoxPairs_star1.removeFirst();
//            System.out.printf("Joining %s with %s at distanceSquared %s%n", closestPair.first, closestPair.second, closestPair.distanceSquared);
            Circuit circuitOfFirst = circuits_star1.stream().filter(c -> c.contains(closestPair.first)).findAny().orElseThrow(IllegalStateException::new);
            Circuit circuitOfSecond = circuits_star1.stream().filter(c -> c.contains(closestPair.second)).findAny().orElseThrow(IllegalStateException::new);
            if (circuitOfFirst.equals(circuitOfSecond)) {
                continue; // already connected
            }
            circuitOfFirst.addAll(circuitOfSecond); // combine circuits
            // For some reason probably related to the implementation of HashSet, can't just use circuits_star1.remove(circuitOfSecond)
            circuits_star1 = circuits_star1.stream().filter(c -> !circuitOfSecond.equals(c)).collect(Collectors.toSet());
//            System.out.printf("Current circuits: %s%n", circuits_star1);
//            System.out.printf("Size: %d%n", circuits_star1.size());
        }
        return String.valueOf(circuits_star1.stream().map(Circuit::junctionBoxes).map(Set::size).sorted(Comparator.reverseOrder()).mapToInt(i->i).limit(3).reduce(1, (i1, i2) -> i1 * i2));
    }

    public String star2() {
        long finalX1 = 0, finalX2 = 0;
        while (!junctionBoxPairs_star2.isEmpty()) {
            JunctionBoxPair closestPair = junctionBoxPairs_star2.removeFirst();
//            System.out.printf("Joining %s and %s%n", closestPair.first, closestPair.second);
            finalX1 = closestPair.first.x();
            finalX2 = closestPair.second.x();
            Circuit circuitOfFirst = circuits_star2.stream().filter(c -> c.contains(closestPair.first)).findAny().orElseThrow(IllegalStateException::new);
            Circuit circuitOfSecond = circuits_star2.stream().filter(c -> c.contains(closestPair.second)).findAny().orElseThrow(IllegalStateException::new);
//            System.out.printf("Circuits: %s, %s%n", circuitOfFirst, circuitOfSecond);
            if (circuitOfFirst.equals(circuitOfSecond)) {
                System.err.println("Encountered already joined pair!");
                throw new IllegalStateException();
            }
            circuitOfFirst.addAll(circuitOfSecond); // combine circuits
            circuits_star2 = circuits_star2.stream().filter(c -> !circuitOfSecond.equals(c)).collect(Collectors.toSet());
//            System.out.printf("Removing all pairs in circuit %s%n...", circuitOfFirst);
//            System.out.printf("Pairs found: %s%n", junctionBoxPairs_star2.stream().filter(pair -> circuitOfFirst.contains(pair.first) && circuitOfFirst.contains(pair.second)).collect(Collectors.toSet()));
            junctionBoxPairs_star2.removeIf(pair -> circuitOfFirst.contains(pair.first) && circuitOfFirst.contains(pair.second));
        }
        return String.valueOf(finalX1 * finalX2);
    }
}
