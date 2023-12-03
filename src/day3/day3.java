package day3;

import day3.ParsedLineNumberLocations.NumberAndLocation;
import global.InputProcessor;

public class day3 {
    
    public static void main(String[] args) {
        // Build up coordinates of symbols
        InputProcessor atlasBuilder = new InputProcessor("src/day3/input.txt");
        SymbolAtlas atlas = atlasBuilder.processLines(ParsedLineSymbolLocations::new, (parsedLine, accumulator) -> {
            return accumulator.update(parsedLine);
        }, new SymbolAtlas());
        atlas.atlas.forEach(location -> {
            System.out.println("Recorded a symbol at coordinates (" + location.row() + "," + location.column() + ")");
        });

        // Now go through the lines again looking for numbers
        InputProcessor processor = new InputProcessor("src/day3/input.txt");
        Accumulator result = processor.processLines(ParsedLineNumberLocations::new, (parsedLine, accumulator) -> {
            int currentLineNumber = accumulator.currentLineNumber;
            int lineTotal = 0;
            for (NumberAndLocation numberAndLocation : parsedLine.numberLocations) {
                processNumber: for (int row = currentLineNumber - 1; row <= currentLineNumber + 1; row++) {
                    for (int column = numberAndLocation.start() - 1; column <= numberAndLocation.end(); column++) {
                        if (atlas.atlas.contains(new SymbolLocation(row, column))) {
                            lineTotal += numberAndLocation.value();
                            break processNumber;
                        }
                    }
                }
            }
            return accumulator.update(lineTotal);
        }, new Accumulator());
        System.out.println("Star 1 solution: " + result.star1());
    }
}
