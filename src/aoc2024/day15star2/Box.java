package aoc2024.day15star2;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import global.GridUtils.Direction;
import global.GridUtils.GridCoordinate;

public class Box {
    GridCoordinate gridCoordinateLeft, gridCoordinateRight;

    public Box(GridCoordinate initialCoordinateLeft, GridCoordinate initialCoordinateRight) {
        this.gridCoordinateLeft = initialCoordinateLeft;
        this.gridCoordinateRight = initialCoordinateRight;
    }

    public boolean canMove(Direction direction, List<Box> boxes, Set<GridCoordinate> walls) {
        GridCoordinate prospectiveNewLocationLeft = gridCoordinateLeft.step(direction);
        GridCoordinate prospectiveNewLocationRight = gridCoordinateRight.step(direction);
        if (walls.contains(prospectiveNewLocationRight) || walls.contains(prospectiveNewLocationLeft)) {
            return false; // can't move
        }
        if (direction == Direction.LEFT) {
            Optional<Box> maybeBlockingBox = boxes.stream()
                    .filter(b -> b.gridCoordinateRight.equals(prospectiveNewLocationLeft)).findFirst();
            return maybeBlockingBox.map(b -> b.canMove(direction, boxes, walls)).orElse(true);
        }
        if (direction == Direction.RIGHT) {
            Optional<Box> maybeBlockingBox = boxes.stream()
                    .filter(b -> b.gridCoordinateLeft.equals(prospectiveNewLocationRight)).findFirst();
            return maybeBlockingBox.map(b -> b.canMove(direction, boxes, walls)).orElse(true);
        }
        // otherwise it's moving up or down
        Optional<Box> maybeBlockingBoxLeft = boxes.stream()
                .filter(b -> b.gridCoordinateRight.equals(prospectiveNewLocationLeft)
                        || b.gridCoordinateLeft.equals(prospectiveNewLocationLeft))
                .findFirst();
        Optional<Box> maybeBlockingBoxRight = boxes.stream()
                .filter(b -> b.gridCoordinateRight.equals(prospectiveNewLocationRight)
                        || b.gridCoordinateLeft.equals(prospectiveNewLocationRight))
                .findFirst();
        return maybeBlockingBoxLeft.map(b -> b.canMove(direction, boxes, walls)).orElse(true)
                && maybeBlockingBoxRight.map(b -> b.canMove(direction, boxes, walls)).orElse(true);
    }

    public void move(Direction direction, List<Box> boxes, Set<GridCoordinate> walls) {
        if (!canMove(direction, boxes, walls)) {
            return;
        }
        GridCoordinate prospectiveNewLocationLeft = gridCoordinateLeft.step(direction);
        GridCoordinate prospectiveNewLocationRight = gridCoordinateRight.step(direction);
        if (direction == Direction.LEFT) {
            Optional<Box> maybeBlockingBox = boxes.stream()
                    .filter(b -> b.gridCoordinateRight.equals(prospectiveNewLocationLeft)).findFirst();
            maybeBlockingBox.ifPresent(b -> b.move(direction, boxes, walls));
            this.gridCoordinateLeft = prospectiveNewLocationLeft;
            this.gridCoordinateRight = prospectiveNewLocationRight;
            return;
        }
        if (direction == Direction.RIGHT) {
            Optional<Box> maybeBlockingBox = boxes.stream()
                    .filter(b -> b.gridCoordinateLeft.equals(prospectiveNewLocationRight)).findFirst();
            maybeBlockingBox.ifPresent(b -> b.move(direction, boxes, walls));
            this.gridCoordinateLeft = prospectiveNewLocationLeft;
            this.gridCoordinateRight = prospectiveNewLocationRight;
            return;
        }
        // otherwise it's moving up or down
        Optional<Box> maybeBlockingBoxLeft = boxes.stream()
                .filter(b -> b.gridCoordinateRight.equals(prospectiveNewLocationLeft)
                        || b.gridCoordinateLeft.equals(prospectiveNewLocationLeft))
                .findFirst();
        Optional<Box> maybeBlockingBoxRight = boxes.stream()
                .filter(b -> b.gridCoordinateRight.equals(prospectiveNewLocationRight)
                        || b.gridCoordinateLeft.equals(prospectiveNewLocationRight))
                .findFirst();
        // these could be the same if the box is in line with this one        
        if (maybeBlockingBoxLeft.equals(maybeBlockingBoxRight)) {
            maybeBlockingBoxLeft.ifPresent(b -> b.move(direction, boxes, walls));
        } else {
            maybeBlockingBoxLeft.ifPresent(b -> b.move(direction, boxes, walls));
            maybeBlockingBoxRight.ifPresent(b -> b.move(direction, boxes, walls));
        }
        
        // now move this box
        this.gridCoordinateLeft = prospectiveNewLocationLeft;
        this.gridCoordinateRight = prospectiveNewLocationRight;
    }

    public int getGPSCoordinate() {
        return this.gridCoordinateLeft.i() * 100 + this.gridCoordinateLeft.j();
    }

    @Override
    public String toString() {
        return this.gridCoordinateLeft + ";" + this.gridCoordinateRight;
    }

}
