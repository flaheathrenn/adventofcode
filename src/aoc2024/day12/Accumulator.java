package aoc2024.day12;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import global.GridUtils.GridCoordinate;

public class Accumulator {
    // State
    List<char[]> rows = new ArrayList<>();

    Map<GridCoordinate, Long> regionForGridCoordinate = new HashMap<>();
    Map<Long, Set<GridCoordinate>> gridCoordinatesForRegion = new HashMap<>();
    Map<Long, RegionStats> regionStatsDb = new HashMap<>();

    // Update state from parsed line
    public Accumulator update(ParsedLine parsedLine) {
        rows.add(parsedLine.row);
        return this;
    }

    public void init() {
        char[][] grid = rows.toArray(new char[0][0]);
        SecureRandom regionIdGen = new SecureRandom();
        
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                char charAt = grid[i][j];
                boolean equalsLeft = j != 0 && charAt == grid[i][j-1];
                boolean equalsUp = i != 0 && charAt == grid[i-1][j];
                boolean equalsUpAndLeft = i != 0 && j != 0 && charAt == grid[i-1][j-1];
                boolean equalsUpAndRight = i != 0 && j != grid[i].length - 1 && charAt == grid[i-1][j+1];
                if (!equalsLeft && !equalsUp) {
                    // new region
                    long newRegionId = regionIdGen.nextLong();
                    regionForGridCoordinate.put(new GridCoordinate(i, j), newRegionId);
                    gridCoordinatesForRegion.put(newRegionId, new HashSet<>());
                    gridCoordinatesForRegion.get(newRegionId).add(new GridCoordinate(i, j));
                    regionStatsDb.put(newRegionId, new RegionStats(1, 4));
                } else if (equalsLeft && !equalsUp) {
                    // add to region of left
                    long oldRegionId = regionForGridCoordinate.get(new GridCoordinate(i, j-1));
                    regionForGridCoordinate.put(new GridCoordinate(i, j), oldRegionId);
                    gridCoordinatesForRegion.get(oldRegionId).add(new GridCoordinate(i, j));
                    regionStatsDb.get(oldRegionId).addOneSpace();
                } else if (!equalsLeft && equalsUp) {
                    // add to region of up
                    long oldRegionId = regionForGridCoordinate.get(new GridCoordinate(i-1, j));
                    regionForGridCoordinate.put(new GridCoordinate(i, j), oldRegionId);
                    gridCoordinatesForRegion.get(oldRegionId).add(new GridCoordinate(i, j));
                    regionStatsDb.get(oldRegionId).addOneSpace();
                } else { // (equalsLeft && equalsUp: the tricky case)
                    long oldRegionIdUp = regionForGridCoordinate.get(new GridCoordinate(i-1, j));
                    long oldRegionIdLeft = regionForGridCoordinate.get(new GridCoordinate(i, j-1));
                    long newRegionId = mergeRegionCoordinates(oldRegionIdUp, oldRegionIdLeft);
                    regionForGridCoordinate.put(new GridCoordinate(i, j), newRegionId);
                    gridCoordinatesForRegion.get(newRegionId).add(new GridCoordinate(i, j));
                    if (oldRegionIdLeft != oldRegionIdUp) {
                        regionStatsDb.get(oldRegionIdUp).addRegion(regionStatsDb.get(oldRegionIdLeft));
                        regionStatsDb.remove(oldRegionIdLeft);
                    } else {
                        regionStatsDb.get(oldRegionIdUp).addOneSpaceNoPerim();
                    }
                }

                // star 2 stuff
                RegionStats myRegion = regionStatsDb.get(regionForGridCoordinate.get(new GridCoordinate(i, j)));
                if (!equalsLeft && !equalsUp) {
                    myRegion.sides = 4; // initialise to four sides
                } else if (equalsUp && !equalsLeft && !equalsUpAndLeft && !equalsUpAndRight) {
                    // noop
                } else if (!equalsUp && equalsLeft && !equalsUpAndLeft) {
                    // noop
                } else if (equalsUp && equalsLeft && !equalsUpAndRight) {
                    myRegion.addSides(-2);
                } else if (equalsUp && equalsLeft && equalsUpAndRight) {
                    // noop
                } else if (equalsUp && !equalsLeft && equalsUpAndRight && equalsUpAndLeft) {
                    myRegion.addSides(4);
                } else if (equalsUp && !equalsLeft && !equalsUpAndRight && equalsUpAndLeft) {
                    myRegion.addSides(2);
                } else if (!equalsUp && equalsLeft && equalsUpAndLeft) {
                    myRegion.addSides(2);
                } else if (equalsUp && !equalsLeft && equalsUpAndRight && !equalsUpAndLeft) {
                    myRegion.addSides(2);
                } else {
                    System.out.println("Uncovered branch: " + (equalsLeft ? "0" : "1") + (equalsUp ? "0" : "1") + (equalsUpAndLeft ? "0" : "1") + (equalsUpAndRight ? "0" : "1"));
                }
            }
        }
    }

    // Extract solution
    public String star1() {
        long totalPrice = 0;
        for (RegionStats entry : regionStatsDb.values()) {
            // System.out.println("Region has area " + entry.area + " and perimeter " + entry.perimeter);
            totalPrice += entry.area * entry.perimeter;
        }
        return Long.toString(totalPrice);
    }

    public String star2() {
        long totalPrice = 0;
        for (RegionStats entry : regionStatsDb.values()) {
            // System.out.println("Region has area " + entry.area + " and sides " + entry.sides);
            totalPrice += entry.area * entry.sides;
        }
        return Long.toString(totalPrice);
    }

    private long mergeRegionCoordinates(long region, long otherRegion) {
        if (region == otherRegion) {
            return region;
        }
        for (GridCoordinate gc : gridCoordinatesForRegion.get(otherRegion)) {
            regionForGridCoordinate.put(gc, region);
            gridCoordinatesForRegion.get(region).add(gc);
        }
        gridCoordinatesForRegion.remove(otherRegion);
        return region;
    }

    public static class RegionStats {
        private int area = 0;
        private int perimeter = 0;
        private int sides = 0;

        public RegionStats(int area, int perimeter) {
            this.area = area;
            this.perimeter = perimeter;
        }

        public void addSides(int add) {
            this.sides += add;
        }

        public void addOneSpace() {
            // eg XX -> XXX
            area++;
            perimeter += 2;
        }

        public void addOneSpaceNoPerim() {
            // eg XX -> XX
            //    X  -> XX
            area++;
        }

        public void addRegion(RegionStats other) {
            // eg
            // .X -> .X
            // Y  -> XX
            this.area = this.area + other.area + 1;
            this.perimeter = this.perimeter + other.perimeter;
            this.sides = this.sides + other.sides;
        }
    }
}
