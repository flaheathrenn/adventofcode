package aoc2023.day24;

import java.util.ArrayList;
import java.util.List;

public class Accumulator {
    // State
    List<Hailstone> hailstones = new ArrayList<>();

    // Update state from parsed line
    public Accumulator update(ParsedLine parsedLine) {
        hailstones.add(parsedLine.hailstone);
        return this;
    }

    // Extract solution
    public String star1() {
        long intersections = 0;
        for (int i = 0; i < hailstones.size() - 1; i++) {
            for (int j = i + 1; j < hailstones.size(); j++) {
                if (intersect(hailstones.get(i), hailstones.get(j))) {
                    intersections++;
                }
            }
        }

        return Long.toString(intersections);
    }

    private boolean intersect(Hailstone h1, Hailstone h2) {
        // System.out.println("Hailstone A: " + h1);
        // System.out.println("Hailstone B: " + h2);
        long testAreaMin = 200000000000000L;
        long testAreaMax = 400000000000000L;
        
        long numerator = (h1.px() * h1.vy()) - (h1.py() * h1.vx()) + (h2.py() * h1.vx()) - (h2.px() * h1.vy());
        long denominator = (h2.vx() * h1.vy()) - (h2.vy() * h1.vx());

        if (denominator == 0) {
            // System.out.println("Hailstones' trajectories are parallel");
            return false;
        }

        double t2 = (double) numerator / (double) denominator;

        if (t2 <= 0) {
            // System.out.println("Hailstones crossed in the past");
            return false;
        }

        double x = h2.px() + t2 * h2.vx();

        double t1 = (x - h1.px()) / h1.vx();
        if (t1 <= 0) {
            // System.out.println("Hailstones crossed in the past");
            return false;
        }

        if (x < testAreaMin || x > testAreaMax) {
            // System.out.println("Hailstones cross outside the test area");
            return false;
        }

        double y = h2.py() + t2 * h2.vy();
        if (y < testAreaMin || y > testAreaMax) {
            // System.out.println("Hailstones cross outside the test area");
            return false;
        }

        // System.out.println("Hailstones cross at x=" + x + ", y=" + y);
        return true;
    }
}
