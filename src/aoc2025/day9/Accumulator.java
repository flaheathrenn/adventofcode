package aoc2025.day9;

import java.util.ArrayList;
import java.util.List;
import aoc2025.day9.ParsedLine.Corner;

public class Accumulator {
    // State
    List<Corner> corners = new ArrayList<>();
    List<Corner> allEdgePoints = new ArrayList<>();

    Corner previousCorner = null;

    // Update state from parsed line
    public Accumulator update(ParsedLine parsedLine) {
        corners.add(parsedLine.corner);
        allEdgePoints.add(parsedLine.corner);
        if (previousCorner != null) {
            if (previousCorner.x() == parsedLine.corner.x()) {
                for (long l = Math.min(previousCorner.y(), parsedLine.corner.y()) + 1;
                     l < Math.max(previousCorner.y(), parsedLine.corner.y());
                     l++) {
                    allEdgePoints.add(new Corner(previousCorner.x(), l));
                }
            } else if (previousCorner.y() == parsedLine.corner.y()) {
                for (long l = Math.min(previousCorner.x(), parsedLine.corner.x()) + 1;
                     l < Math.max(previousCorner.x(), parsedLine.corner.x());
                     l++) {
                    allEdgePoints.add(new Corner(l, previousCorner.y()));
                }
            }
        }
        previousCorner = parsedLine.corner;
        return this;
    }

    // Extract solution
    public String star1() {
//        corners.removeIf(r -> corners.stream().anyMatch(r1 -> r1.x() < r.x() && r1.y() < r.y())
//            && corners.stream().anyMatch(r2 -> r2.x() > r.x() && r2.y() > r.y()));
        long maxArea = 0;
        for (Corner r1 : corners) {
            for (Corner r2 : corners) {
                long area = (Math.abs(r1.x() - r2.x()) + 1) * (Math.abs(r1.y() - r2.y()) + 1);
//                System.out.printf("%s and %s -> %s%n", r1, r2, area);
                maxArea = Math.max(maxArea, area);
            }
        }
        return Long.toString(maxArea);
    }

    /*
     * NOTE: In order for star 2 to work, the first line of the input needs to be copied and added to the end of the input
     */
    // Extract solution
    public String star2() {
        long maxArea = 0;
        for (Corner r1 : corners) {
            for (Corner r2 : corners) {
                long area = (Math.abs(r1.x() - r2.x()) + 1) * (Math.abs(r1.y() - r2.y()) + 1);
                if (area <= maxArea) {
                    continue;
                }
                if (allEdgePoints.stream().anyMatch(ic -> isInside(ic, r1, r2))) {
                    continue;
                }
                // System.out.printf("Updated with %s-%s: %s%n", r1, r2, area);
                maxArea = area;
            }
        }
        return Long.toString(maxArea);
    }

    public boolean isInside(Corner potentialInterior, Corner c1, Corner c2) {
        return potentialInterior.x() < Math.max(c1.x(), c2.x())
                && potentialInterior.x() > Math.min(c1.x(), c2.x())
                && potentialInterior.y() < Math.max(c1.y(), c2.y())
                && potentialInterior.y() > Math.min(c1.y(), c2.y());
    }
}
