package aoc2024.day8;

import java.util.HashSet;
import java.util.Set;

public record Node(int x, int y) {
    public Set<Node> antiNodesWith(Node other) {
        int xDiff = other.x() - x;
        int yDiff = other.y() - y;
        Set<Node> antiNodeSet = new HashSet<>();
        antiNodeSet.add(new Node(other.x() + xDiff, other.y() + yDiff));
        antiNodeSet.add(new Node(x - xDiff, y - yDiff));
        return antiNodeSet;
    }

    public boolean isInBounds(int maxX, int maxY) {
        return x >= 0 && x <= maxX && y >= 0 && y <= maxY;
    }
}
