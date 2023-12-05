package day5;

import java.util.Optional;

public record AlmanacMapEntry(long destinationRangeStart, long sourceRangeStart, long rangeLength) {
    public Optional<Long> convert(long source) {
        if (source < sourceRangeStart || source >= sourceRangeStart + rangeLength) {
            return Optional.empty();
        }
        return Optional.of(source - sourceRangeStart + destinationRangeStart);
    }
}
