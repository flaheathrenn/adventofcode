package aoc2023.day20;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class Module {

    String name;
    List<String> destinations;

    public abstract List<PulseQueueItem> handlePulse(Pulse pulse, String originatingModule);

    public String getName() {
        return name;
    }

    public List<String> getDestinations() {
        return destinations;
    }

    public static Module makeModule(String line) {
        String[] splitAroundArrow = line.split(" -> ");
        List<String> destinations = Arrays.asList(splitAroundArrow[1].split(", "));
        if (splitAroundArrow[0].startsWith("%")) {
            return new FlipFlopModule(splitAroundArrow[0].substring(1), destinations);
        }
        if (splitAroundArrow[0].startsWith("&")) {
            return new ConjunctionModule(splitAroundArrow[0].substring(1), destinations);
        }
        if (splitAroundArrow[0].equals("broadcaster")) {
            return new BroadcasterModule(destinations);
        }
        throw new IllegalArgumentException("Cannot make module from line " + line);
    }

    public static class BroadcasterModule extends Module {
        public BroadcasterModule(List<String> destinations) {
            this.name = "broadcaster";
            this.destinations = destinations;
        }

        @Override
        public List<PulseQueueItem> handlePulse(Pulse pulse, String originatingModule) {
            return destinations.stream().map(destination -> new PulseQueueItem(pulse, destination, this.name)).toList();
        }
    }

    public static class FlipFlopModule extends Module {
        boolean state;

        public FlipFlopModule(String name, List<String> destinations) {
            this.name = name;
            this.destinations = destinations;
            this.state = false; // starts 'off'
        }

        @Override
        public List<PulseQueueItem> handlePulse(Pulse pulse, String originatingModule) {
            if (pulse == Pulse.HIGH) {
                // ignored, nothing happens
                return Collections.emptyList();
            }
            // if a flip-flop module receives a low pulse, it flips between on and off.
            // If it was off, it turns on and sends a high pulse.
            if (!state) {
                state = true;
                return destinations.stream().map(destination -> new PulseQueueItem(Pulse.HIGH, destination, this.name)).toList();
            }
            // If it was on, it turns off and sends a low pulse.
            state = false;
            return destinations.stream().map(destination -> new PulseQueueItem(Pulse.LOW, destination, this.name)).toList();
        }
    }

    public static class ConjunctionModule extends Module {
        List<InputMemory> inputMemories;

        public ConjunctionModule(String name, List<String> destinations) {
            this.name = name;
            this.destinations = destinations;
            this.inputMemories = new ArrayList<>();
        }

        public void updateInputs(String input) {
            inputMemories.add(new InputMemory(input));
        }

        @Override
        public List<PulseQueueItem> handlePulse(Pulse pulse, String originatingModule) {
            // When a pulse is received, the conjunction module first updates its memory for
            // that input.

            inputMemories.stream().filter(memory -> memory.name.equals(originatingModule))
                    .forEach(relevantMemory -> relevantMemory.memory = pulse);

            // Then, if it remembers high pulses for all inputs, it sends a low pulse;
            // otherwise, it sends a high pulse.

            if (inputMemories.stream().map(InputMemory::getMemory).allMatch(memory -> memory == Pulse.HIGH)) {
                return destinations.stream().map(destination -> new PulseQueueItem(Pulse.LOW, destination, this.name)).toList();
            } else {
                return destinations.stream().map(destination -> new PulseQueueItem(Pulse.HIGH, destination, this.name)).toList();
            }
        }

        private static class InputMemory {
            private final String name;
            private Pulse memory;

            InputMemory(String name) {
                this.name = name;
                this.memory = Pulse.LOW;
            }

            Pulse getMemory() {
                return memory;
            }

            public String toString() {
                return "{" + name + "=" + memory + "}";
            }
        }
    }

    public static record PulseQueueItem(Pulse pulse, String receivingModule, String originatingModule) {
    }

    public static enum Pulse {
        HIGH, LOW
    }

}