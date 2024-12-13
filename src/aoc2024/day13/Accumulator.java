package aoc2024.day13;

import java.util.Optional;

public class Accumulator {
    // State
    ClawMachine current = new ClawMachine();
    long cost = 0;

    // Update state from parsed line
    public Accumulator update(ParsedLine parsedLine) {
        switch (parsedLine.type) {
            case ABUTT: {
                current.ax = parsedLine.xVal;
                current.ay = parsedLine.yVal;
                break;
            }
            case BBUTT: {
                current.bx = parsedLine.xVal;
                current.by = parsedLine.yVal;
                break;
            }
            case PRIZE: {
                current.px = parsedLine.xVal;
                current.py = parsedLine.yVal;
                break;
            }
            case BLANK: {
                cost += current.extractCost().orElse(0L);
                break;
            }
        }
        return this;
    }

    // Extract solution
    public String star2() {
        return Long.toString(cost);
    }

    public static class ClawMachine {
        long ax;
        long ay;
        long bx;
        long by;
        long px;
        long py;

        void setAButton(long xVal, long yVal) {
            this.ax = xVal;
            this.ay = yVal;
        }

        void setBButton(long xVal, long yVal) {
            this.bx = xVal;
            this.by = yVal;
        }

        void setPrize(long xVal, long yVal) {
            this.px = xVal;
            this.py = yVal;
        }

        Optional<Long> extractCost() {
            long bnum = px * ay - py * ax;
            long bden = bx * ay - by * ax;
            if (bnum % bden != 0) {
                return Optional.empty(); // no solution
            }
            long bsol = bnum / bden;
            long anum = px - bx * bsol;
            long aden = ax;
            if (anum % aden != 0) {
                return Optional.empty(); // no solution
            }
            long asol = anum / aden;
            // Readd the following if to return to star 1, though I suspect even there it doesn't actually do anything
            // if (bsol >= 100 || asol >= 100) {
            //     return Optional.empty(); // solution too high
            // }
            return Optional.of(3 * asol + bsol);
        }
    }
}
