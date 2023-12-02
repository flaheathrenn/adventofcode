package day2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class day2 {
    public static void main(String[] args) {
        final String filename = "src/day2/input.txt";
        try (BufferedReader inputFileReader = new BufferedReader(new FileReader(filename))) {
            Bag bag = new Bag(12, 13, 14);
            int idsAcc = 0;
            int powerAcc = 0;
            for (String inputLine = inputFileReader.readLine(); inputLine != null; inputLine = inputFileReader.readLine()) {
                ParsedLine parsedLine = parseLine(inputLine);
                if (bag.canContain(parsedLine.bag())) {
                    idsAcc += parsedLine.lineId();
                }
                powerAcc += parsedLine.bag().power();
            }
            System.out.println("Sum of all valid line IDs:");
            System.out.println(idsAcc);
            System.out.println("Sum of all minimum bag powers:");
            System.out.println(powerAcc);
            System.exit(0);
        } catch (IOException e) {
            System.err.println("Exception reading file " + filename + ":");
            e.printStackTrace();
            System.exit(1);
        }
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
