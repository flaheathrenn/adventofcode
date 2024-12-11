package aoc2024.day11;

import java.util.HashMap;
import java.util.Map;

public class StoneDirectory {
    Map<Long, Long> stones = new HashMap<>();

    public void addStone(long stoneId, long quantity) {
        long oldStones = getQuantity(stoneId);
        stones.put(stoneId, oldStones + quantity);
    }

    public long getQuantity(long stoneId) {
        return stones.containsKey(stoneId) ? stones.get(stoneId) : 0L;
    }

    public StoneDirectory blink() {
        StoneDirectory result = new StoneDirectory();
        for (Map.Entry<Long, Long> entry : stones.entrySet()) {
            long stoneId = entry.getKey();
            long quantity = entry.getValue();
            if (stoneId == 0) {
                result.addStone(1L, quantity);
            } else if (evenNumberOfDigits(stoneId)) {
                result.addStone(leftHalf(stoneId), quantity);
                result.addStone(rightHalf(stoneId), quantity);
            } else {
                result.addStone(stoneId * 2024L, quantity);
            }
        }
        return result;
    }

    public long size() {
        return stones.values().stream().reduce(Long::sum).orElse(0L);
    }

    private boolean evenNumberOfDigits(long startStone) {
        return ((int) Math.log10(startStone)) % 2 == 1;
    }

    private long leftHalf(long startStone) {
        int halfLength = (((int) Math.log10(startStone)) + 1) / 2;
        return startStone / Math.round(Math.pow(10, halfLength));
    }

    private long rightHalf(long startStone) {
        int halfLength = (((int) Math.log10(startStone)) + 1) / 2;
        return startStone % Math.round(Math.pow(10, halfLength));
    }
}
