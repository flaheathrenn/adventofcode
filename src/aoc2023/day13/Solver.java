package aoc2023.day13;

import global.InputProcessor;

public class Solver {
    
    public static void main(String[] args) {
        // Set up any initial state

        // Process the input line-by-line
        // NOTE: required preprocessing: add two newlines to the end of input
        InputProcessor processor = new InputProcessor("src/aoc2023/day13/input.txt");
        Accumulator result = processor.processLines(ParsedLine::new, (parsedLine, accumulator) -> {
            // Update accumulator using parsed line object
            return accumulator.update(parsedLine);
        }, new Accumulator());
        System.out.println("Star 1 solution: " + result.star1());
        System.out.println("Star 2 solution: " + result.star2());
    }
}
