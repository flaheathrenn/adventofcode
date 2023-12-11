package day11;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.LongStream;

public class Accumulator {
    // State
    List<Galaxy> galaxies = new ArrayList<>();
    List<Long> emptyRows = new ArrayList<>();
    List<Long> emptyColumns = new ArrayList<>();
    long currentRow = 0;

    // Update state from parsed line
    public Accumulator update(ParsedLine parsedLine) {
        if (parsedLine.galaxyIndexes.isEmpty()) {
            emptyRows.add(currentRow);
        } else {
            galaxies.addAll(parsedLine.galaxyIndexes.stream().map(x -> new Galaxy(currentRow, x)).toList());
        }
        currentRow++;
        return this;
    }

    // Extract solution
    public String star1() {

        // Calculate which columns are empty
        long maxColumn = galaxies.stream().mapToLong(Galaxy::getColumn).max().orElse(0);
        List<Long> populatedColumns = galaxies.stream().map(Galaxy::getColumn).distinct().toList();
        LongStream.range(0, maxColumn).filter(x -> !populatedColumns.contains(x)).forEach(emptyColumns::add);

        for (Galaxy g : galaxies) {
            g.expandUniverse(emptyRows, emptyColumns);
        }

        long acc = 0;
        for (Galaxy g : galaxies) {
            for (Galaxy o : galaxies) {
                acc += g.distance(o);
            }
        }

        // we're doing each pair twice so divide sum by two
        return String.valueOf(acc / 2);
    }
}
