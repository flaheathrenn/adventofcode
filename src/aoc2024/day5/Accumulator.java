package aoc2024.day5;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import aoc2024.day5.ParsedLine.Rule;

public class Accumulator {
    // State
    Set<Rule> rules = new HashSet<>();
    int solution = 0;

    // Update state from parsed line
    public Accumulator update(ParsedLine parsedLine) {
        if (parsedLine.isRule) {
            rules.add(parsedLine.rule);
        }
        if (parsedLine.isPages) {
            if (isSorted(parsedLine.pages, rules)) {
                // update pages are in correct order
                String middle = parsedLine.pages.get(parsedLine.pages.size() / 2);
                solution += Integer.parseInt(middle);
            }
        }
        return this;
    }

    private boolean isSorted(List<String> pages, Set<Rule> rules) {
        for (int i = 0; i < pages.size(); i++) {
            String left = pages.get(i);
            for (int j = i + 1; j < pages.size(); j++) {
                String right = pages.get(j);
                if (rules.contains(new Rule(right, left))) {
                    return false;
                }
            }
        }
        return true;
    }

    // Extract solution
    public String star1() {
        return String.valueOf(solution);
    }
}
