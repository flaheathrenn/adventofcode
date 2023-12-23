package day21;

import global.InputProcessor;

public class Solver {

    public static void main(String[] args) {
        // Set up any initial state

        // Process the input line-by-line
        InputProcessor processor = new InputProcessor("src/day21/input.txt");
        AccumulatorForStar2 result = processor.processLines(ParsedLine::new, (parsedLine, accumulator) -> {
            // Update accumulator using parsed line object
            return accumulator.update(parsedLine);
        }, new AccumulatorForStar2());
        for (long stepCount = 26501365; stepCount < 26501366; stepCount++) {
            String newMethod = result.star2(stepCount);
            System.out.println("Star 2 solution: " + newMethod);
        }
    }
}
