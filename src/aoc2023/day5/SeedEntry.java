package aoc2023.day5;

public record SeedEntry(long start, long range) implements Comparable<SeedEntry> {
    public long end() {
        return start + range - 1;
    }

    public Boolean contains(long id) {
        return id >= start && id < start + range;
    }

    @Override
    public int compareTo(SeedEntry o) {
        // seed entries should never overlap so can just compare starts
        return Long.compare(this.start, o.start);
    }

    @Override
    public String toString() {
        return start() + "-" + end();
    }
    
}
