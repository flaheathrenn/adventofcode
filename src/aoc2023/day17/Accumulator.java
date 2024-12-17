package aoc2023.day17;

import java.util.ArrayList;
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
    // State
    List<int[]> gridRows = new ArrayList<>();

    // Update state from parsed line
    public Accumulator update(ParsedLine parsedLine) {
        gridRows.add(parsedLine.values);
        return this;
    }

    // Extract solution
    public String star1() {
        int[][] grid = gridRows.toArray(new int[gridRows.size()][gridRows.get(0).length]);
        Map<Node, Set<Edge>> edges = new HashMap<>();

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                GridCoordinate here = new GridCoordinate(i, j);
                for (Direction d : Direction.values()) {
                    edges.put(new Node(here, d), new HashSet<>());
                }
                for (Direction d : Direction.values()) {
                    GridCoordinate stepped = here;
                    int distance = 0;
                    for (int steps = 1; steps <= 3; steps++) {
                        stepped = stepped.step(d);
                        if (!stepped.isWithin(grid)) {
                            break; // overstepped grid edge
                        }
                        distance += stepped.read(grid);
                        for (Direction d1 : Direction.values()) {
                            if (d1 == d) {
                                continue;
                            }
                            if (d1 == d.flip()) {
                                continue; // can't reverse direction
                            }
                            Node start = new Node(here, d1);
                            Node end = new Node(stepped, d);
                            edges.get(start).add(new Edge(end, distance));
                        }
                    }
                }
            }
        }

        // System.out.println(edges);

        Node startNode = new Node(new GridCoordinate(-1, -1), Direction.UP); // arbitrary start node
        edges.put(startNode, Set.of(
            new Edge(new Node(new GridCoordinate(0, 0), Direction.UP), 0),
            new Edge(new Node(new GridCoordinate(0, 0), Direction.DOWN), 0),
            new Edge(new Node(new GridCoordinate(0, 0), Direction.LEFT), 0),
            new Edge(new Node(new GridCoordinate(0, 0), Direction.RIGHT), 0)
        ));

        Node endNode = new Node(new GridCoordinate(Integer.MAX_VALUE, Integer.MAX_VALUE), Direction.UP); // arbitrary end node
        int maxI = grid.length - 1;
        int maxJ = grid[maxI].length - 1;
        GridCoordinate bottomRight = new GridCoordinate(maxI, maxJ);
        for (Direction d : Direction.values()) {
            edges.get(new Node(bottomRight, d)).add(new Edge(endNode, 0));
        }

        return Integer.toString(shortestPath(startNode, endNode, edges));
    }

    public String star2() {
        int[][] grid = gridRows.toArray(new int[gridRows.size()][gridRows.get(0).length]);
        Map<Node, Set<Edge>> edges = new HashMap<>();

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                GridCoordinate here = new GridCoordinate(i, j);
                for (Direction d : Direction.values()) {
                    edges.put(new Node(here, d), new HashSet<>());
                }
                for (Direction d : Direction.values()) {
                    GridCoordinate stepped = here;
                    int distance = 0;
                    for (int steps = 1; steps <= 10; steps++) {
                        stepped = stepped.step(d);
                        if (!stepped.isWithin(grid)) {
                            break; // overstepped grid edge
                        }
                        distance += stepped.read(grid);
                        if (steps < 4) {
                            continue;
                        }
                        for (Direction d1 : Direction.values()) {
                            if (d1 == d) {
                                continue;
                            }
                            if (d1 == d.flip()) {
                                continue; // can't reverse direction
                            }
                            Node start = new Node(here, d1);
                            Node end = new Node(stepped, d);
                            edges.get(start).add(new Edge(end, distance));
                        }
                    }
                }
            }
        }

        // System.out.println(edges);

        Node startNode = new Node(new GridCoordinate(-1, -1), Direction.UP); // arbitrary start node
        edges.put(startNode, Set.of(
            new Edge(new Node(new GridCoordinate(0, 0), Direction.UP), 0),
            new Edge(new Node(new GridCoordinate(0, 0), Direction.DOWN), 0),
            new Edge(new Node(new GridCoordinate(0, 0), Direction.LEFT), 0),
            new Edge(new Node(new GridCoordinate(0, 0), Direction.RIGHT), 0)
        ));

        Node endNode = new Node(new GridCoordinate(Integer.MAX_VALUE, Integer.MAX_VALUE), Direction.UP); // arbitrary end node
        int maxI = grid.length - 1;
        int maxJ = grid[maxI].length - 1;
        GridCoordinate bottomRight = new GridCoordinate(maxI, maxJ);
        for (Direction d : Direction.values()) {
            edges.get(new Node(bottomRight, d)).add(new Edge(endNode, 0));
        }

        return Integer.toString(shortestPath(startNode, endNode, edges));
    }

    int shortestPath(Node startNode, Node endNode, Map<Node, Set<Edge>> edgesFromNode) {
        Map<Node, Integer> visitedNodes = new HashMap<>();
        Queue<NodeWithDistance> unvisitedNodes = new PriorityQueue<>();
        unvisitedNodes.add(new NodeWithDistance(startNode, 0));
        while (!unvisitedNodes.isEmpty()) {
            NodeWithDistance currentNode = unvisitedNodes.poll();
            if (visitedNodes.containsKey(currentNode.node())) {
                // we've already visited this node with a shorter distance
                continue;
            }
            // System.out.println("Visiting " + currentNode);
            visitedNodes.put(currentNode.node(), currentNode.distance());
            if (currentNode.node().equals(endNode)) {
                // reached target node
                return currentNode.distance();
            }
            for (Edge edge : edgesFromNode.get(currentNode.node())) {
                unvisitedNodes.add(new NodeWithDistance(edge.end(), currentNode.distance() + edge.length()));
            }
            // System.out.println("Added " + edgesFromNode.get(currentNode.node()).size() + " destinations");
        }
        throw new IllegalStateException();
    }

}
