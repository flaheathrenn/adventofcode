package aoc2024.day16;

import global.InputProcessor;

public class Solver {
    
    public static void main(String[] args) {
        // Set up any initial state

        // Process the input line-by-line
        InputProcessor processor = new InputProcessor("src/aoc2024/day16/input.txt");
        Accumulator result = processor.processLines(ParsedLine::new, (parsedLine, accumulator) -> {
            // Update accumulator using parsed line object
            return accumulator.update(parsedLine);
        }, new Accumulator());
        // Accumulator#star2 will print out the star 1 solution on the way
        System.out.println("Star 2 solution: " + result.star2());
    }
}
