package aoc2024.day1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Accumulator {
    // State
    List<Long> lefts = new ArrayList<>();
    List<Long> rights = new ArrayList<>();

    // Update state from parsed line
    public Accumulator update(ParsedLine parsedLine) {
        lefts.add(parsedLine.left);
        rights.add(parsedLine.right);
        return this;
    }

    // Extract solution
    public String star1() {
        Collections.sort(lefts);
        Collections.sort(rights);
        Iterator<Long> leftIt = lefts.iterator();
        Iterator<Long> rightIt = rights.iterator();
        long acc = 0;
        while (leftIt.hasNext()) {
            acc += Math.abs(leftIt.next() - rightIt.next());
        }
        return Long.toString(acc);
    }

    public String star2() {
        long acc = 0;
        for (long left : lefts) {
            acc += left * rights.stream().filter(x -> left == x).count();
        }
        return Long.toString(acc);
    }
}
