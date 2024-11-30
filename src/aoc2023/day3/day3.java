package aoc2023.day3;

import aoc2023.day3.ParsedLineNumberLocations.NumberAndLocation;
import global.InputProcessor;

public class day3 {
    
    public static void main(String[] args) {
        // Build up coordinates of symbols
        InputProcessor atlasBuilder = new InputProcessor("src/aoc2023/day3/input.txt");
        GearAtlas atlas = atlasBuilder.processLines(ParsedLineSymbolLocations::new, (parsedLine, accumulator) -> {
            return accumulator.update(parsedLine);
        }, new GearAtlas());
        atlas.gears.forEach(gear -> {
            System.out.println("Recorded a gear at coordinates (" + gear.row + "," + gear.column + ")");
        });

        // Now go through the lines again looking for numbers
        InputProcessor processor = new InputProcessor("src/aoc2023/day3/input.txt");
        processor.processLines(ParsedLineNumberLocations::new, (parsedLine, accumulator) -> {
            int currentLineNumber = accumulator.currentLineNumber;
            for (NumberAndLocation numberAndLocation : parsedLine.numberLocations) {
                for (int row = currentLineNumber - 1; row <= currentLineNumber + 1; row++) {
                    for (int column = numberAndLocation.start() - 1; column <= numberAndLocation.end(); column++) {
                        for (Gear gear : atlas.gears) {
                            if (gear.row == row && gear.column == column) {
                                gear.addValue(numberAndLocation.value());
                            }
                        }
                    }
                }
            }
            return accumulator.update(0); // track line number
        }, new Accumulator());

        int acc = 0;
        for (Gear gear : atlas.gears) {
            acc += gear.gearRatio();
        }

        System.out.println(acc);
    }
}
