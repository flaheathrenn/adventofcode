package global;

import java.util.function.BiFunction;

public interface GridUtils {
    public static enum Direction {
        UP, RIGHT, DOWN, LEFT;
    
        Direction rotate() {
            return switch (this) {
                case UP -> RIGHT;
                case RIGHT -> DOWN;
                case DOWN -> LEFT;
                case LEFT -> UP;
            };
        }

        String marker() {
            return switch (this) {
                case UP -> "^";
                case RIGHT -> ">";
                case DOWN -> "v";
                case LEFT -> "<";
            };
        }
    }
    public static record GridCoordinate(int i, int j) {
        /**
         * @return the contents of the grid at this coordinate, or empty string if coordinate is outside grid
         */
        public <T> T read(T[][] grid) {
            if (!isWithin(grid)) {
                return null;
            }
            return grid[i][j];
        }

        /**
         * @return true if write was successful, false otherwise (i.e. target was outside the grid)
         */
        public <T> boolean write(T[][] grid, T content) {
            if (!isWithin(grid)) {
                return false;
            }
            grid[i][j] = content;
            return true;
        }

        public <T> boolean isWithin(T[][] grid) {
            if (i < 0 || i >= grid.length) {
                return false;
            }
            if (j < 0 || j >= grid[i].length) {
                return false;
            }
            return true;
        }

        public GridCoordinate step(Direction direction) {
            int newI = switch (direction) {
                case UP -> i - 1;
                case DOWN -> i + 1;
                case LEFT, RIGHT -> i;
            };
            int newJ = switch (direction) {
                case LEFT -> j - 1;
                case RIGHT -> j + 1;
                case UP, DOWN -> j;
            };
            return new GridCoordinate(newI, newJ);
        }
    }

    public static <T> GridCoordinate find(T[][] grid, T target) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (new GridCoordinate(i, j).read(grid).equals(target)) {
                    return new GridCoordinate(i, j);
                }
            }
        }
        return null;
    }

    public static <T> GridCoordinate find(T[][] grid, T target, BiFunction<T, T, Boolean> equals) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (equals.apply(new GridCoordinate(i, j).read(grid), target)) {
                    return new GridCoordinate(i, j);
                }
            }
        }
        return null;
    }

    public static <T> void prettyPrint(T[][] grid) {
        for (T[] row : grid) {
            for (T item : row) {

                System.out.print(item == null ? " " : String.valueOf(item));
            }
            System.out.println();
        }
    }

    public static long count(String[][] grid, String target) {
        long count = 0L;
        for (String[] row : grid) {
            for (String item : row) {
                if (item.equals(target)) {
                    count++;
                }
            }
        }
        return count;
    }

    public static <T> long count(T[][] grid, T target, BiFunction<T, T, Boolean> equals) {
        long count = 0L;
        for (T[] row : grid) {
            for (T item : row) {
                if (equals.apply(item, target)) {
                    count++;
                }
            }
        }
        return count;
    }
}
