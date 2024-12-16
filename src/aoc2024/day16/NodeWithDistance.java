package aoc2024.day16;

record NodeWithDistance(
    Node node,
    Integer distance) implements Comparable<NodeWithDistance> {
    @Override
    public int compareTo(NodeWithDistance o) {
        return Integer.compare(this.distance == null ? Integer.MAX_VALUE : this.distance, 
            o.distance == null ? Integer.MAX_VALUE : o.distance);
    }
    
}
