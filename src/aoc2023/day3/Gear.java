package aoc2023.day3;

public class Gear {
    int row;
    int column;

    boolean valid = false;
    Integer firstValue;
    Integer secondValue;

    public void setPosition(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public void addValue(int value) {
        if (firstValue == null) {
            firstValue = value;
            return;
        }
        if (secondValue == null) {
            secondValue = value;
            valid = true;
            return;
        }
        // already has two values, adding a third makes it invalid
        valid = false;
    }

    public int gearRatio() {
        if (!valid) {
            return 0;
        }
        return firstValue * secondValue;
    }
}
