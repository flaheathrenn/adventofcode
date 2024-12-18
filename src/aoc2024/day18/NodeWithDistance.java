package aoc2024.day18;

import global.GridUtils.GridCoordinate;

record NodeWithDistance(
    GridCoordinate node,
    Integer distance) implements Comparable<NodeWithDistance> {
    @Override
    public int compareTo(NodeWithDistance o) {
        return Integer.compare(this.distance == null ? Integer.MAX_VALUE : this.distance, 
            o.distance == null ? Integer.MAX_VALUE : o.distance);
    }
    
}
