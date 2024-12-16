package aoc2024.day16;

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
    private static int TURN_COST = 1000;
    // State
    List<char[]> rows = new ArrayList<>();

    // Update state from parsed line
    public Accumulator update(ParsedLine parsedLine) {
        rows.add(parsedLine.row);
        return this;
    }

    // Extract solution
    public String star2() {

        char[][] grid = rows.toArray(new char[rows.size()][]);

        GridCoordinate start = null, end = null;
        Map<Node, Set<Edge>> edgesFromNode = new HashMap<>();

        // Add horizontal edges
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                char charAt = grid[i][j];
                if (charAt == '#') {
                    continue;
                }
                if (charAt == 'S') {
                    start = new GridCoordinate(i, j);
                }
                if (charAt == 'E') {
                    end = new GridCoordinate(i, j);
                }
                boolean upFree = grid[i - 1][j] != '#';
                boolean downFree = grid[i + 1][j] != '#';
                boolean leftFree = grid[i][j - 1] != '#';
                boolean rightFree = grid[i][j + 1] != '#';
                Node upNode = new Node(new GridCoordinate(i, j), Direction.UP);
                edgesFromNode.put(upNode, new HashSet<>());
                if (upFree) {
                    Node thisEnd = new Node(new GridCoordinate(i - 1, j), Direction.UP);
                    edgesFromNode.get(upNode).add(new Edge(thisEnd, 1));
                }
                Node downNode = new Node(new GridCoordinate(i, j), Direction.DOWN);
                edgesFromNode.put(downNode, new HashSet<>());
                if (downFree) {
                    Node thisEnd = new Node(new GridCoordinate(i + 1, j), Direction.DOWN);
                    edgesFromNode.get(downNode).add(new Edge(thisEnd, 1));
                }
                Node leftNode = new Node(new GridCoordinate(i, j), Direction.LEFT);
                edgesFromNode.put(leftNode, new HashSet<>());
                if (leftFree) {
                    Node thisEnd = new Node(new GridCoordinate(i, j - 1), Direction.LEFT);
                    edgesFromNode.get(leftNode).add(new Edge(thisEnd, 1));
                }
                Node rightNode = new Node(new GridCoordinate(i, j), Direction.RIGHT);
                edgesFromNode.put(rightNode, new HashSet<>());
                if (rightFree) {
                    Node thisEnd = new Node(new GridCoordinate(i, j + 1), Direction.RIGHT);
                    edgesFromNode.get(rightNode).add(new Edge(thisEnd, 1));
                }

                // add turning costs
                if (upFree && downFree && !leftFree && !rightFree) {
                    continue; // straight, no turning
                } else if (leftFree && rightFree && !upFree && !downFree) {
                    continue; // straight, no turning
                } else if (leftFree && upFree && !rightFree && !downFree) {
                    edgesFromNode.get(rightNode).add(new Edge(upNode, TURN_COST));
                    edgesFromNode.get(downNode).add(new Edge(leftNode, TURN_COST));
                } else if (leftFree && downFree && !rightFree && !upFree) {
                    edgesFromNode.get(rightNode).add(new Edge(downNode, TURN_COST));
                    edgesFromNode.get(upNode).add(new Edge(leftNode, TURN_COST));
                } else if (rightFree && downFree && !leftFree && !upFree) {
                    edgesFromNode.get(leftNode).add(new Edge(downNode, TURN_COST));
                    edgesFromNode.get(upNode).add(new Edge(rightNode, TURN_COST));
                } else if (rightFree && upFree && !leftFree && !downFree) {
                    edgesFromNode.get(downNode).add(new Edge(rightNode, TURN_COST));
                    edgesFromNode.get(leftNode).add(new Edge(upNode, TURN_COST));
                } else {
                    // more complex case, so just add all turns
                    edgesFromNode.get(upNode).add(new Edge(leftNode, TURN_COST));
                    edgesFromNode.get(leftNode).add(new Edge(upNode, TURN_COST));
                    edgesFromNode.get(upNode).add(new Edge(rightNode, TURN_COST));
                    edgesFromNode.get(rightNode).add(new Edge(upNode, TURN_COST));
                    edgesFromNode.get(downNode).add(new Edge(leftNode, TURN_COST));
                    edgesFromNode.get(leftNode).add(new Edge(downNode, TURN_COST));
                    edgesFromNode.get(downNode).add(new Edge(rightNode, TURN_COST));
                    edgesFromNode.get(rightNode).add(new Edge(downNode, TURN_COST));
                }
            }
        }

        Node startNode = new Node(start, Direction.RIGHT);
        Node endNodeRight = new Node(end, Direction.RIGHT);
        int star1solution = shortestPath(startNode, endNodeRight, edgesFromNode);
        System.out.println("Star 1 solution: " + star1solution);
        int star2solution = tilesCoveredByShortestPaths(startNode, endNodeRight, edgesFromNode, star1solution);
        return Integer.toString(star2solution);

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
            visitedNodes.put(currentNode.node(), currentNode.distance());
            if (currentNode.node().coordinate().equals(endNode.coordinate())) {
                // reached target node
                return currentNode.distance();
            }
            for (Edge edge : edgesFromNode.get(currentNode.node())) {
                unvisitedNodes.add(new NodeWithDistance(edge.end(), currentNode.distance() + edge.length()));
            }
        }
        throw new IllegalStateException();
    }

    int tilesCoveredByShortestPaths(Node startNode, Node endNode, Map<Node, Set<Edge>> edgesFromNode,
            int shortestPathLength) {
        Map<Node, Integer> visitedNodes = new HashMap<>();
        Map<Node, Set<List<Node>>> shortestPaths = new HashMap<>();
        Queue<NodeWithDistanceWithMemory> unvisitedNodes = new PriorityQueue<>();
        unvisitedNodes.add(new NodeWithDistanceWithMemory(startNode, 0, List.of(startNode)));
        while (!unvisitedNodes.isEmpty()) {
            NodeWithDistanceWithMemory currentNode = unvisitedNodes.poll();
            // System.out.println(currentNode.distance());
            if (currentNode.distance() > shortestPathLength) {
                break;
            }
            if (visitedNodes.containsKey(currentNode.node())) {
                if (visitedNodes.get(currentNode.node()) < currentNode.distance()) {
                    // we've already visited this node with a shorter distance
                    continue;
                }
            }
            visitedNodes.put(currentNode.node(), currentNode.distance());
            if (!shortestPaths.containsKey(currentNode.node())) {
                shortestPaths.put(currentNode.node(), new HashSet<>());
            }
            shortestPaths.get(currentNode.node()).add(currentNode.path());
            for (Edge edge : edgesFromNode.get(currentNode.node())) {
                List<Node> newPath = new ArrayList<>(currentNode.path());
                newPath.add(edge.end());
                unvisitedNodes.add(
                        new NodeWithDistanceWithMemory(edge.end(), currentNode.distance() + edge.length(), newPath));
            }
        }

        Set<GridCoordinate> tilesCovered = new HashSet<>();
        for (Direction d : Direction.values()) {
            Node thisEnd = new Node(endNode.coordinate(), d);
            if (shortestPaths.containsKey(thisEnd)) {
                // System.out.println("There are " + shortestPaths.get(thisEnd).size() + " shortest paths");
                for (List<Node> path : shortestPaths.get(thisEnd)) {
                    path.stream().map(n -> n.coordinate()).forEach(tilesCovered::add);
                }
            }
        }

        return tilesCovered.size();
    }
}
