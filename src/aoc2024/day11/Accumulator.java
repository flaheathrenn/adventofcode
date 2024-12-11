package aoc2024.day11;

import java.util.List;

public class Accumulator {
    // State
    long[] stones;
    StoneCache stoneCache = new StoneCache();

    // Update state from parsed line
    public Accumulator update(ParsedLine parsedLine) {
        stones = parsedLine.stones; // only one line
        return this;
    }

    // Extract solution
    public String star1() {
        long stoneCount = 0;
        for (long stone : stones) {
            stoneCount += stonesAfterWithCaching(stone, 25).size();
        }
        return Long.toString(stoneCount);
    }

    public String star2() {
        long stoneCount = 0;
        for (long stone : stones) {
            stoneCount += stonesAfterWithCaching(stone, 40).size();
        }
        return Long.toString(stoneCount);
    }

    private List<Long> stonesAfterWithCaching(long startStone, int blinks) {
        if (blinks == 1) {
            List<Long> stonesAfterBlink = blink(startStone);
            stoneCache.put(startStone, 1, stonesAfterBlink);
            return stonesAfterBlink;
        }
        return stoneCache.lookUp(startStone, blinks)
                .map(cacheHit -> {
                    if (cacheHit.blinks() == blinks) {
                        // exact result is cached
                        // System.out.println("Cache hit! Exact result");
                        return cacheHit.stonesAfterBlinks();
                    }
                    if (cacheHit.blinks() > blinks) {
                        throw new IllegalStateException(); // should never happen
                    }
                    // System.out.println("Cache hit! Skipping " + cacheHit.blinks() + " steps");
                    int blinksRemaining = blinks - cacheHit.blinks();
                    List<Long> stonesAfterBlinks = cacheHit.stonesAfterBlinks().stream()
                            .map(stone -> stonesAfterWithCaching(stone, blinksRemaining))
                            .flatMap(List::stream)
                            .sorted()
                            .toList();
                    stoneCache.put(startStone, blinks, stonesAfterBlinks);
                    return stonesAfterBlinks;
                })
                .orElseGet(() -> {
                    List<Long> stonesAfterBlink = blink(startStone);
                    List<Long> stonesAfterBlinks = stonesAfterBlink.stream()
                            .map(stone -> stonesAfterWithCaching(stone, blinks - 1))
                            .flatMap(List::stream)
                            .sorted()
                            .toList();
                    stoneCache.put(startStone, blinks, stonesAfterBlinks);
                    return stonesAfterBlinks;
                });
    }

    private List<Long> blink(long startStone) {
        if (startStone == 0) {
            return List.of(1L);
        }
        if (evenNumberOfDigits(startStone)) {
            long left = leftHalf(startStone);
            long right = rightHalf(startStone);
            return List.of(left < right ? left : right, left < right ? right : left);
        }
        return List.of(startStone * 2024);
    }

    private long stonesAfter(long startStone, int blinks) {
        if (blinks == 0) {
            return 1;
        }
        if (startStone == 0) {
            return stonesAfter(1, blinks - 1);
        }
        if (evenNumberOfDigits(startStone)) {
            return stonesAfter(leftHalf(startStone), blinks - 1) + stonesAfter(rightHalf(startStone), blinks - 1);
        }
        return stonesAfter(startStone * 2024, blinks - 1);
    }

    private boolean evenNumberOfDigits(long startStone) {
        return ((int) Math.log10(startStone)) % 2 == 1;
    }

    private long leftHalf(long startStone) {
        int halfLength = (((int) Math.log10(startStone)) + 1) / 2;
        return startStone / Math.round(Math.pow(10, halfLength));
    }

    private long rightHalf(long startStone) {
        int halfLength = (((int) Math.log10(startStone)) + 1) / 2;
        return startStone % Math.round(Math.pow(10, halfLength));
    }
}
