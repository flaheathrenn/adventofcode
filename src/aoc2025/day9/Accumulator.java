package aoc2025.day9;

import java.util.ArrayList;
import java.util.List;
import aoc2025.day9.ParsedLine.Corner;

public class Accumulator {
    // State
    List<Corner> corners = new ArrayList<>();
    List<Edge> edges = new ArrayList<>();

    Corner previousCorner = null;

    // Update state from parsed line
    public Accumulator update(ParsedLine parsedLine) {
        corners.add(parsedLine.corner);
        if (previousCorner != null) {
            edges.add(new Edge(previousCorner, parsedLine.corner));
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
                if (edges.stream().anyMatch(e -> e.intersectsRectangle(r1, r2))) {
                    continue;
                }
                // System.out.printf("Updated with %s-%s: %s%n", r1, r2, area);
                maxArea = area;
            }
        }
        return Long.toString(maxArea);
    }

    public record Edge(Corner p1, Corner p2) {
        public boolean intersectsRectangle(Corner c1, Corner c2) {
            long rectLeft = Math.min(c1.x(), c2.x());
            long rectRight = Math.max(c1.x(), c2.x());
            long rectTop = Math.min(c1.y(), c2.y());
            long rectBottom = Math.max(c1.y(), c2.y());
            if (p1.x() == p2.x()) { // edge runs vertically
                if (p1.x() <= rectLeft || p1.x() >= rectRight) {
                    return false; // edge is to left or right of rectangle
                }
                if (p1.y() <= rectTop && p2.y() <= rectTop) {
                    return false; // edge is above rectangle
                }
                if (p1.y() >= rectBottom && p2.y() >= rectBottom) {
                    return false; // edge is below rectangle
                }
                return true; // edge intersects rectangle
            } else if (p1.y() == p2.y()) { // edge runs horizontally
                if (p1.y() <= rectTop || p1.y() >= rectBottom) {
                    return false; // edge is below or above of rectangle
                }
                if (p1.x() <= rectLeft && p2.x() <= rectLeft) {
                    return false; // edge is to left of rectangle
                }
                if (p1.x() >= rectRight && p2.x() >= rectRight) {
                    return false; // edge is to right of rectangle
                }
                return true; // edge intersects rectangle
            } else {
                throw new IllegalStateException();
            }
        }
    }
}
