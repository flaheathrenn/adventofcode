package day22;

import java.util.Set;
import java.util.TreeSet;

public class AccumulatorOldMethod {
    // State
    Set<Brick> bricks = new TreeSet<>();

    int maxX = 0;
    int maxY = 0;
    int maxZ = 0;

    // Update state from parsed line
    public AccumulatorOldMethod update(Brick brick) {
        bricks.add(brick);
        maxX = Integer.max(maxX, Integer.max(brick.startX, brick.endX));
        maxY = Integer.max(maxY, Integer.max(brick.startY, brick.endY));
        maxZ = Integer.max(maxZ, Integer.max(brick.startZ, brick.endZ));
        return this;
    }

    // Extract solution
    public String star1() {
        Brick[][][] brickArena = new Brick[maxX + 1][maxY + 1][maxZ + 1];

        for (Brick brick : bricks) {
            // drop into the 'brick arena'
            // bricks is sorted by minimum Z, so no later brick will ever be lower than this
            // one,
            // so we can examine the spaces under it
            dropBrick(brick, brickArena);
        }

        int unfreeBricks = 0;
        for (Brick brick : bricks) {
            for (Brick supportedBrick : brick.supportedBricks) {
                if (supportedBrick.supportingBricks.size() == 1) {
                    // not free
                    unfreeBricks++;
                    break;
                }
            }
        }
        return String.valueOf(bricks.size() - unfreeBricks);
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
    private void dropBrick(Brick brick, Brick[][][] brickArena) {
        // examine each layer starting from one lower than this brick's bottom
        for (int z = brick.lowestZ() - 1; z > 0; z--) {
            boolean brickSupported = false;
            // examine each cell in the XY plane below this brick
            for (int y = brick.startY; y < brick.endY + 1; y++) {
                for (int x = brick.startX; x < brick.endX + 1; x++) {
                    if (brickArena[x][y][z] != null) {
                        Brick supportingBrick = brickArena[x][y][z];
                        brick.supportingBricks.add(supportingBrick);
                        supportingBrick.supportedBricks.add(brick);
                        brickSupported = true;
                    }
                }
            }
            // mark the cells this brick occupies
            if (brickSupported) {
                for (int y = brick.startY; y < brick.endY + 1; y++) {
                    for (int x = brick.startX; x < brick.endX + 1; x++) {
                        for (int height = 0; height < brick.endZ - brick.startZ + 1; height++) {
                            brickArena[x][y][z + 1 + height] = brick;
                        }
                    }
                }
                return;
            }
        }
        // brick landed on floor
        for (int y = brick.startY; y < brick.endY + 1; y++) {
            for (int x = brick.startX; x < brick.endX + 1; x++) {
                brickArena[x][y][1] = brick;
                for (int height = 0; height < brick.endZ - brick.startZ + 1; height++) {
                    brickArena[x][y][1 + height] = brick;
                }
            }
        }
    }
}
