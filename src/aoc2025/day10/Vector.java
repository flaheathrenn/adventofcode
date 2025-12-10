package aoc2025.day10;

import java.util.*;

public record Vector(int[] elements, int width) implements Comparable<Vector> {
    static void main(String[] args) {
// (0,1,2,3,4) (0,1,2,3,4,5) (3,5,6) (0,2,4,5) (1,2,5) (3) (0,3,4,5,6) (1,2,4,6) {48,163,178,40,179,32,133}
        Vector bigVector = new Vector(new int[] { 48, 163, 178, 40, 179, 32, 133 });
        List<Vector> parts = List.of(
                new Vector(new int[] { 1, 1, 1, 1, 1, 0, 0}),
                new Vector(new int[] { 1, 1, 1, 1, 1, 1, 0}),
                new Vector(new int[] { 0, 0, 0, 1, 0, 1, 1}),
                new Vector(new int[] { 1, 0, 1, 0, 1, 1, 0}),
                new Vector(new int[] { 0, 1, 1, 0, 0, 1, 0}),
                new Vector(new int[] { 0, 0, 0, 1, 0, 0, 0}),
                new Vector(new int[] { 1, 0, 0, 1, 1, 1, 1}),
                new Vector(new int[] { 0, 1, 1, 0, 1, 0, 1}) 
        );

//        System.out.println(bigVector.divideBy(smallVector));
//        System.out.println(bigVector.divideBy(otherSmallVector));
        System.out.println(calculateStarTwoButtonPresses(new TreeSet<>(parts), bigVector));
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
        System.out.println("Making remainder " + divisionResult.remainder + " from parts " + parts);
        long answerIncludingWidestVector = divisionResult.result() + calculateStarTwoButtonPresses(new TreeSet<>(parts), divisionResult.remainder());
        System.out.println("Trying to just make " + goal + " from " + parts);
        long answerIgnoringWidestVector = calculateStarTwoButtonPresses(new TreeSet<>(parts), goal);
        System.out.println("Comparing answers " + answerIncludingWidestVector + " and " + answerIgnoringWidestVector);
        return Long.min(answerIncludingWidestVector, answerIgnoringWidestVector);
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
}
