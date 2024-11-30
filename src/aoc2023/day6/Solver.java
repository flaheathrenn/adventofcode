package aoc2023.day6;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class Solver {

    public static void main(String[] args) {
        try (BufferedReader inputFileReader = new BufferedReader(new FileReader("src/aoc2023/day6/input.txt"))) {
            // String line = inputFileReader.readLine();
            String timeLine = inputFileReader.readLine();
            long[] times = parse(timeLine.replaceAll("Time:\\s+", "").replaceAll("\\s", ""));
            String distanceLine = inputFileReader.readLine();
            long[] distances = parse(distanceLine.replaceAll("Distance:\\s+", "").replaceAll("\\s", ""));
            int accumulator = 1;
            for (int i = 0; i < times.length; i++) {
                int ways = findDistanceBetweenRoots(times[i], distances[i]);
                System.out.println("Found " + ways + " to beat distance record " + distances[i] + " in time " + times[i]);
                accumulator *= ways;
            }
            System.out.println(accumulator);

        } catch (IOException e) {
            System.err.println("Exception reading file:");
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static long[] parse(String s) {
        return Arrays.stream(s.split("\\s+")).mapToLong(Long::parseLong).toArray();
    }

    public static int findDistanceBetweenRoots(long T, long s) {
        double determinant = Math.sqrt(Math.pow(T, 2.0) - 4*s);
        double upperRoot = 0.5 * (T + determinant);
        double lowerRoot = 0.5 * (T - determinant);
        // it's lucky for us the roots are within integer range!
        return nonInclusiveFloor(upperRoot) - nonInclusiveCeil(lowerRoot) + 1;
    }

    public static int nonInclusiveFloor(double a) {
        int floor = (int) Math.floor(a);
        if (floor == a) {
            return floor - 1;
        }
        return floor;
    }

    public static int nonInclusiveCeil(double a) {
        int ceil = (int) Math.ceil(a);
        if (ceil == a) {
            return ceil + 1;
        }
        return ceil;
    }
}
