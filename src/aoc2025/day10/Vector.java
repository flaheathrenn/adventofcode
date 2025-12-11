package aoc2025.day10;

import java.util.*;

public record Vector(int[] elements, int width) implements Comparable<Vector> {
    static void main(String[] args) {
//        Vector v = new Vector(new int[] { 2, 5, 8 });
//        Matrix m = new Matrix(new int[][] {
//                { 0, 1, 0 },
//                { 1, 0, 1 },
//                { 1, 1, 0 }
//        });
//        System.out.println(v.multiply(m));
//
//        Vector v1 = new Vector(new int[] { 13, 10, 5 });
//        System.out.println(v1.multiply(m.inverse()));

        Vector bigVector = new Vector(new int[] {222,258,228,230,246,216,68,67,272,243});
        List<Vector> parts = List.of(
                new Vector(new int[] { 0, 1, 0, 0, 1, 0, 1, 0, 1, 1}),
                new Vector(new int[] { 1, 1, 1, 1, 0, 0, 0, 0, 1, 1}),
                new Vector(new int[] { 0, 0, 0, 0, 1, 0, 0, 1, 1, 0}),
                new Vector(new int[] { 0, 1, 1, 1, 0, 1, 0, 1, 0, 0}),
                new Vector(new int[] { 1, 1, 1, 0, 1, 0, 1, 1, 1, 0}),
                new Vector(new int[] { 1, 0, 0, 0, 1, 0, 1, 0, 0, 1}),
                new Vector(new int[] { 0, 1, 0, 0, 0, 1, 0, 0, 1, 0}),
                new Vector(new int[] { 0, 1, 1, 1, 0, 1, 1, 1, 1, 0}),
                new Vector(new int[] { 1, 1, 1, 1, 1, 1, 0, 0, 1, 1}),
                new Vector(new int[] { 1, 1, 1, 1, 1, 1, 0, 1, 1, 1}),
                new Vector(new int[] { 0, 1, 1, 1, 0, 1, 1, 1, 1, 1}),
                new Vector(new int[] { 0, 1, 0, 1, 0, 0, 0, 0, 1, 1})
        );
        Matrix m = new Matrix(parts.toArray(new Vector[0]));
        System.out.println(bigVector.multiply(m.transpose()));
//        System.out.println(calculateStarTwoButtonPresses(new TreeSet<>(parts), bigVector));
    }

    private static long calculateStarTwoButtonPresses(SortedSet<Vector> parts, Vector goal) {
        System.out.println("Trying to make " + goal + " from " + parts);
        if (goal.isZeroVector()) {
            return 0L; // no more button presses required
        }
        if (parts.isEmpty()) {
            System.out.println("Parts is empty, recursion bottoming out");
            return Integer.MAX_VALUE;
        }

        // Deal quickly with case where only one part-vector contains 1 at a position
        Vector totalParts = parts.stream().reduce(new Vector(new int[goal.elements.length]), Vector::add);
        for (int i = 0; i < totalParts.elements.length; i++) {
            if (totalParts.elements[i] == 1) {
                // only one part-vector contributes to this part of joltage
                int finalI = i;
                Optional<Vector> contributingPart = parts.stream().filter(part -> part.elements[finalI] == 1).findFirst();
                return contributingPart.map(part -> {
                    VectorDivisionResult divisionResult = goal.divideBy(part);
                    SortedSet<Vector> otherParts = new TreeSet<>(parts);
                    otherParts.remove(part);
                    return divisionResult.result + calculateStarTwoButtonPresses(otherParts, divisionResult.remainder);
                }).orElse((long) Integer.MAX_VALUE);
            }
        }

        Vector widestVector = parts.removeLast();
        Vector.VectorDivisionResult divisionResult = goal.divideBy(widestVector);
        System.out.println(goal + " divided by " + widestVector + " is " + divisionResult);
        if (divisionResult.result() == 0) {
            System.out.println("No button presses here, skipping straight to making " + goal + " from " + parts);
            return calculateStarTwoButtonPresses(parts, goal);
        }
        long minimum = Integer.MAX_VALUE;
        for (int i = divisionResult.result(); i >= 0; i--) {
            long thisAnswer = i + calculateStarTwoButtonPresses(new TreeSet<>(parts), goal.subtract(widestVector.times(i)));
            minimum = Long.min(minimum, thisAnswer);
        }
        return minimum;
    }

    public Vector(int[] elements) {
        int width = 0;
        for (int i = 0; i < elements.length; i++) {
            if (elements[i] != 0) {
                width++;
            }
        }
        this(elements, width);
    }
    public VectorDivisionResult divideBy(Vector divisor) {
        if (this.elements.length != divisor.elements.length) {
            throw new IllegalArgumentException();
        }
        int result = Integer.MAX_VALUE;
        for (int i = 0; i < elements.length; i++) {
            if (divisor.elements[i] == 1) {
                result = Math.min(result, this.elements[i]);
            } else if (divisor.elements[i] != 0) {
                throw new IllegalArgumentException();
            }
        }
        return new VectorDivisionResult(result, this.subtract(divisor.times(result)));
    }

    public Vector subtract(Vector subtractend) {
        if (this.elements.length != subtractend.elements.length) {
            throw new IllegalArgumentException();
        }
        int[] resultArray = new int[this.elements.length];
        for (int i = 0; i < this.elements.length; i++) {
            resultArray[i] = this.elements[i] - subtractend.elements[i];
        }
        return new Vector(resultArray);
    }

    public Vector add(Vector addend) {
        if (this.elements.length != addend.elements.length) {
            throw new IllegalArgumentException();
        }
        int[] resultArray = new int[this.elements.length];
        for (int i = 0; i < this.elements.length; i++) {
            resultArray[i] = this.elements[i] + addend.elements[i];
        }
        return new Vector(resultArray);
    }

    public Vector times(int multiplicend) {
        int[] resultArray = new int[this.elements.length];
        for (int i = 0; i < this.elements.length; i++) {
            resultArray[i] = this.elements[i] * multiplicend;
        }
        return new Vector(resultArray);
    }

    @Override
    public int compareTo(Vector o) {
        return Comparator.comparingLong(Vector::width)
                .thenComparing(Vector::elements, Arrays::compare)
                .compare(this, o);
    }

    public boolean isZeroVector() {
        for (int element : this.elements) {
            if (element != 0) {
                return false;
            }
        }
        return true;
    }

    public record VectorDivisionResult(int result, Vector remainder) {}

    @Override
    public String toString() {
        return Arrays.toString(elements);
    }

    public Vector multiply(Matrix m) {
        if (this.elements.length != m.elements().length) {
            throw new IllegalArgumentException();
        }
        int[] result = new int[this.elements.length];
        for (int i = 0; i < this.elements.length; i++) {
            for (int j = 0; j < m.elements().length; j++) {
                result[i] += this.elements[j] * m.elements()[j][i];
            }
        }
        return new Vector(result);
    }
}
