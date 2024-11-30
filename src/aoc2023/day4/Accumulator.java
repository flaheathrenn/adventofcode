package aoc2023.day4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Accumulator {
    // State
    List<ParsedLine> lineList = new ArrayList<>();

    // Update state from parsed line
    public Accumulator update(ParsedLine parsedLine) {
        lineList.add(parsedLine);
        return this;
    }

    // Extract solution
    public String solution() {
        Collections.reverse(lineList);
        int numberOfCards = 0;
        List<Integer> lastTen = new ArrayList<>();
        for (ParsedLine card : lineList) {
            int cards = 1; // for this card
            for (int i = 0; i < card.numberOfMatches; i++) {
                cards += lastTen.get(i);
            }
            lastTen.add(0, cards);
            numberOfCards += cards;
        }
        return String.valueOf(numberOfCards);
    }
}
