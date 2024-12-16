package aoc2024.day16;

import java.util.List;

record NodeWithDistanceWithMemory(
    Node node,
    Integer distance,
    List<Node> path) implements Comparable<NodeWithDistanceWithMemory> {
    @Override
    public int compareTo(NodeWithDistanceWithMemory o) {
        return Integer.compare(this.distance == null ? Integer.MAX_VALUE : this.distance, 
            o.distance == null ? Integer.MAX_VALUE : o.distance);
    }
    
}
