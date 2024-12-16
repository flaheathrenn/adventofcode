package aoc2024.day16;

import global.GridUtils.Direction;
import global.GridUtils.GridCoordinate;

record Node(GridCoordinate coordinate, Direction direction) {
    Node flip() {
        return new Node(coordinate, direction.flip());
    }
}
