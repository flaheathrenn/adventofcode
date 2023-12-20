package day18;

public class GridUtils {
    public static void printGrid(String[][] grid) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                System.out.print(grid[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }

    public static enum Direction {
        UP, LEFT, DOWN, RIGHT;

        Direction opposite() {
            return switch (this) {
                case UP -> DOWN;
                case DOWN -> UP;
                case LEFT -> RIGHT;
                case RIGHT -> LEFT;
            };
        }

        String asciiArt() {
            return switch (this) {
                case UP -> "^";
                case DOWN -> "v";
                case LEFT -> "<";
                case RIGHT -> ">";
            };
        }
    }

    static record GridCoordinate(long row, long column) implements Comparable<GridCoordinate> {
        GridCoordinate advance(Direction direction, long distance) {
            switch (direction) {
                case LEFT: {
                    return new GridCoordinate(row, column - distance);
                }
                case RIGHT: {
                    return new GridCoordinate(row, column + distance);
                }
                case UP: {
                    return new GridCoordinate(row - distance, column);
                }
                case DOWN: {
                    return new GridCoordinate(row + distance, column);
                }
                default: {
                    throw new IllegalArgumentException();
                }
            }
        }

        @Override
        public int compareTo(GridCoordinate o) {
            if (this.row == o.row) {
                return Long.compare(this.column, o.column);
            }
            return Long.compare(this.row, o.row);
        }
    }
}
