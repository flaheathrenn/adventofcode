package aoc2024.day22;

public record ChangeSequence(int c1, int c2, int c3, int c4) {
    ChangeSequence add(int c5) {
        return new ChangeSequence(c2, c3, c4, c5);
    }

    @Override
    public String toString() {
        return String.format("%d,%d,%d,%d", c1, c2, c3, c4);
    }
}
