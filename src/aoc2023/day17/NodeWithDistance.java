package aoc2023.day17;

record NodeWithDistance(
    Node node,
    Integer distance) implements Comparable<NodeWithDistance> {
    @Override
    public int compareTo(NodeWithDistance o) {
        return Integer.compare(this.distance == null ? Integer.MAX_VALUE : this.distance, 
            o.distance == null ? Integer.MAX_VALUE : o.distance);
    }
    
}
