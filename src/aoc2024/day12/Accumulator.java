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

    /**
     * Basic idea: iterate through the grid in rows, at each stage working out what region the new square is in,
     * and updating that region's area, perimeter, and number of sides accordingly.
     * We only ever use knowledge about cells we've already examined (cells on rows above, or to the left on the current row);
     * as long as our adjustments are correct at each step, the end result will be correct.
     */
    public void init() {
        char[][] grid = rows.toArray(new char[0][0]);
        SecureRandom regionIdGen = new SecureRandom();
        // Refer to regions using long IDs rather than their character, since characters can be reused.
        
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                char charAt = grid[i][j];
                // Examine the new square's immediate neighbourhood
                boolean equalsLeft = j != 0 && charAt == grid[i][j-1];
                boolean equalsUp = i != 0 && charAt == grid[i-1][j];
                boolean equalsUpAndLeft = i != 0 && j != 0 && charAt == grid[i-1][j-1];
                boolean equalsUpAndRight = i != 0 && j != grid[i].length - 1 && charAt == grid[i-1][j+1];
                if (!equalsLeft && !equalsUp) {
                    // As far as we currently know, this is a new region, so create one
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
                    // First, merge the region above and the region to the left
                    long oldRegionIdUp = regionForGridCoordinate.get(new GridCoordinate(i-1, j));
                    long oldRegionIdLeft = regionForGridCoordinate.get(new GridCoordinate(i, j-1));
                    long newRegionId = mergeRegionCoordinates(oldRegionIdUp, oldRegionIdLeft);
                    regionForGridCoordinate.put(new GridCoordinate(i, j), newRegionId);
                    gridCoordinatesForRegion.get(newRegionId).add(new GridCoordinate(i, j));
                    if (oldRegionIdLeft != oldRegionIdUp) {
                        // the two regions were different, so update their stats accordingly
                        // (including adding together their number of sides - we know they aren't adjacent
                        // so none of their sides need to be merged)
                        regionStatsDb.get(oldRegionIdUp).addRegion(regionStatsDb.get(oldRegionIdLeft));
                        regionStatsDb.remove(oldRegionIdLeft);
                    } else {
                        // the two regions were actually the same, so increase area by one
                        // (number of sides will be dealt with later)
                        regionStatsDb.get(oldRegionIdUp).addOneSpaceNoPerim();
                    }
                }

                /* Star 2 stuff
                 * 
                 * Take a look at this example region:
                 * 
                 * XXX
                 *  XX
                 * 
                 * In our traversal, we will see this region in a number of snapshots: first as X, then XX, then XXX,
                 * then XXX, and finally XXX - the true shape of the region.
                 *       X                XX
                 * If we were to calculate the number of sides of each snapshot of this region, the sequence will be
                 * 444
                 *  86
                 * 
                 * Some more complicated examples:
                 * 
                 * 444
                 * 664
                 * 664
                 * 
                 * 444
                 * 6 8
                 * 8X8 (X = 10)
                 *
                 * From examining these we can construct a rule that examines the four neighbours to the left, above,
                 * above-left and above-right, and uses those to work out how adding this new square affects the number
                 * of sides the region has - bearing in mind that adding more squares to the right and below
                 * later on will have additional effects.
                 * 
                 * Anyway that's what this if statement below does.
                 */
                RegionStats myRegion = regionStatsDb.get(regionForGridCoordinate.get(new GridCoordinate(i, j)));
                if (!equalsLeft && !equalsUp) {
                    myRegion.sides = 4; // initialise to four sides
                } else if (equalsUp && !equalsLeft && !equalsUpAndLeft && !equalsUpAndRight) { // .X. -> .X.
                    // noop                                                                    // .      .X
                } else if (!equalsUp && equalsLeft && !equalsUpAndLeft) { // ..? -> ..?
                    // noop                                               // X      XX
                } else if (equalsUp && equalsLeft && !equalsUpAndRight) {               // ?X. -> ?X.
                    myRegion.addSides(-2);                                              // X      XX
                } else if (equalsUp && equalsLeft && equalsUpAndRight) { // ?XX -> ?XX
                    // noop                                              // X      XX
                } else if (equalsUp && !equalsLeft && equalsUpAndRight && equalsUpAndLeft) { // XXX -> XXX
                    myRegion.addSides(4);                                                // .      .X
                } else if (equalsUp && !equalsLeft && !equalsUpAndRight && equalsUpAndLeft) {              // XX. -> XX.
                    myRegion.addSides(2);                                                              // .      .X  
                } else if (!equalsUp && equalsLeft && equalsUpAndLeft) { //  X.? -> X.?
                    myRegion.addSides(2);                            //  X   -> XX
                } else if (equalsUp && !equalsLeft && equalsUpAndRight && !equalsUpAndLeft) { // .XX -> .XX
                    myRegion.addSides(2);                                                 // .   -> .X
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
