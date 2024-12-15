package aoc2024.day15;

import java.util.List;
import java.util.Set;

import global.GridUtils.Direction;
import global.GridUtils.GridCoordinate;

public class Box {
    GridCoordinate gridCoordinate;

    public Box(GridCoordinate initialCoordinate) {
        this.gridCoordinate = initialCoordinate;
    }

    public boolean move(Direction direction, List<Box> boxes, Set<GridCoordinate> walls) {
        // System.out.println("Moving box at " + this.gridCoordinate + " to the " + direction.name() + "...");
        GridCoordinate prospectiveNewLocation = gridCoordinate.step(direction);
        if (walls.contains(prospectiveNewLocation)) {
            // System.out.println("Box at " + gridCoordinate + " can't move, wall");
            return false; // don't move
        }
        if (!boxes.contains(new Box(prospectiveNewLocation))) {
            // System.out.println("Box at " + gridCoordinate + " can move to " + prospectiveNewLocation);
            this.gridCoordinate = prospectiveNewLocation;
            return true; // move
        }
        Box boxInSpace = boxes.stream().filter(b -> b.gridCoordinate.equals(prospectiveNewLocation)).findFirst().get();
        if (boxInSpace.move(direction, boxes, walls)) {
            // System.out.println("Now box at " + gridCoordinate + " can move to " + prospectiveNewLocation);
            this.gridCoordinate = prospectiveNewLocation;
            return true; // move
        } else {
            // System.out.println("So box at " + gridCoordinate + " can't move");
            return false; // don't move
        }
    }

    public int getGPSCoordinate() {
        return this.gridCoordinate.i() * 100 + this.gridCoordinate.j();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((gridCoordinate == null) ? 0 : gridCoordinate.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Box other = (Box) obj;
        if (gridCoordinate == null) {
            if (other.gridCoordinate != null)
                return false;
        } else if (!gridCoordinate.equals(other.gridCoordinate))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "[" + gridCoordinate.i() + "," + gridCoordinate.j() + "]";
    }

}
