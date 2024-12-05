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
    int star1solution = 0;
    int star2solution = 0;

    // Update state from parsed line
    public Accumulator update(ParsedLine parsedLine) {
        if (parsedLine.isRule) {
            rules.add(parsedLine.rule);
        }
        if (parsedLine.isPages) {
            List<String> sortedPages = new ArrayList<>(parsedLine.pages);
            sort(sortedPages, rules);
            if (sortedPages.equals(parsedLine.pages)) {
                // update pages are in correct order
                String middle = parsedLine.pages.get(parsedLine.pages.size() / 2);
                star1solution += Integer.parseInt(middle);
            } else {
                String middle = sortedPages.get(sortedPages.size() / 2);
                star2solution += Integer.parseInt(middle);
            }
        }
        return this;
    }

    private void sort(List<String> pages, Set<Rule> rules) {
        for (int i = 0; i < pages.size(); i++) {
            String left = pages.get(i);
            for (int j = i + 1; j < pages.size(); j++) {
                String right = pages.get(j);
                if (rules.contains(new Rule(right, left))) {
                    // swap elements
                    pages.remove(i);
                    pages.add(i, right);
                    pages.remove(j);
                    pages.add(j, left);
                    sort(pages, rules);
                    return;
                }
            }
        }
    }

    // Extract solution
    public String star1() {
        return String.valueOf(star1solution);
    }

    public String star2() {
        return String.valueOf(star2solution);
    }
}
