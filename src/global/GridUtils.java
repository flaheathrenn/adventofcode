package global;

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
        public String read(String[][] grid) {
            if (!isWithin(grid)) {
                return "";
            }
            return grid[i][j];
        }

        /**
         * @return true if write was successful, false otherwise (i.e. target was outside the grid)
         */
        public boolean write(String[][] grid, String content) {
            if (!isWithin(grid)) {
                return false;
            }
            grid[i][j] = content;
            return true;
        }

        public boolean isWithin(String[][] grid) {
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

    public static GridCoordinate find (String[][] grid, String target) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (new GridCoordinate(i, j).read(grid).equals(target)) {
                    return new GridCoordinate(i, j);
                }
            }
        }
        return null;
    }

    public static void prettyPrint(String[][] grid) {
        for (String[] row : grid) {
            for (String item : row) {
                System.out.print(item + " ");
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
}
