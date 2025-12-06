package aoc2025.day6;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Accumulator {
    // State
    List<Long>[] columns;
    long star1sol = 0;

    List<String[]> rawContents = new ArrayList<>();

    // Update state from parsed line
    public Accumulator update(ParsedLine parsedLine) {
        rawContents.add(parsedLine.rawContents);
        if (columns == null) {
            columns = new List[parsedLine.contents.length];
            for (int i = 0; i < columns.length; i++) {
                columns[i] = new ArrayList<>();
            }
        }
        if (parsedLine.contents[0].matches("[+*]")) {
            for (int i = 0; i < columns.length; i++) {
                switch (parsedLine.contents[i]) {
                    case "+":
                        star1sol += columns[i].stream().mapToLong(l -> l).sum();
                        break;
                    case "*":
                        star1sol += columns[i].stream().mapToLong(l -> l).reduce(1L, (l1, l2) -> l1 * l2);
                        break;
                    default:
                        throw new IllegalArgumentException();
                }
            }
        } else {
            // numeric
            for (int i = 0; i < columns.length; i++) {
                columns[i].add(Long.parseLong(parsedLine.contents[i]));
            }
        }
        return this;
    }

    // Extract solution
    public String star1() {
        return Long.toString(star1sol);
    }

    public String star2() {
        long star2sol = 0;
        String[][] grid = rawContents.toArray(new String[rawContents.size()][]);
        String problemOperator = null;
        List<Long> problemNumbers = new ArrayList<>();
        for (int columnIndex = 0; columnIndex < grid[0].length; columnIndex++) {
            boolean allBlank = true;
            long currentNumber = 0;
            for (int rowIndex = 0; rowIndex < grid.length; rowIndex++) {
                if (!grid[rowIndex][columnIndex].equals(" ")) {
                    allBlank = false;
                    if (grid[rowIndex][columnIndex].matches("[+*]")) {
                        problemOperator = grid[rowIndex][columnIndex];
                    } else {
                        currentNumber = (currentNumber * 10) + Long.parseLong(grid[rowIndex][columnIndex]);
                    }
                }
            }
            if (!allBlank) {
                problemNumbers.add(currentNumber);
            }
            if (allBlank || columnIndex == grid[0].length - 1) {
                // end of a 'problem' so calculate
                System.out.printf("Calculating %s of %s\n", problemOperator, problemNumbers.stream().map(String::valueOf).collect(Collectors.joining(",", "[", "]")));
                switch (problemOperator) {
                    case "+":
                        star2sol += problemNumbers.stream().mapToLong(l -> l).sum();
                        break;
                    case "*":
                        star2sol += problemNumbers.stream().mapToLong(l -> l).reduce(1L, (l1, l2) -> l1 * l2);
                        break;
                    case null:
                    default:
                        throw new IllegalArgumentException();
                }
                problemOperator = null;
                problemNumbers.clear();
            }
        }
        return Long.toString(star2sol);
    }
}
