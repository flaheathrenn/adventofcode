package day22;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class Accumulator {
    // State
    Set<Brick> bricks = new TreeSet<>();

    int maxX = 0;
    int maxY = 0;
    int maxZ = 0;

    // Update state from parsed line
    public Accumulator update(Brick brick) {
        bricks.add(brick);
        maxX = Integer.max(maxX, Integer.max(brick.startX, brick.endX));
        maxY = Integer.max(maxY, Integer.max(brick.startY, brick.endY));
        maxZ = Integer.max(maxZ, Integer.max(brick.startZ, brick.endZ));
        return this;
    }

    // Extract solution
    public String star2() {
        BrickArenaViewedFromAbovePosition[][] brickArena = new BrickArenaViewedFromAbovePosition[maxX + 1][maxY + 1];
        for (int x = 0; x < brickArena.length; x++) {
            for (int y = 0; y < brickArena[0].length; y++) {
                brickArena[x][y] = new BrickArenaViewedFromAbovePosition(0, null);
            }
        }

        for (Brick brick : bricks) {
            // drop into the 'brick arena'
            // bricks is sorted by minimum Z, so no later brick will ever be lower than this
            // one,
            // so we can examine the spaces under it
            dropBrick(brick, brickArena);
        }

        int acc = 0;
        for (Brick brick : bricks) {
            acc += numberOfBricksThatWouldFallIfDisintegrated(brick);
        }
        return String.valueOf(acc);
    }

    private int numberOfBricksThatWouldFallIfDisintegrated(Brick brick) {
        Set<Brick> allSupportedBricks = allSupportedBricks(brick);
        int acc = 0;
        Iterator<Brick> setIterator = allSupportedBricks.iterator();
        while (setIterator.hasNext()) {
            Brick supportedBrick = setIterator.next();
            if (supportedBrick.equals(brick)) {
                continue; // don't count the brick itself
            }
            if (allSupportedBricks.containsAll(supportedBrick.supportingBricks)) {
                acc++;
            } else {
                setIterator.remove();
            }
        }
        return acc;
    }

    private Set<Brick> allSupportedBricks(Brick brick) {
        Set<Brick> result = new TreeSet<>(brick.supportedBricks);
        result.add(brick);
        for (Brick supportedBrick : brick.supportedBricks) {
            result.addAll(allSupportedBricks(supportedBrick));
        }
        return result;
    }

    public static record BrickArenaViewedFromAbovePosition(int zIndex, Brick brick) {
    }

    /**
     * Mutates brickArena by creating brick brick and then letting it fall.
     * <p>
     * <p>
     * First, brick is created at its listed coordinates, and then it falls until it
     * is supported by either
     * bricks or the floor.
     * The coordinates in brickArena in which it comes to rest are all update with a
     * reference to brick.
     * Brick is then updated to add any supporting bricks to its supportingBricks
     * property,
     * and those supporting bricks are updated to add brick to their supportedBricks
     * property.
     */
    private void dropBrick(Brick brick, BrickArenaViewedFromAbovePosition[][] brickArena) {
        // examine each layer starting from one lower than this brick's bottom
        Set<BrickArenaViewedFromAbovePosition> bricksUnderneath = new HashSet<>();
        for (int y = brick.startY; y < brick.endY + 1; y++) {
            for (int x = brick.startX; x < brick.endX + 1; x++) {
                if (brickArena[x][y].brick != null) {
                    bricksUnderneath.add(brickArena[x][y]);
                }
            }
        }
        // work out what's actually supporting
        int supportedHeight = bricksUnderneath.stream().map(BrickArenaViewedFromAbovePosition::zIndex)
                .max(Integer::compareTo).orElse(0);
        Set<Brick> supportingBricks = bricksUnderneath.stream()
                .filter(brickUnderneath -> brickUnderneath.zIndex == supportedHeight)
                .map(BrickArenaViewedFromAbovePosition::brick).distinct().collect(Collectors.toSet());
        brick.supportingBricks.addAll(supportingBricks);
        for (Brick supporter : supportingBricks) {
            supporter.supportedBricks.add(brick);
        }
        // record brick position
        int brickHeight = brick.endZ - brick.startZ + 1;
        for (int y = brick.startY; y < brick.endY + 1; y++) {
            for (int x = brick.startX; x < brick.endX + 1; x++) {
                brickArena[x][y] = new BrickArenaViewedFromAbovePosition(supportedHeight + brickHeight, brick);
            }
        }
    }
}
