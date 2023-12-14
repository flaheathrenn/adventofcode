package day14;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CycleDetector {

    public static void main(String[] args) {
        Map<String, String> memo = Map.of(
                "0", "1",
                "1", "a",
                "a", "b",
                "b", "c",
                "c", "d",
                "d", "1");
        CycleInformation info = detectCycleInMemo(memo, "0");
        // What is the 100th entry?
        int afterThisManyCycles = 10;
        long lowestEquivalent = info.cycleStart + ((afterThisManyCycles - info.cycleStart) % info.cycleLength);
        System.out.println(lowestEquivalent);
        String result = "0";
        for (int i = 0; i < lowestEquivalent; i++) {
            result = memo.get(result);
        }
        System.out.println(result);
    }

    public static CycleInformation detectCycleInMemo(Map<String, String> memo, String startPattern) {
        // Work out where the cycle starts and its length
        List<String> patterns = new ArrayList<>();
        patterns.add(startPattern);
        String currentPattern = memo.get(startPattern);
        int cycleEnds = 1;
        while (!patterns.contains(currentPattern)) {
            patterns.add(currentPattern);
            currentPattern = memo.get(currentPattern);
            cycleEnds++;
        }
        int cycleStart = patterns.indexOf(currentPattern);
        System.out.println("The cycle starts with pattern " + cycleStart
                + " and is of length " +
                (cycleEnds - cycleStart));
        return new CycleInformation(cycleStart, cycleEnds - cycleStart);
    }

    public static record CycleInformation(long cycleStart, long cycleLength) {
    }

}
