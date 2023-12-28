package day23;

import java.util.Optional;

public class GridUtils {
    public static enum Direction {
        UP, DOWN, LEFT, RIGHT;

    Direction opposite() {
            return switch (this) {
                case UP -> DOWN;
                case DOWN -> UP;
                case LEFT -> RIGHT;
                case RIGHT -> LEFT;
            };
        }

    boolean isValidStep(String gridChar) {
        if (".".equals(gridChar)) {
            return true;
        }
        return switch (this) {
            case UP -> "^".equals(gridChar);
            case DOWN -> "v".equals(gridChar);
            case LEFT -> "<".equals(gridChar);
            case RIGHT -> ">".equals(gridChar);
        };
    }
    }

    public static record GridCoordinate(int row, int column) {
        public Optional<String> safeGet(String[][] grid) {
            if (row < 0 || column < 0 || row >= grid.length || column >= grid[0].length) {
                return Optional.empty();
            }
            return Optional.of(grid[row][column]);
        }

        public GridCoordinate advance(Direction direction) {
            switch (direction) {
                case LEFT: {
                    return new GridCoordinate(row, column - 1);
                }
                case RIGHT: {
                    return new GridCoordinate(row, column + 1);
                }
                case UP: {
                    return new GridCoordinate(row - 1, column);
                }
                case DOWN: {
                    return new GridCoordinate(row + 1, column);
                }
                default: {
                    throw new IllegalArgumentException();
                }
            }
        }
    }
}
