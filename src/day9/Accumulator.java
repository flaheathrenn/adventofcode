package day9;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.naming.spi.DirStateFactory;

public class Accumulator {
    // State
    // this has to be initialised with as many 0s as the length of the input sequences, sorry
    List<Long> polynomial = new ArrayList<>(List.of(0L,0L,0L,0L,0L,0L,0L,0L,0L,0L,0L,0L,0L,0L,0L,0L,0L,0L,0L,0L,0L));
    // List<Long> polynomial = new ArrayList<>(List.of(0L,0L,0L,0L,0L,0L));

    // Update state from parsed line
    public Accumulator update(ParsedLine parsedLine) {
        // The sum of two polynomials is a polynomial of at most the same order,
        // so if we add together all our input sequences...
        for (int i = 0; i < parsedLine.integers.size(); i++) {
            polynomial.set(i, polynomial.get(i) + parsedLine.integers.get(i));
        }
        return this;
    }

    // Extract solution
    public String star1() {
        // ... we can just do the method of differences once on the resulting sequence
        List<List<Long>> differencesList = new ArrayList<>();
        differencesList.add(polynomial);
        List<Long> differences = List.copyOf(polynomial);
        while (true) {
            differences = differences(differences);
            differencesList.add(differences);
            if (differences.stream().allMatch(l -> l == 0L)) {
                break;
            }
        }
        Collections.reverse(differencesList);
        long acc = 0L;
        for (List<Long> diffs : differencesList) {
           acc = acc + diffs.get(diffs.size() - 1); 
        }
        return String.valueOf(acc);
    }

    public List<Long> differences(List<Long> input) {
        List<Long> result = new ArrayList<Long>();
        for (int i = 0; i < input.size() - 1; i++) {
            result.add(input.get(i + 1) - input.get(i));
        }
        return result;
    }
}
