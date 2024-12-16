package aoc2023.day23;

import java.util.Optional;

class GridUtils {
    static enum Direction {
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

    static record GridCoordinate(int row, int column) implements Comparable<GridCoordinate> {
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

        @Override
        public int compareTo(GridCoordinate o) {
            if (this.row == o.row) {
                return Integer.compare(this.column, o.column);
            }
            return Integer.compare(this.row, o.row);
        }

        @Override
        public String toString() {
            return String.format("(%d,%d)", row, column);
        }
    }
}
