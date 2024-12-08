package aoc2024.day8;

import java.util.HashSet;
import java.util.Set;

public record Node(int x, int y) {
    // Calculate the two antinodes under star 1 conditions - some may be out of bounds
    public Set<Node> antiNodesWith(Node other) {
        int xDiff = other.x() - x;
        int yDiff = other.y() - y;
        Set<Node> antiNodeSet = new HashSet<>();
        antiNodeSet.add(new Node(other.x() + xDiff, other.y() + yDiff));
        antiNodeSet.add(new Node(this.x() - xDiff, this.y() - yDiff));
        return antiNodeSet;
    }

    // Calculate all the antinodes under star 2 conditions - none are out of bounds
    public Set<Node> antiNodesWith(Node other, int maxX, int maxY) {
        int xDiff = other.x() - x;
        int yDiff = other.y() - y;
        Set<Node> antiNodeSet = new HashSet<>();
        // go in one direction from other
        Node currentNode = other;
        while (currentNode.isInBounds(maxX, maxY)) {
            antiNodeSet.add(currentNode);
            currentNode = new Node(currentNode.x() + xDiff, currentNode.y() + yDiff);
        }
        // and now in the other direction
        currentNode = this;
        while (currentNode.isInBounds(maxX, maxY)) {
            antiNodeSet.add(currentNode);
            currentNode = new Node(currentNode.x() - xDiff, currentNode.y() - yDiff);
        }
        return antiNodeSet;
    }

    public boolean isInBounds(int maxX, int maxY) {
        return x >= 0 && x <= maxX && y >= 0 && y <= maxY;
    }
}
