package aoc2024.day18;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import global.GridUtils.Direction;
import global.GridUtils.GridCoordinate;

public class Accumulator {
    private static final int MEMORY_SPACE_SIZE = 71; // for real input
    // State
    List<GridCoordinate> bytesList = new ArrayList<>();

    // Update state from parsed line
    public Accumulator update(ParsedLine parsedLine) {
        bytesList.add(parsedLine.corrupted);
        return this;
    }

    // Extract solution
    public String star1() {
        // init map
        Set<GridCoordinate> corrupted = new HashSet<>(bytesList.subList(0, 1024));
        Map<GridCoordinate, Set<Edge>> edgesFromNode = new HashMap<>();
        for (int i = 0; i < MEMORY_SPACE_SIZE; i++) {
            for (int j = 0; j < MEMORY_SPACE_SIZE; j++) {
                GridCoordinate here = new GridCoordinate(i, j);
                edgesFromNode.put(here, new HashSet<>());
                if (corrupted.contains(here)) {
                    continue; // no paths out of corrupted node
                }
                for (Direction d : Direction.values()) {
                    GridCoordinate adj = here.step(d);
                    if (adj.isWithin(MEMORY_SPACE_SIZE)) {
                        edgesFromNode.get(here).add(new Edge(adj, 1));
                    }
                }
            }
        }
        GridCoordinate startNode = new GridCoordinate(0, 0);
        GridCoordinate endNode = new GridCoordinate(MEMORY_SPACE_SIZE - 1, MEMORY_SPACE_SIZE - 1);
        return Integer.toString(shortestPath(startNode, endNode, edgesFromNode));
    }

    public String star2() {
        // init map
        Map<GridCoordinate, Set<Edge>> edgesFromNode = new HashMap<>();
        for (int i = 0; i < MEMORY_SPACE_SIZE; i++) {
            for (int j = 0; j < MEMORY_SPACE_SIZE; j++) {
                GridCoordinate here = new GridCoordinate(i, j);
                edgesFromNode.put(here, new HashSet<>());
                for (Direction d : Direction.values()) {
                    GridCoordinate adj = here.step(d);
                    if (adj.isWithin(MEMORY_SPACE_SIZE)) {
                        edgesFromNode.get(here).add(new Edge(adj, 1));
                    }
                }
            }
        }
        GridCoordinate startNode = new GridCoordinate(0, 0);
        GridCoordinate endNode = new GridCoordinate(MEMORY_SPACE_SIZE - 1, MEMORY_SPACE_SIZE - 1);

        for (GridCoordinate fallingByte : bytesList) {
            try {
                edgesFromNode.put(fallingByte, Collections.emptySet()); // remove paths out of corrupted space
                shortestPath(startNode, endNode, edgesFromNode);
            } catch (IllegalStateException e) {
                return fallingByte.toString();
            }
        }

        return "Exhaused byte list with no blocking";
    }

    int shortestPath(GridCoordinate startNode, GridCoordinate endNode, Map<GridCoordinate, Set<Edge>> edgesFromNode) {
        Map<GridCoordinate, Integer> visitedNodes = new HashMap<>();
        Queue<NodeWithDistance> unvisitedNodes = new PriorityQueue<>();
        unvisitedNodes.add(new NodeWithDistance(startNode, 0));
        while (!unvisitedNodes.isEmpty()) {
            NodeWithDistance currentNode = unvisitedNodes.poll();
            if (visitedNodes.containsKey(currentNode.node())) {
                // we've already visited this node with a shorter distance
                continue;
            }
            visitedNodes.put(currentNode.node(), currentNode.distance());
            if (currentNode.node().equals(endNode)) {
                // reached target node
                return currentNode.distance();
            }
            for (Edge edge : edgesFromNode.get(currentNode.node())) {
                unvisitedNodes.add(new NodeWithDistance(edge.end(), currentNode.distance() + edge.length()));
            }
        }
        throw new IllegalStateException();
    }
}
