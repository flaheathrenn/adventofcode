package day19;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import day19.ParsedLine.Condition;
import day19.ParsedLine.Part;

public class Accumulator {
    // State
    Map<String, List<Condition>> workflows = new HashMap<>();
    List<Part> parts = new ArrayList<>();

    // Update state from parsed line
    public Accumulator update(ParsedLine parsedLine) {
        if (parsedLine.isPart) {
            parts.add(parsedLine.part);
        } else if (parsedLine.isWorkflow) {
            workflows.put(parsedLine.workflow.name(), parsedLine.workflow.conditions());
        }
        return this;
    }

    // Extract solution
    public String star1() {
        List<Part> acceptedParts = new ArrayList<>();
        for (Part p : parts) {
            String startWorkflowName = "in";
            processPart: while (true) {
                List<Condition> workflowConditions = workflows.get(startWorkflowName);
                for (Condition condition : workflowConditions) {
                    String result = condition.test(p);
                    if (result == null) {
                        continue; // test failed
                    }
                    if ("A".equals(result)) {
                        acceptedParts.add(p);
                        break processPart;
                    }
                    if ("R".equals(result)) {
                        break processPart;
                    }
                    startWorkflowName = result;
                    break;
                }
            }
        }
        return String.valueOf(
                acceptedParts.stream().map(part -> part.x() + part.m() + part.a() + part.s()).reduce(0, Integer::sum));
    }
}
