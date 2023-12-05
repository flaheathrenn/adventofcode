package day4;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ParsedLine {

    // State
    int points = 0;
    int cardNumber;
    int numberOfMatches;

    // Parsing
    public ParsedLine(String line) {
        // Example line: Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
        String[] splitLineOnColon = line.split(":");
        cardNumber = Integer.parseInt(splitLineOnColon[0].replaceAll("Card +", ""));
        String[] inputs = splitLineOnColon[1].split("\\|");
        List<Integer> winningNumbers = parseNumbers(inputs[0]);
        Set<Integer> numbersIHave = new HashSet<>(parseNumbers(inputs[1]));
        numbersIHave.retainAll(winningNumbers);
        numberOfMatches = numbersIHave.size();
        if (!numbersIHave.isEmpty()) {
           points = (int) Math.pow(2.0, numberOfMatches - 1);
        }
    }

    private static List<Integer> parseNumbers(String numbers) {
        return Arrays.stream(numbers.split(" +")).filter(s -> !s.isBlank()).map(Integer::parseInt).collect(Collectors.toList());
    }

}