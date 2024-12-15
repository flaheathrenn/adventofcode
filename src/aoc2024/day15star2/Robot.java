package aoc2024.day15star2;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import global.GridUtils.Direction;
import global.GridUtils.GridCoordinate;

public class Robot {
    GridCoordinate gridCoordinate;

    public Robot(GridCoordinate initialCoordinate) {
        this.gridCoordinate = initialCoordinate;
    }

    public void move(Direction direction, List<Box> boxes, Set<GridCoordinate> walls) {
        GridCoordinate prospectiveNewLocation = gridCoordinate.step(direction);
        if (walls.contains(prospectiveNewLocation)) {
            return; // don't move
        }
        Optional<Box> maybeBoxInSpace = boxes.stream().filter(b -> b.gridCoordinateLeft.equals(prospectiveNewLocation) || b.gridCoordinateRight.equals(prospectiveNewLocation)).findFirst();
        if (maybeBoxInSpace.isEmpty()) {
            this.gridCoordinate = prospectiveNewLocation;
            return;
        }
        Box boxInSpace = maybeBoxInSpace.get();
        if (!boxInSpace.canMove(direction, boxes, walls)) {
            // then I can't move either
            return;
        } else {
            // then it moves and I move
            boxInSpace.move(direction, boxes, walls);
            this.gridCoordinate = prospectiveNewLocation;
            return;
        }
    }
}
