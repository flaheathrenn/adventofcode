package aoc2024.day25;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

public class Accumulator {
    // State
    Set<List<Integer>> keys = new HashSet<>();
    Set<List<Integer>> locks = new HashSet<>();

    List<String> currentThing = new ArrayList<>();

    // Update state from parsed line
    public Accumulator update(ParsedLine parsedLine) {
        if (parsedLine.isBlank) {
            // reached end of thing
            String[] currentEntity = currentThing.toArray(new String[currentThing.size()]);
            List<Integer> currentEntityHeights = new ArrayList<>();
            if (currentEntity[0].startsWith("#")) {
                // lock
                for (int i = 0; i < currentEntity[0].length(); i++) {
                    int size = -1;
                    for (int j = 0; j < currentEntity.length; j++) {
                        if (currentEntity[j].charAt(i) == '#') {
                            size++;
                        } else {
                            break;
                        }
                    }
                    currentEntityHeights.add(size);
                }
                locks.add(currentEntityHeights);
            } else if (currentEntity[0].startsWith(".")) {
                // key
                for (int i = 0; i < currentEntity[0].length(); i++) {
                    int size = -1;
                    for (int j = currentEntity.length - 1; j >= 0; j--) {
                        if (currentEntity[j].charAt(i) == '#') {
                            size++;
                        } else {
                            break;
                        }
                    }
                    currentEntityHeights.add(size);
                }
                keys.add(currentEntityHeights);
            }
            // clear current thing
            currentThing = new ArrayList<>();
        } else {
            currentThing.add(parsedLine.line);
        }
        return this;
    }

    // Extract solution
    public String star1() {
        long solution = 0;
        for (List<Integer> lock : locks) {
            k: for (List<Integer> key : keys) {
                for (int i = 0; i < lock.size(); i++) {
                    if (lock.get(i) + key.get(i) > 5) {
                        continue k;
                    }
                }
                solution++;
            }
        }
        return Long.toString(solution);
    }
}
