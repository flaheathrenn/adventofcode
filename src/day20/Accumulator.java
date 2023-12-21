package day20;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import day20.Module.ConjunctionModule;
import day20.Module.Pulse;
import day20.Module.PulseQueueItem;

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

        // print for debug
        // for (Module m : moduleAlmanac.values()) {
        //     if (m instanceof BroadcasterModule) {
        //         System.out.print("b");
        //     }
        //     if (m instanceof FlipFlopModule) {
        //         System.out.print("%");
        //     }
        //     if (m instanceof ConjunctionModule) {
        //         System.out.print("&");
        //     }
        //     System.out.print(m.name + " -> " + m.destinations);
        //     if (m instanceof ConjunctionModule) {
        //         ConjunctionModule cm = (ConjunctionModule) m;
        //         System.out.print(" (" + cm.inputMemories + ")");
        //     }
        //     System.out.println();
        // }

        // actual calcuation
        PushButtonResult acc = new PushButtonResult(0, 0);
        for (int i = 0; i < 1000; i++) {
            acc = acc.add(pushButton());
        }
        return String.valueOf(acc.lowPulses * acc.highPulses);
    }

    public static PushButtonResult pushButton() {
        long lowPulses = 0;
        long highPulses = 0;
        ArrayDeque<PulseQueueItem> currentPulses = new ArrayDeque<PulseQueueItem>();
        currentPulses.add(new PulseQueueItem(Pulse.LOW, "broadcaster", null));
        lowPulses++;
        while (!currentPulses.isEmpty()) {
            PulseQueueItem topItem = currentPulses.poll();
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

    public static record PushButtonResult(long lowPulses, long highPulses) {
        public PushButtonResult add(PushButtonResult other) {
            return new PushButtonResult(this.lowPulses + other.lowPulses, this.highPulses + other.highPulses);
        }
    }
}
