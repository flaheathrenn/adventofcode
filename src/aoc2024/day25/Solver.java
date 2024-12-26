package aoc2024.day25;

import global.InputProcessor;

public class Solver {
    
    /**
     * Preprocessing: need to add two newlines to the input
     */
    public static void main(String[] args) {
        // Set up any initial state

        // Process the input line-by-line
        InputProcessor processor = new InputProcessor("src/aoc2024/day25/input.txt");
        Accumulator result = processor.processLines(ParsedLine::new, (parsedLine, accumulator) -> {
            // Update accumulator using parsed line object
            return accumulator.update(parsedLine);
        }, new Accumulator());
        System.out.println("Star 1 solution: " + result.star1());
    }
}
