package aoc2025.day8;

public class ParsedLine {

    // State
    JunctionBox junctionBox;

    // Parsing
    public ParsedLine(String line) {
        junctionBox = new JunctionBox(line);
    }

    public record JunctionBox(long x, long y, long z) {
        public JunctionBox(String stringToParse) {
            String[] splitString = stringToParse.split(",");
            this(Long.parseLong(splitString[0]), Long.parseLong(splitString[1]), Long.parseLong(splitString[2]));
        }

        // just using this for ordering so we don't need to do the square root
        public long distanceSquared(JunctionBox other) {
            return (this.x-other.x) * (this.x-other.x) + (this.y-other.y) * (this.y - other.y) + (this.z - other.z) * (this.z - other.z);
        }

        @Override
        public String toString() {
            return String.format("(%d,%d,%d)", x, y, z);
        }
    }

}