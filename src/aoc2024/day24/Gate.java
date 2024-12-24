package aoc2024.day24;

import java.util.Map;

public record Gate(String inputWire, String inputWire2, GateType type, String outputWire) {
    public static enum GateType{
        AND, OR, XOR;
    }

    /**
     * return true if map update, false otherwise
     * @return
     */
    public boolean updateMapForGate(Map<String, Boolean> map) {
        if (!map.containsKey(inputWire) || !map.containsKey(inputWire2)) {
            return false;
        }
        return switch (type) {
            case AND: {
                map.put(outputWire, Boolean.logicalAnd(map.get(inputWire), map.get(inputWire2)));
                yield true;
            }
            case OR: {
                map.put(outputWire, Boolean.logicalOr(map.get(inputWire), map.get(inputWire2)));
                yield true;
            }
            case XOR: {
                map.put(outputWire, Boolean.logicalXor(map.get(inputWire), map.get(inputWire2)));
                yield true;
            }
        };
    }
}
