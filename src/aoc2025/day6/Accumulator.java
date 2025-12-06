package aoc2025.day6;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Accumulator {
    // State
    List<Long>[] columns;
    long star1sol = 0;

    // Update state from parsed line
    public Accumulator update(ParsedLine parsedLine) {
        if (columns == null) {
            columns = new List[parsedLine.contents.length];
            for (int i = 0; i < columns.length; i++) {
                columns[i] = new ArrayList<Long>();
            }
        }
        if (parsedLine.contents[0].matches("[+*]")) {

            for (int i = 0; i < columns.length; i++) {
                switch (parsedLine.contents[i]) {
                    case "+":
                        star1sol += columns[i].stream().mapToLong(l -> l).sum();
                        break;
                    case "*":
                        star1sol += columns[i].stream().mapToLong(l -> l).reduce(1L, (l1, l2) -> l1 * l2);
                        break;
                    default:
                        throw new IllegalArgumentException();
                }
            }
        } else {
            // numeric
            for (int i = 0; i < columns.length; i++) {
                columns[i].add(Long.parseLong(parsedLine.contents[i]));
            }
        }
        return this;
    }

    // Extract solution
    public String star1() {
        return Long.toString(star1sol);
    }
}
