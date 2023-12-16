package day16;

import java.util.ArrayList;
import java.util.List;

public class Accumulator {
    // State
    List<String[]> gridRows = new ArrayList<>();

    // Update state from parsed line
    public Accumulator update(ParsedLine parsedLine) {
        gridRows.add(parsedLine.row);
        return this;
    }

    // Extract solution
    public String star1() {
        String[][] grid = gridRows.toArray(new String[gridRows.size()][gridRows.get(0).length]);

        // print for debugging
        for (int rowIndex = 0; rowIndex < grid.length; rowIndex++) {
            for (int columnIndex = 0; columnIndex < grid[rowIndex].length; columnIndex++) {
                System.out.print(grid[rowIndex][columnIndex]);
            }
            System.out.println();
        }

        // Create a square array of bytes
        // The last four bits of the byte represent beams passing through a grid square
        // [up][left][down][right]
        // e.g. a grid square that has beams passing up and left through it gets value
        // 00001100
        // Note this is based on the direction a beam ENTERS a square
        // i.e. a splitter | which a beam has entered from the right will get value
        // 00000001,
        // even though beams have exited it up and down.
        byte[][] gridEnergisation = new byte[grid.length][grid[0].length];

        int initialBeamRow = 0;
        int initialBeamColumn = 0;
        Direction initialBeamDirection = Direction.RIGHT;

        energiseGridWithBeam(grid, gridEnergisation, new GridCoordinate(initialBeamRow, initialBeamColumn),
                initialBeamDirection);

        // print for debugging: beam path
        System.out.println();
        for (int rowIndex = 0; rowIndex < gridEnergisation.length; rowIndex++) {
            for (int columnIndex = 0; columnIndex < gridEnergisation[rowIndex].length; columnIndex++) {
                byte energisation = gridEnergisation[rowIndex][columnIndex];
                if ("|-/\\".contains(grid[rowIndex][columnIndex])) {
                    System.out.print(grid[rowIndex][columnIndex]);
                    continue;
                }
                System.out.print(switch (energisation) {
                    case (byte) 0b00001000 -> "^"; // up
                    case (byte) 0b00000100 -> "<"; // left
                    case (byte) 0b00000010 -> "v"; // down
                    case (byte) 0b00000001 -> ">"; // right
                    case (byte) 0b00000000 -> "."; // empty
                    default -> "2"; // hardcode
                });
            }
            System.out.println();
        }

        // print for debugging: energised spaces
        System.out.println();
        int acc = 0;
        for (int rowIndex = 0; rowIndex < gridEnergisation.length; rowIndex++) {
            for (int columnIndex = 0; columnIndex < gridEnergisation[rowIndex].length; columnIndex++) {
                byte energisation = gridEnergisation[rowIndex][columnIndex];
                System.out.print(energisation > 0 ? "#" : ".");
                acc += energisation > 0 ? 1 : 0;
            }
            System.out.println();
        }

        return String.valueOf(acc);
    }

    private void energiseGridWithBeam(String[][] grid, byte[][] gridEnergisation, GridCoordinate beamLocation,
            Direction beamDirection) {
        if (beamLocation.row < 0 || beamLocation.column < 0 || beamLocation.row >= grid.length
                || beamLocation.column >= grid[beamLocation.row].length) {
            return; // beam absorbed by wall
        }

        // check if we've already visited here, if not update energisation
        if (beamDirection.gridEnergisationContainsDirection(gridEnergisation[beamLocation.row][beamLocation.column])) {
            return; // already visited
        } else {
            gridEnergisation[beamLocation.row][beamLocation.column] = beamDirection
                    .updateEnergisationWithDirection(gridEnergisation[beamLocation.row][beamLocation.column]);
        }

        // move on
        switch (grid[beamLocation.row][beamLocation.column]) {
            case ".": {
                GridCoordinate nextSpace = beamLocation.advance(beamDirection);
                energiseGridWithBeam(grid, gridEnergisation, nextSpace, beamDirection);
                return;
            }
            case "\\": {
                Direction newDirection = switch (beamDirection) {
                    case UP -> Direction.LEFT;
                    case LEFT -> Direction.UP;
                    case RIGHT -> Direction.DOWN;
                    case DOWN -> Direction.RIGHT;
                };
                GridCoordinate nextSpace = beamLocation.advance(newDirection);
                energiseGridWithBeam(grid, gridEnergisation, nextSpace, newDirection);
                return;
            }
            case "/": {
                Direction newDirection = switch (beamDirection) {
                    case UP -> Direction.RIGHT;
                    case RIGHT -> Direction.UP;
                    case LEFT -> Direction.DOWN;
                    case DOWN -> Direction.LEFT;
                };
                GridCoordinate nextSpace = beamLocation.advance(newDirection);
                energiseGridWithBeam(grid, gridEnergisation, nextSpace, newDirection);
                return;
            }
            case "-": {
                switch (beamDirection) {
                    case LEFT, RIGHT: {
                        // continue as if it were .
                        GridCoordinate nextSpace = beamLocation.advance(beamDirection);
                        energiseGridWithBeam(grid, gridEnergisation, nextSpace, beamDirection);
                        return;
                    }
                    case UP, DOWN: {
                        // split
                        GridCoordinate nextSpaceLeft = beamLocation.advance(Direction.LEFT);
                        energiseGridWithBeam(grid, gridEnergisation, nextSpaceLeft, Direction.LEFT);
                        GridCoordinate nextSpaceRight = beamLocation.advance(Direction.RIGHT);
                        energiseGridWithBeam(grid, gridEnergisation, nextSpaceRight, Direction.RIGHT);
                        return;
                    }
                }
            }
            case "|": {
                switch (beamDirection) {
                    case UP, DOWN: {
                        // continue as if it were .
                        GridCoordinate nextSpace = beamLocation.advance(beamDirection);
                        energiseGridWithBeam(grid, gridEnergisation, nextSpace, beamDirection);
                        return;
                    }
                    case LEFT, RIGHT: {
                        // split
                        GridCoordinate nextSpaceUp = beamLocation.advance(Direction.UP);
                        energiseGridWithBeam(grid, gridEnergisation, nextSpaceUp, Direction.UP);
                        GridCoordinate nextSpaceDown = beamLocation.advance(Direction.DOWN);
                        energiseGridWithBeam(grid, gridEnergisation, nextSpaceDown, Direction.DOWN);
                        return;
                    }
                }
            }
        }
    }

    private static enum Direction {
        UP, LEFT, DOWN, RIGHT;

        boolean gridEnergisationContainsDirection(byte energisation) {
            return 0 < (energisation & switch (this) {
                case UP -> 0b00001000;
                case LEFT -> 0b00000100;
                case DOWN -> 0b00000010;
                case RIGHT -> 0b00000001;
            });
        }

        byte updateEnergisationWithDirection(byte energisation) {
            return energisation |= switch (this) {
                case UP -> 0b00001000;
                case LEFT -> 0b00000100;
                case DOWN -> 0b00000010;
                case RIGHT -> 0b00000001;
            };
        }
    }

    private static record GridCoordinate(int row, int column) {
        GridCoordinate advance(Direction direction) {
            switch (direction) {
                case LEFT: {
                    return new GridCoordinate(row, column - 1);
                }
                case RIGHT: {
                    return new GridCoordinate(row, column + 1);
                }
                case UP: {
                    return new GridCoordinate(row - 1, column);
                }
                case DOWN: {
                    return new GridCoordinate(row + 1, column);
                }
                default: {
                    throw new IllegalArgumentException();
                }
            }
        }
    }
}
