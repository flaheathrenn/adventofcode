package aoc2024.day18;

import java.util.HashSet;
import java.util.Set;

import global.GridUtils.GridCoordinate;

class WallGroup {
    Set<GridCoordinate> contents = new HashSet<>();
    int minI, minJ, maxI, maxJ;

    WallGroup(GridCoordinate initial) {
        contents.add(initial);
        minI = initial.i();
        maxI = initial.i();
        minJ = initial.j();
        maxJ = initial.j();
    }

    void add(GridCoordinate addee) {
        contents.add(addee);
        minI = Integer.min(minI, addee.i());
        maxI = Integer.max(maxI, addee.i());
        minJ = Integer.min(minJ, addee.j());
        maxJ = Integer.max(maxJ, addee.j());
    }

    boolean isSpanning(int gridSize) {
        return (minI == 0 && maxI == gridSize - 1)
            || (minJ == 0 && maxJ == gridSize - 1)
            || (minI == 0 && minJ == 0)
            || (maxI == gridSize - 1 && maxJ == gridSize - 1);
    }

    boolean contains(GridCoordinate c) {
        return contents.contains(c);
    }
}
