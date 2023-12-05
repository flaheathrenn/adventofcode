package day5;

import java.util.Optional;

public record AlmanacMapEntry(long destinationRangeStart, long sourceRangeStart, long rangeLength) implements Comparable<AlmanacMapEntry> {
    public Optional<Long> convert(long source) {
        if (source < sourceRangeStart || source >= sourceRangeStart + rangeLength) {
            return Optional.empty();
        }
        return Optional.of(source - sourceRangeStart + destinationRangeStart);
    }

    public Optional<Long> backConvert(long destination) {
        if (destination < destinationRangeStart || destination >= destinationRangeStart + rangeLength) {
            return Optional.empty();
        }
        return Optional.of(destination - destinationRangeStart + sourceRangeStart);
    }

    @Override
    public int compareTo(AlmanacMapEntry o) {
        return Long.compare(this.sourceRangeStart, o.sourceRangeStart);
    }
}
