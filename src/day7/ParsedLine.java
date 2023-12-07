package day7;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ParsedLine implements Comparable<ParsedLine> {

    // private static final String CARD_RANKING = "AKQJT98765432";
    private static final String CARD_RANKING = "AKQT98765432J";

    // State
    String hand;
    HandType type;
    int bid;

    // Parsing
    public ParsedLine(String line) {
        String[] components = line.split(" ");
        this.hand = components[0];
        this.bid = Integer.parseInt(components[1]);

        // // parse type
        // List<Integer> counts = new ArrayList<>(Arrays.stream(this.hand.split(""))
        //     .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
        //     .values()
        //     .stream()
        //     .map(Long::intValue)
        //     .toList());

        // parse type, ignoring Js
        List<Integer> counts = new ArrayList<>(Arrays.stream(this.hand.split(""))
            .filter(s -> !s.equals("J"))
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
            .values()
            .stream()
            .map(Long::intValue)
            .sorted()
            .toList());

        if (counts.isEmpty()) {
            counts.add(0); // deal with all-J case
        }

        // count J as whatever there's most of
        counts.set(counts.size()-1, counts.get(counts.size()-1) + this.hand.replaceAll("[^J]","").length());

        System.out.println("Hand " + hand + " parsed into counts " + counts.stream().map(String::valueOf).collect(Collectors.joining(",")));

        if (counts.equals(List.of(5))) {
            this.type = HandType.FIVEOFAKIND;
        }
        if (counts.equals(List.of(1,4))) {
            this.type = HandType.FOUROFAKIND;
        }
        if (counts.equals(List.of(2,3))) {
            this.type = HandType.FULLHOUSE;
        }
        if (counts.equals(List.of(1,1,3))) {
            this.type = HandType.THREEOFAKIND;
        }
        if (counts.equals(List.of(1,2,2))) {
            this.type = HandType.TWOPAIR;
        }
        if (counts.equals(List.of(1,1,1,2))) {
            this.type = HandType.ONEPAIR;
        }
        if (counts.equals(List.of(1,1,1,1,1))) {
            this.type = HandType.HIGHCARD;
        }

        System.out.println("Hand " + hand + " evaluated as " + type);
    }

    @Override
    public int compareTo(ParsedLine other) {
        int strengthCompare = Integer.compare(this.type.strength, other.type.strength);    
        if (strengthCompare != 0) {
            return strengthCompare;
        }
        for (int i = 0; i < this.hand.length(); i++) {
            int cardStrengthCompare = -Integer.compare(
                CARD_RANKING.indexOf(this.hand.charAt(i)),
                CARD_RANKING.indexOf(other.hand.charAt(i)));
            if (cardStrengthCompare != 0) {
                return cardStrengthCompare;
            }
        }
        return 0;
    }

}