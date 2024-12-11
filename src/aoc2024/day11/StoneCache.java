package aoc2024.day11;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class StoneCache {
    private Map<Long, StoneCacheEntry> cache = new HashMap<>();

    public Optional<CacheHit> lookUp(long startStone, int blinks) {
        if (!cache.containsKey(startStone)) {
            return Optional.empty();
        }
        return cache.get(startStone).lookUp(blinks);
    }

    public void put(long startStone, int blinks, List<Long> stonesAfterBlinks) {
        if (!cache.containsKey(startStone)) {
            cache.put(startStone, new StoneCacheEntry());
        }
        StoneCacheEntry cacheEntry = cache.get(startStone);
        cacheEntry.stonesAfterBlinks.put(blinks, stonesAfterBlinks);
    }

    public static record CacheHit(int blinks, List<Long> stonesAfterBlinks) {
    };

    private static class StoneCacheEntry {
        private Map<Integer, List<Long>> stonesAfterBlinks = new HashMap<>();

        public Optional<CacheHit> lookUp(int blinks) {
            return stonesAfterBlinks.keySet().stream().filter(x -> x <= blinks).max(Integer::compare).map(key -> new CacheHit(key, stonesAfterBlinks.get(key)));
        }

        public String toString() {
            return stonesAfterBlinks.keySet().stream().map(String::valueOf).collect(Collectors.joining(","));
        }
    }

    public void printStats() {
        System.out.println("Stone cache stats:");
        System.out.println("Entries: " + cache.keySet().size());
        for (Map.Entry<Long, StoneCacheEntry> entry : cache.entrySet()) {
            System.out.println(entry);
        }
    }
}
