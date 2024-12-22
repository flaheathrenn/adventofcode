package aoc2024.day22;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Accumulator {
    // State
    long sum = 0;
    Map<ChangeSequence, Long> sequenceValue = new HashMap<>();

    // Update state from parsed line
    public Accumulator update(ParsedLine parsedLine) {
        Set<ChangeSequence> seenSequences = new HashSet<>();
        long seed = parsedLine.seed;
        int price = (int) (seed % 10);
        ChangeSequence sequence = new ChangeSequence(0, 0, 0, 0);
        for (int i = 0; i < 3; i++) {
            seed = iterate(seed);
            int newPrice = (int) (seed % 10);
            sequence = sequence.add(newPrice - price);
            price = newPrice;
        }
        for (int i = 0; i < (2000 - 3); i++) {
            seed = iterate(seed);
            int newPrice = (int) (seed % 10);
            sequence = sequence.add(newPrice - price);
            price = newPrice;
            if (seenSequences.contains(sequence)) {
                continue;
            } else {
                if (!sequenceValue.containsKey(sequence)) {
                    sequenceValue.put(sequence, (long) newPrice);
                } else {
                    sequenceValue.put(sequence, sequenceValue.get(sequence) + newPrice);
                }
                seenSequences.add(sequence);
            }
        }
        sum += seed;
        return this;
    }

    // Extract solution
    public String star1() {
        return Long.toString(sum);
    }

    public String star2() {
        // for (Map.Entry<ChangeSequence, Long> sequEntry : sequenceValue.entrySet()) {
        //     System.out.println(String.format("%s -> %d", sequEntry.getKey(), sequEntry.getValue()));
        // }
        return sequenceValue.values().stream().max(Long::compareTo).map(String::valueOf).orElse("N/A");
    }

    long iterate(long seed) {
        long sixfour = seed * 64;
        seed = seed ^ sixfour;
        seed = seed % 16777216L;
        long threetwo = seed / 32;
        seed = seed ^ threetwo;
        seed = seed % 16777216L;
        long twooh = seed * 2048;
        seed = seed ^ twooh;
        return seed % 16777216L;
    }
}
