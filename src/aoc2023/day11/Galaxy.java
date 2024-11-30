package aoc2023.day11;

import java.util.List;

public class Galaxy {
    private static final long EXPANSION_COEFFICIENT = 1000000;

    private long row;
    private long column;

    public Galaxy(long row, long column) {
        this.row = row;
        this.column = column;
    }

    public long getRow() {
        return row;
    }

    public long getColumn() {
        return column;
    }

    /**
     * Update this galaxy with its position after the universe expands
     * @param rowsToExpand List of row numbers for which the corresponding row contains no galaxies
     * @param columnsToExpand List of column numbers for which the corresponding column contains no galaxies
     */
    public void expandUniverse(List<Long> rowsToExpand, List<Long> columnsToExpand) {
        System.out.println(String.format("Moving galaxy (%d, %d)...", row, column));
        this.row += rowsToExpand.stream().filter(x -> x < this.row).count() * (EXPANSION_COEFFICIENT-1);
        this.column += columnsToExpand.stream().filter(x -> x < this.column).count() * (EXPANSION_COEFFICIENT-1);
        System.out.println(String.format("...to (%d, %d)", row, column));
    }

    /**
     * @param other a Galaxy
     * @return the Manhattan distance between this Galaxy and the other
     */
    public long distance(Galaxy other) {
        return Math.abs(this.row - other.row) + Math.abs(this.column - other.column);
    }
}
