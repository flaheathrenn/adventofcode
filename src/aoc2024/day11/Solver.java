package aoc2024.day11;

import global.InputProcessor;

public class Solver {

    public static void main(String[] args) {
        // Set up any initial state

        // Process the input line-by-line
        InputProcessor processor = new InputProcessor("src/aoc2024/day11/input.txt");
        Accumulator result = processor.processLines(ParsedLine::new, (parsedLine, accumulator) -> {
            // Update accumulator using parsed line object
            return accumulator.update(parsedLine);
        }, new Accumulator());
        System.out.println("Star 1 solution: " + result.star(25));
        System.out.println("Star 2 solution: " + result.star(50)); // 25 + 50 = 75
    }
}
