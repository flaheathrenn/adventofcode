package day8;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Solver {
    
    public static void main(String[] args) {
        // Set up any initial state
        Accumulator result = new Accumulator();

        // Process the input line-by-line
        try (BufferedReader inputFileReader = new BufferedReader(new FileReader("src/day8/input.txt"))) {
            String firstLine = inputFileReader.readLine();
            inputFileReader.readLine(); // skip blank line
            for (String inputLine = inputFileReader.readLine(); inputLine != null; inputLine = inputFileReader.readLine()) {
                ParsedLine parsedLine = new ParsedLine(inputLine);
                result = result.update(parsedLine);
            }
            System.out.println("Star 1 solution: " + result.star1(firstLine, "AAA"));
            // it so happens that AAA reaches ZZZ before any other ending-in-Z nodes
            System.out.println("Star 2 solution: " + result.star2(firstLine));
        } catch (IOException e) {
            System.err.println("Exception reading file:");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
