package aoc2024.day5;

import java.util.ArrayList;
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
        pages.sort((x, y) -> {
            if (rules.contains(new Rule(x,y))) {
                return -1;
            }
            if (rules.contains(new Rule(y,x))) {
                return 1;
            }
            // if the following was ever printed it would indicate a potential issue
            System.out.println("Relationship between " + x + " and " + y + " is ambiguous");
            return 0;
        });
    }

    // Extract solution
    public String star1() {
        return String.valueOf(star1solution);
    }

    public String star2() {
        return String.valueOf(star2solution);
    }
}
