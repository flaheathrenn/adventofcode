package day3;

import java.util.HashSet;
import java.util.Set;

public class SymbolAtlas {
    // State
    Set<SymbolLocation> atlas = new HashSet<>();
    int currentLineNumber = 1;

    // Update state from parsed line
    public SymbolAtlas update(ParsedLineSymbolLocations parsedLine) {
        parsedLine.symbolLocations.stream().map(locationInLine -> {
            return new SymbolLocation(currentLineNumber, locationInLine);
        }).forEach(atlas::add);
        currentLineNumber++;
        return this;
    }
}
