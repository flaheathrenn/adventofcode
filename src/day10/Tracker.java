package day10;

public class Tracker {
    int row;
    int column;
    Direction direction;

    int getRow() {
        return row;
    }

    int getColumn() {
        return column;
    }

    void advance(String tile, Direction oldDirection) {
        switch (tile) {
            case "-" -> {
                if (oldDirection == Direction.WEST) {
                    column--;
                    direction = Direction.WEST;
                } else if (oldDirection == Direction.EAST) {
                    column++;
                    direction = Direction.EAST;
                } else {
                    throw new IllegalStateException();
                }
            }
            case "|" -> {
                if (oldDirection == Direction.NORTH) {
                    row--;
                    direction = Direction.NORTH;
                } else if (oldDirection == Direction.SOUTH) {
                    row++;
                    direction = Direction.SOUTH;
                } else {
                    throw new IllegalStateException();
                }
            }
            case "L" -> {
                if (oldDirection == Direction.SOUTH) {
                    column++;
                    direction = Direction.EAST;
                } else if (oldDirection == Direction.WEST) {
                    row--;
                    direction = Direction.NORTH;
                } else {
                    throw new IllegalStateException();
                }
            }
            case "F" -> {
                if (oldDirection == Direction.NORTH) {
                    column++;
                    direction = Direction.EAST;
                } else if (oldDirection == Direction.WEST) {
                    row++;
                    direction = Direction.SOUTH;
                } else {
                    throw new IllegalStateException();
                }
            }
            case "7" -> {
                if (oldDirection == Direction.NORTH) {
                    column--;
                    direction = Direction.WEST;
                } else if (oldDirection == Direction.EAST) {
                    row++;
                    direction = Direction.SOUTH;
                } else {
                    throw new IllegalStateException();
                }
            }
            case "J" -> {
                if (oldDirection == Direction.SOUTH) {
                    column--;
                    direction = Direction.WEST;
                } else if (oldDirection == Direction.EAST) {
                    row--;
                    direction = Direction.NORTH;
                } else {
                    throw new IllegalStateException();
                }
            }
            case "S" -> {
                if (oldDirection == Direction.SOUTH) {
                    row++;
                    direction = Direction.SOUTH;
                } else if (oldDirection == Direction.NORTH) {
                    row--;
                    direction = Direction.NORTH;
                } else if (oldDirection == Direction.WEST) {
                    column--;
                    direction = Direction.WEST;
                } else if (oldDirection == Direction.EAST) {
                    column++;
                    direction = Direction.EAST;
                } else {
                    throw new IllegalStateException();
                }
            }
            default -> throw new IllegalStateException();
        }
    }

    public static enum Direction {
        NORTH,
        WEST,
        SOUTH,
        EAST;
    }
}
