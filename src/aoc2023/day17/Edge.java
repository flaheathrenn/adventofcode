package aoc2023.day17;

record Edge(Node end, int length) {
    @Override
    public String toString() {
        return "-> " + end + ": " + length;
    }
}
