package aoc2023.day20;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import aoc2023.day20.Module.ConjunctionModule;
import aoc2023.day20.Module.Pulse;
import aoc2023.day20.Module.PulseQueueItem;

public class Accumulator {
    // State
    static Map<String, Module> moduleAlmanac = new HashMap<>();

    // Update state from parsed line
    public Accumulator update(Module module) {
        moduleAlmanac.put(module.name, module);
        return this;
    }

    // Extract solution
    public String star1() {
        // init conjunction modules
        initConjunctionModules();

        // actual calcuation
        PushButtonResult acc = new PushButtonResult(0, 0);
        for (int i = 0; i < 1000; i++) {
            acc = acc.add(pushButton());
        }
        return String.valueOf(acc.lowPulses * acc.highPulses);
    }

    // Extract solution
    public String star2() {
        // init conjunction modules
        initConjunctionModules();

        // actual calculation
        long acc = 1322000000L;
        do {
            if (acc % 1000000 == 0) {
                System.out.println(acc);
            }
            acc++;
        } while (!pushButton2());
        return String.valueOf(acc);
    }

    private static void initConjunctionModules() {
        List<String> conjunctionModuleNames = moduleAlmanac.values().stream()
                .filter(m -> m instanceof ConjunctionModule).map(Module::getName).toList();

        for (Module m : moduleAlmanac.values()) {
            for (String destination : m.destinations) {
                if (conjunctionModuleNames.contains(destination)) {
                    ConjunctionModule cm = (ConjunctionModule) moduleAlmanac.get(destination);
                    cm.updateInputs(m.name);
                }
            }
        }
    }

    public static PushButtonResult pushButton() {
        long lowPulses = 0;
        long highPulses = 0;
        ArrayDeque<PulseQueueItem> currentPulses = new ArrayDeque<PulseQueueItem>();
        currentPulses.add(new PulseQueueItem(Pulse.LOW, "broadcaster", null));
        lowPulses++;
        while (!currentPulses.isEmpty()) {
            PulseQueueItem topItem = currentPulses.poll();
            if (topItem.receivingModule().equals("rx") && topItem.pulse() == Pulse.LOW) {
                throw new RxReceivedLowPulseException();
            }
            if (!moduleAlmanac.containsKey(topItem.receivingModule())) {
                continue;
            }
            Module handlingModule = moduleAlmanac.get(topItem.receivingModule());
            List<PulseQueueItem> newPulses = handlingModule.handlePulse(topItem.pulse(), topItem.originatingModule());
            // System.out.println("Module " + handlingModule + " handling " + topItem.pulse() + " pulse, creates new pulses " + newPulses);
            currentPulses.addAll(newPulses);
            long newLowPulses = newPulses.stream().filter(pqi -> pqi.pulse() == Pulse.LOW).count();
            lowPulses += newLowPulses;
            highPulses += newPulses.size() - newLowPulses;
        }
        return new PushButtonResult(lowPulses, highPulses);
    }

    public static boolean pushButton2() {
        ArrayDeque<PulseQueueItem> currentPulses = new ArrayDeque<PulseQueueItem>();
        currentPulses.add(new PulseQueueItem(Pulse.LOW, "broadcaster", null));
        while (!currentPulses.isEmpty()) {
            PulseQueueItem topItem = currentPulses.poll();
            if (topItem.receivingModule().equals("rx") && topItem.pulse() == Pulse.LOW) {
                return true;
            }
            if (!moduleAlmanac.containsKey(topItem.receivingModule())) {
                continue;
            }
            Module handlingModule = moduleAlmanac.get(topItem.receivingModule());
            List<PulseQueueItem> newPulses = handlingModule.handlePulse(topItem.pulse(), topItem.originatingModule());
            currentPulses.addAll(newPulses);
        }
        return false;
    }

    public static record PushButtonResult(long lowPulses, long highPulses) {
        public PushButtonResult add(PushButtonResult other) {
            return new PushButtonResult(this.lowPulses + other.lowPulses, this.highPulses + other.highPulses);
        }
    }
    
    public static class RxReceivedLowPulseException extends IllegalStateException {}
}
