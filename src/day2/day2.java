package day2;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import global.InputProcessor;

public class day2 {
    public static void main(String[] args) {
        Bag limitingBag = new Bag(12, 13, 14);
        InputProcessor processor = new InputProcessor("src/day2/input.txt");
        RunningTotals finalTotals = processor.processLines(day2::parseLine, (parsedLine, runningTotals) -> {
            if (limitingBag.canContain(parsedLine.bag())) {
                runningTotals.idsAccumulator += parsedLine.lineId();
            }
            runningTotals.powerAccumulator += parsedLine.bag().power();
            return runningTotals;
        }, new RunningTotals());
        System.out.println("Star 1 solution: " + finalTotals.idsAccumulator);
        System.out.println("Star 2 solution: " + finalTotals.powerAccumulator);
    }

    private static class RunningTotals {
        int idsAccumulator = 0;
        int powerAccumulator = 0;
    }

    private static ParsedLine parseLine(String inputLine) {
        // example: Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
        String[] splitLine = inputLine.split(": ");

        // Search part before the colon for game ID
        Pattern gamePattern = Pattern.compile("Game (\\d+)");
        Matcher gameMatcher = gamePattern.matcher(splitLine[0]);
        gameMatcher.matches();
        int gameId = Integer.parseInt(gameMatcher.group(1));

        // Search part after the colon for ball quantities
        Bag bag = new Bag(0,0,0);
        Pattern ballPattern = Pattern.compile("(\\d+) (red|blue|green)");
        Matcher ballMatcher = ballPattern.matcher(splitLine[1]);
        while (ballMatcher.find()) {
            int ballQuantity = Integer.parseInt(ballMatcher.group(1));
            switch (ballMatcher.group(2)) {
                case "red" -> bag.update(ballQuantity, 0, 0);
                case "green" -> bag.update(0, ballQuantity, 0);
                case "blue" -> bag.update(0, 0, ballQuantity);
            }
        }
        return new ParsedLine(gameId, bag);
    }

}
