package aoc2023.day3;

import java.util.HashSet;
import java.util.Set;

public class GearAtlas {
    // State
    Set<Gear> gears = new HashSet<>();
    int currentLineNumber = 1;

    // Update state from parsed line
    public GearAtlas update(ParsedLineSymbolLocations parsedLine) {
        parsedLine.symbolLocations.stream().map(locationInLine -> {
            Gear gear = new Gear();
            gear.setPosition(currentLineNumber, locationInLine);
            return gear;
        }).forEach(gears::add);
        currentLineNumber++;
        return this;
    }
}
