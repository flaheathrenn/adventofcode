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
                cost += current.extractCost().orElse(0);
                break;
            }
        }
        return this;
    }

    // Extract solution
    public String star1() {
        return Long.toString(cost);
    }

    public static class ClawMachine {
        int ax;
        int ay;
        int bx;
        int by;
        int px;
        int py;

        void setAButton(int xVal, int yVal) {
            this.ax = xVal;
            this.ay = yVal;
        }

        void setBButton(int xVal, int yVal) {
            this.bx = xVal;
            this.by = yVal;
        }

        void setPrize(int xVal, int yVal) {
            this.px = xVal;
            this.py = yVal;
        }

        Optional<Integer> extractCost() {
            int bnum = px * ay - py * ax;
            int bden = bx * ay - by * ax;
            if (bnum % bden != 0) {
                return Optional.empty(); // no solution
            }
            int bsol = bnum / bden;
            int anum = px - bx * bsol;
            int aden = ax;
            if (anum % aden != 0) {
                return Optional.empty(); // no solution
            }
            int asol = anum / aden;
            if (bsol >= 100 || asol >= 100) {
                return Optional.empty(); // solution too high
            }
            return Optional.of(3 * asol + bsol);
        }
    }
}
