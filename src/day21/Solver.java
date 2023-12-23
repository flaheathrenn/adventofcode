package day21;

import global.InputProcessor;

public class Solver {

    public static void main(String[] args) {
        // Set up any initial state

        // Process the input line-by-line
        InputProcessor processor = new InputProcessor("src/day21/input.txt");
        AccumulatorForStar1 resultOld = processor.processLines(ParsedLine::new, (parsedLine, accumulator) -> {
            // Update accumulator using parsed line object
            return accumulator.update(parsedLine);
        }, new AccumulatorForStar1());
        AccumulatorForStar2 result = processor.processLines(ParsedLine::new, (parsedLine, accumulator) -> {
            // Update accumulator using parsed line object
            return accumulator.update(parsedLine);
        }, new AccumulatorForStar2());
        for (int stepCount = 11; stepCount < 12; stepCount++) {
            System.out.println("Star 2 solution (old method): " + resultOld.star1(stepCount));
            System.out.println("Star 2 solution (new method): " + result.star2(stepCount));
        }
    }
}
