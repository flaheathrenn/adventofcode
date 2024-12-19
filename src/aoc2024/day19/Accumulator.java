package aoc2024.day19;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Accumulator {
    // State
    Set<String> designs;
    long possibleDisplays = 0;
    long possibleWays = 0;

    // Update state from parsed line
    public Accumulator update(ParsedLine parsedLine) {
        if (parsedLine.skip) {
            return this;
        }
        if (parsedLine.existingTowels != null) {
            designs = Arrays.stream(parsedLine.existingTowels).collect(Collectors.toSet());
            if (!designs.containsAll(Set.of("w", "g", "b", "u"))) {
                System.out.println("Star 1 assumption incorrect: can't only think about r");
                System.exit(1);
            }
            if (!designs.containsAll(Set.of("rw", "rg", "rb", "ru", "rr"))) {
                System.out.println("Star 1 assumption incorrect: can't only think about terminal r");
                System.exit(1);
            }
            return this;
        }
        if (isPossible(parsedLine.design, designs)) {
            possibleDisplays++;
        }
        Map<String, Long> memo = new HashMap<>();
        possibleWays += possibleWays(parsedLine.design, designs, memo);
        return this;
    }

    // Star 1
    private boolean isPossible(String target, Set<String> designs) {
        if (!target.endsWith("r")) {
            return true; // includes empty strings
        }
        for (String design : designs) {
            if (target.endsWith(design)) {
                if (isPossible(target.substring(0, target.length() - design.length()), designs)) {
                    return true;
                }
            }
        }
        return false;
    }

    private long possibleWays(String target, Set<String> designs, Map<String, Long> memo) {
        if (target.isBlank()) {
            return 1L;
        }
        if (memo.containsKey(target)) {
            return memo.get(target);
        }
        long possibleWays = 0L;
        for (String design : designs) {
            if (target.endsWith(design)) {
                possibleWays += possibleWays(target.substring(0, target.length() - design.length()), designs, memo);
            }
        }
        memo.put(target, possibleWays);
        return possibleWays;
    }

    // Extract solution
    public String star1() {
        return Long.toString(possibleDisplays);
    }

    // Extract solution
    public String star2() {
        return Long.toString(possibleWays);
    }
}
