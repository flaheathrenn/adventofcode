package aoc2025.day2;

import java.util.*;
import java.util.stream.Collectors;

public class Accumulator {
    // State
    Set<Long> repeatingIds_star1 = new HashSet<>();
    Set<Long> repeatingIds_star2 = new HashSet<>();

    // Update state from parsed line
    public Accumulator update(ParsedLine parsedLine) {
        for (ParsedLine.Range range : parsedLine.ranges) {
            int orderOfMagnitudeDifference = range.end().length() - range.start().length();
            if (orderOfMagnitudeDifference > 1) {
                throw new IllegalStateException();
            }
            long start = Long.parseLong(range.start());
            long end = Long.parseLong(range.end());
            Set<Long> repeatingIdsInRange_star1 = new HashSet<>();
            Set<Long> repeatingIdsInRange_star2 = new HashSet<>();
            if (orderOfMagnitudeDifference == 1) {
                long splitOn = Math.powExact(10, range.start().length());
                repeatingIdsInRange_star1.addAll(repeatingIdsInRange(start, splitOn - 1, range.start().length(), 1));
                repeatingIdsInRange_star1.addAll(repeatingIdsInRange(splitOn, end, range.end().length(), 1));
                repeatingIdsInRange_star2.addAll(repeatingIdsInRange(start, splitOn - 1, range.start().length(), 2));
                repeatingIdsInRange_star2.addAll(repeatingIdsInRange(splitOn, end, range.end().length(), 2));
            } else if (orderOfMagnitudeDifference == 0) {
                repeatingIdsInRange_star1.addAll(repeatingIdsInRange(start, end, range.start().length(), 1));
                repeatingIdsInRange_star2.addAll(repeatingIdsInRange(start, end, range.start().length(), 2));
            }

            System.out.printf("Star 1: Adding ids %s for range %s-%s%n", repeatingIdsInRange_star1.stream()
                            .map(l -> Long.toString(l)).collect(Collectors.joining(",", "[", "]")),
                    range.start(), range.end());
            System.out.printf("Star 2: Adding ids %s for range %s-%s%n", repeatingIdsInRange_star2.stream()
                            .map(l -> Long.toString(l)).collect(Collectors.joining(",", "[", "]")),
                    range.start(), range.end());
            repeatingIds_star1.addAll(repeatingIdsInRange_star1);
            repeatingIds_star2.addAll(repeatingIdsInRange_star2);
        }
        return this;
    }

    private final Map<Integer, long[]> repDivisorsByNumberOfDigits_Star1 = Map.of(
            2, new long[]{11L},
            4, new long[]{101L},
            6, new long[]{1001L},
            8, new long[]{10001L},
            10, new long[]{100001L}
    );

    private final Map<Integer, long[]> repDivisorsByNumberOfDigits_Star2 = Map.of(
            2, new long[]{11L},
            3, new long[]{111L},
            4, new long[]{1111L, 101L},
            5, new long[]{11111L},
            6, new long[]{111111L, 10101L, 1001L},
            7, new long[]{1111111L},
            8, new long[]{11111111L, 1010101L, 10001L},
            9, new long[]{111111111L, 1001001L},
            10, new long[]{1111111111L, 101010101L, 100001L}
    );

    // highest number in input is 10 digits long

    private Collection<Long> repeatingIdsInRange(long start, long end, int numberOfDigits, int star) {
        Map<Integer, long[]> repDivisorsByNumberOfDigitsDictionary = switch (star) {
            case 1 -> repDivisorsByNumberOfDigits_Star1;
            case 2 -> repDivisorsByNumberOfDigits_Star2;
            default -> throw new IllegalArgumentException();
        };
        if (!repDivisorsByNumberOfDigitsDictionary.containsKey(numberOfDigits)) {
            return Collections.emptySet();
        }
        Set<Long> repeatingIds = new HashSet<>();
        for (long repDivisor : repDivisorsByNumberOfDigitsDictionary.get(numberOfDigits)) {
            long otherDivisor = start / repDivisor;
            while (otherDivisor <= end / repDivisor) {
                long prod = otherDivisor * repDivisor;
                if (prod >= start && prod <= end) {
                    repeatingIds.add(prod);
                }
                otherDivisor++;
            }
        }
        return repeatingIds;
    }

    // Extract solution
    public String star1() {
        return Long.toString(repeatingIds_star1.stream().reduce(0L, Long::sum));
    }

    public String star2() {
        return Long.toString(repeatingIds_star2.stream().reduce(0L, Long::sum));
    }
}
