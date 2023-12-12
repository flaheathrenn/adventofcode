package day12;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Memo {

    private static final Map<String, Long> memo = new HashMap<>();

    public static Optional<Long> lookup(String pattern, List<Integer> groupDefinition) {
        String reducedPattern = pattern.replaceAll("\\.+", ".");
        String key = reducedPattern + ";" + groupDefinition.hashCode();
        if (memo.containsKey(key)) {
            return Optional.of(memo.get(key));
        }
        return Optional.empty();
    }

    public static void store(String pattern, List<Integer> groupDefinition, long combinations) {
        String reducedPattern = pattern.replaceAll("\\.+", ".");
        String key = reducedPattern + ";" + groupDefinition.hashCode();
        memo.put(key, combinations);
    }
    
}
