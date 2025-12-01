package aoc2025.day1;

public class Accumulator {
    // State
    int pos = 50;
    int pw_star1 = 0;
    int pw_star2 = 0;

    // Update state from parsed line
    public Accumulator update(ParsedLine parsedLine) {
        pos += parsedLine.value;
        if (pos > 0) {
            pw_star2 += pos / 100;
        } else if (pos == 0) {
            // we went down from a number <100 to 0
            pw_star2++;
        } else { // pos < 0
            if (pos == parsedLine.value) {
                // started from 0, so no sign change
            } else {
                pw_star2++; // for the sign change
            }
            pw_star2 += -pos / 100;
        }
        pos = ((pos % 100) + 100) % 100;
        if (pos == 0) {
            pw_star1++;
        }
        return this;
    }

    // Extract solution
    public String star1() {
        return Integer.toString(pw_star1);
    }

    public String star2() {
        return Integer.toString(pw_star2);
    }
}
