package aoc2025.day2;

public class Accumulator {
    // State
    long star1sol = 0;

    // Update state from parsed line
    public Accumulator update(ParsedLine parsedLine) {
        for (ParsedLine.Range range : parsedLine.ranges) {
            long start = Long.parseLong(range.start());
            long end = Long.parseLong(range.end());
            if (range.start().length() == range.end().length() && range.start().length() % 2 == 0) {
                long repDivisor = Math.powExact(10, range.start().length() / 2) + 1;
                long numHere = sumOfIdsBetween(start, end, repDivisor);
                System.out.printf("Adding %d for range %s-%s%n", numHere, range.start(), range.end());
                star1sol += numHere;
            } else if (range.start().length() + 1 == range.end().length()) {
                if (range.start().length() % 2 == 0) {
                    long repDivisor = Math.powExact(10, range.start().length() / 2) + 1;
                    // there can be no repeating IDs in the odd-length section of range so ignore it
                    long rangeEndToUse = Math.powExact(10, range.start().length());
                    long numHere = sumOfIdsBetween(start, rangeEndToUse, repDivisor);
                    System.out.printf("Adding %d for range %s-%s%n", numHere, range.start(), range.end());
                    star1sol += numHere;
                } else {
                    long repDivisor = Math.powExact(10, range.end().length() / 2) + 1;
                    // there can be no repeating IDs in the odd-length section of range so ignore it
                    long rangeStartToUse = Math.powExact(10, range.start().length());
                    long numHere = sumOfIdsBetween(rangeStartToUse, end, repDivisor);
                    System.out.printf("Adding %d for range %s-%s%n", numHere, range.start(), range.end());
                    star1sol += numHere;
                }
            } else {
                System.out.printf("Not handling range %s-%s%n", range.start(), range.end());
            }
        }
        return this;
    }

    private long sumOfIdsBetween(long start, long end, long repDivisor) {
        long result = 0;
        long otherDivisor = start / repDivisor;
        while (otherDivisor <= end / repDivisor) {
            long prod = otherDivisor * repDivisor;
            if (prod >= start && prod <= end) {
                result += prod;
            }
            otherDivisor++;
        }
        return result;
    }

    // Extract solution
    public String star1() {
        return Long.toString(star1sol);
    }
}
