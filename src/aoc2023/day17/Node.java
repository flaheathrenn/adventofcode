package aoc2023.day17;

import global.GridUtils.Direction;
import global.GridUtils.GridCoordinate;

record Node(GridCoordinate coordinate, Direction bannedDirection) {

    @Override
    public String toString() {
        return coordinate() + ":" + bannedDirection().marker();
    }
}
