package aoc2023.day5;

import java.util.Optional;
import java.util.SortedSet;

public record AlmanacMap(SortedSet<AlmanacMapEntry> entries) {

    public long backConvert(long destinationId) {
        return entries.stream()
            .map(entry -> entry.backConvert(destinationId))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .findAny().orElse(destinationId);
    }
}
