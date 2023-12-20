package day18;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

import day18.GridUtils.GridCoordinate;

public class Accumulator {
    // State
    Set<GridCoordinate> corners = new TreeSet<GridCoordinate>();
    GridCoordinate currentPosition = new GridCoordinate(150, 1);

    // Update state from parsed line
    public Accumulator update(ParsedLine parsedLine) {
        currentPosition = currentPosition.advance(parsedLine.direction, parsedLine.distance);
        corners.add(currentPosition);
        return this;
    }

    // Extract solution
    public String star1() {
        Map<Long, List<GridCoordinate>> cornersByRow = new TreeMap<>(corners.stream().collect(Collectors.groupingBy(GridCoordinate::row)));
        Map<Long, List<Interval>> intervalsByRow = new TreeMap<>();
        cornersByRow.entrySet().stream().forEachOrdered(entry -> {
            Long row = entry.getKey();
            List<GridCoordinate> coordinates = entry.getValue();
            if (!intervalsByRow.containsKey(row)) {
                intervalsByRow.put(row, new ArrayList<>());
            }
            List<Interval> intervalsForRow = intervalsByRow.get(row);
            for (int i = 0; i < coordinates.size(); i += 2) {
                intervalsForRow.add(new Interval(coordinates.get(i).column(), coordinates.get(i+1).column()));
            }
        });

        long capacity = 0;
        long previousRow = 0;
        long currentLiveSize = 0;
        List<Interval> currentLiveIntervals = new ArrayList<>();
        for (Map.Entry<Long, List<Interval>> entry : intervalsByRow.entrySet()) {
            long rowNumber = entry.getKey();
            
            // new row, so add on (current live size) * (empty rows)
            long emptyRows = rowNumber - previousRow - 1;
            capacity += emptyRows * currentLiveSize;
            // System.out.println("Adding " + emptyRows + " rows of size " + currentLiveSize + " each: " + emptyRows * currentLiveSize);
            previousRow = rowNumber;

            // process intervals
            for (Interval newInterval : entry.getValue()) {
                long correction = newInterval.update(currentLiveIntervals);
                Collections.sort(currentLiveIntervals);
                // System.out.println("Adding a correction value of " + correction);
                capacity += correction;
            }

            // work out new size
            currentLiveSize = currentLiveIntervals.stream().map(Interval::size).reduce(0L, (long1, long2) -> long1 + long2);
            // System.out.println("Adding current live size of " + currentLiveSize);
            capacity += currentLiveSize;
        }

        return String.valueOf(capacity);
    }

    public static record Interval(long start, long end) implements Comparable<Interval> {
       
        /**
         * @return the number of integers contained by this interval, including the start and end points
         */
        long size() {
            return end - start + 1;
        }

        /**
         * @return true if the other interval adjoins or is contained by this interval, false otherwise
         * <p>
         * Note that this function will return false if the other interval wholly contains this one, since it's not relevant to our scenario
         */
        boolean contains(Interval otherInterval) {
            return (this.start <= otherInterval.start && otherInterval.start <= this.end)
                || (this.start <= otherInterval.end && otherInterval.end <= this.end);
        }

        /**
         * Update a list of intervals with a new interval.
         * @param intervals
         * @return a correction value if we subtracted interval, 0 otherwise
         */
        long update(List<Interval> intervals) {
            List<Interval> overlaps = intervals.stream().filter(interval -> interval.contains(this)).toList();
            if (overlaps.isEmpty()) {
                intervals.add(this);
                return 0L;
            }
            if (overlaps.size() == 2) {
                // only possible geometrically if it's joining two intervals, I think
                if (overlaps.get(0).end != this.start || this.end != overlaps.get(1).start) {
                    System.err.println("overlaps: " + overlaps + ", this: " + this);
                    throw new IllegalStateException();
                }
                intervals.removeAll(overlaps);
                intervals.add(new Interval(overlaps.get(0).start, overlaps.get(1).end));
                return 0L;
            }
            // overlaps size is 1
            Interval overlapper = overlaps.get(0);
            intervals.remove(overlapper);
            if (this.equals(overlapper)) {
                return this.size();
            } else if (this.start == overlapper.end) {
                intervals.add(new Interval(overlapper.start, this.end));
                return 0L;
            } else if (this.end == overlapper.start) {
                intervals.add(new Interval(this.start, overlapper.end));
                return 0L;
            } else if (this.start == overlapper.start) {
                intervals.add(new Interval(this.end, overlapper.end));
                return this.size() - 1;
            } else if (this.end == overlapper.end) {
                intervals.add(new Interval(overlapper.start, this.start));
                return this.size() - 1;
            } else {
                intervals.add(new Interval(overlapper.start, this.start));
                intervals.add(new Interval(this.end, overlapper.end));
                return this.size() - 2;
            }
        }

        @Override
        public int compareTo(Interval o) {
            if (this.start == o.start) {
                return Long.compare(this.end, o.end);
            }
            return Long.compare(this.start, o.start);
        }
    }
}
