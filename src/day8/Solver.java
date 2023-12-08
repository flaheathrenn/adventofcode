package day8;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.function.BiFunction;
import java.util.function.Function;

import global.InputProcessor;

public class Solver {
    
    public static void main(String[] args) {
        // Set up any initial state
        Accumulator result = new Accumulator();

        // Process the input line-by-line
        try (BufferedReader inputFileReader = new BufferedReader(new FileReader("src/day8/input.txt"))) {
            String firstLine = inputFileReader.readLine();
            inputFileReader.readLine(); // skip blank line
            for (String inputLine = inputFileReader.readLine(); inputLine != null; inputLine = inputFileReader.readLine()) {
                System.out.println("Parsing line " + inputLine);
                ParsedLine parsedLine = new ParsedLine(inputLine);
                System.out.println("Parsed as " + parsedLine.beginning + ", " + parsedLine.left + ", " + parsedLine.right);
                result = result.update(parsedLine);
            }
            System.out.println("Star 1 solution: " + result.star1(firstLine));
        } catch (IOException e) {
            System.err.println("Exception reading file:");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
