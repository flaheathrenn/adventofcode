package aoc2024.day13;

import global.InputProcessor;

/**
 * This program treats a machine as ending when the blank line after it is read in,
 * so an input file will need to end with two line breaks (i.e. one blank line after the machine and then
 * a linebreak to terminate the file)
 */
public class Solver {
    
    public static void main(String[] args) {
        // Set up any initial state

        // Process the input line-by-line
        InputProcessor processor = new InputProcessor("src/aoc2024/day13/input.txt");
        Accumulator result = processor.processLines(ParsedLine::new, (parsedLine, accumulator) -> {
            // Update accumulator using parsed line object
            return accumulator.update(parsedLine);
        }, new Accumulator());
        System.out.println("Star 2 solution: " + result.star2());
    }
}
