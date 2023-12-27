package day23;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import day23.GridUtils.GridCoordinate;

public class Accumulator {
    // State
    List<String[]> rows = new ArrayList<>();

    // Update state from parsed line
    public Accumulator update(ParsedLine parsedLine) {
        rows.add(parsedLine.row);
        return this;
    }

    // Extract solution
    public String star1() {
        // Create grid
        String[][] grid = rows.toArray(new String[rows.size()][rows.get(0).length]);

        // Find entry point
        GridCoordinate entryPoint = new GridCoordinate(0, String.join("", grid[0]).indexOf("."));

        Set<Path> allPaths = new HashSet<>();
        
        // TODO: traverse grid, finding all junctions and paths
        // TODO: recursively 

        return "";
    }

    public static record Path(int length, GridCoordinate endpoint) {}

    public static record Junction(GridCoordinate location, Set<Path> exitPaths) {}
}
