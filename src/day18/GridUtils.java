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

    static record GridCoordinate(int row, int column) {
        GridCoordinate advance(Direction direction) {
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

        String readFromGrid(String[][] grid) {
            return grid[row][column];
        }

        void writeToGrid(String[][] grid, String value) {
            grid[row][column] = value;
        }

        boolean validOnGrid(String[][] grid) {
            int maxRow = grid.length;
            int maxColumn = grid[0].length;
            return row >= 0 && column >= 0 && row < maxRow && column < maxColumn;
        }
    }
}
