package aoc2023.day24;

public class ParsedLine {

    // State
    Hailstone hailstone;

    // Parsing
    public ParsedLine(String line) {
        // 19, 13, 30 @ -2,  1, -2
        String[] components = line.split("[, @]+");
        long px = Long.parseLong(components[0]);
        long py = Long.parseLong(components[1]);
        long pz = Long.parseLong(components[2]);
        long vx = Long.parseLong(components[3]);
        long vy = Long.parseLong(components[4]);
        long vz = Long.parseLong(components[5]);
        this.hailstone = new Hailstone(px, py, pz, vx, vy, vz);
    }

}