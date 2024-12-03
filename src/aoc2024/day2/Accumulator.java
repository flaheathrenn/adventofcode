package aoc2024.day2;

import java.util.List;
import java.util.Optional;

public class Accumulator {
    // State
    long safeRecordCountStar1 = 0;
    long safeRecordCountStar2 = 0;

    // Update state from parsed line
    public Accumulator update(ParsedLine parsedLine) {
        safeRecordCountStar1 += isRecordSafe(parsedLine.differences, false) ? 1 : 0;
        safeRecordCountStar2 += isRecordSafe(parsedLine.differences, true) ? 1 : 0;
        return this;
    }

    // Extract solution
    public String star1() {
        return Long.toString(safeRecordCountStar1);
    }

    public String star2() {
        return Long.toString(safeRecordCountStar2);
    }

    private boolean isRecordSafe(List<Long> differences, boolean dampen) {
        int majoritySign = Integer.signum(differences.stream().map(Long::signum).reduce(0, Integer::sum));
        Optional<Long> firstUnsafeDifference = differences.stream().filter(difference -> {
            return Long.signum(difference) != majoritySign || Math.abs(difference) > 3;
        }).findFirst();
        if (firstUnsafeDifference.isEmpty()) {
            return true;
        }
        if (!dampen) {
            return false;
        }
        int firstUnsafeDifferenceIndex = differences.indexOf(firstUnsafeDifference.get());
        if (firstUnsafeDifferenceIndex == differences.size() - 1) {
            return true; // i.e. only the last level difference is unsafe, so removing the last level will dampen the problem out
        }
        if (firstUnsafeDifferenceIndex == 0) {
            // if the first difference is unsafe, it might be the case that the first level is the only unsafe one, so test that:
            if (isRecordSafe(differences.subList(1, differences.size()), false)) {
                return true;
            }
            // it could still be the case that the second level is the unsafe one so proceed
        }
        long combinedNewDifference = differences.remove(firstUnsafeDifferenceIndex); // remove unsafe difference
        combinedNewDifference += differences.remove(firstUnsafeDifferenceIndex); // and the one after
        differences.add(firstUnsafeDifferenceIndex, combinedNewDifference); // and replace with their combined value
        boolean newValue = isRecordSafe(differences, false);
        return newValue;

    }
}
