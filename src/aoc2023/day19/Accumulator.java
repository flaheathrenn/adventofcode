package aoc2023.day19;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import aoc2023.day19.ParsedLine.Comparer;
import aoc2023.day19.ParsedLine.ComparingCondition;
import aoc2023.day19.ParsedLine.Condition;
import aoc2023.day19.ParsedLine.DefaultCaseCondition;
import aoc2023.day19.ParsedLine.Parameter;
import aoc2023.day19.ParsedLine.Part;

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

    public String star2() {
        return String.valueOf(this.countPossibilities("in", Collections.emptyList()));
    }

    public long countPossibilities(String workflowName, List<ConditionPath> conditionPathArgument) {
        // Base case
        if ("R".equals(workflowName)) {
            return 0L;
        }
        if ("A".equals(workflowName)) {
            return possibilitiesForConditionPath(conditionPathArgument);
        }
        // Recursive case
        long acc = 0;
        List<Condition> conditions = workflows.get(workflowName);
        List<ConditionPath> conditionPath = new ArrayList<>(conditionPathArgument);
        for (Condition condition : conditions) {
            // if default case, just recurse down
            if (condition instanceof DefaultCaseCondition) {
                acc += countPossibilities(condition.result(), conditionPath);
                continue;
            }
            // if condition is true, recurse down condition
            List<ConditionPath> truePath = new ArrayList<>(conditionPath);
            truePath.add(new ConditionPath(condition, true));
            acc += countPossibilities(condition.result(), truePath);
            // if condition is false, mark as false and continue
            conditionPath.add(new ConditionPath(condition, false));
        }
        return acc;
    }
        
    private long possibilitiesForConditionPath(List<ConditionPath> conditionPathArgument) {
        Map<Parameter, List<ConditionPath>> conditions = conditionPathArgument.stream()
            .collect(Collectors.groupingBy(conditionPath -> ((ComparingCondition) conditionPath.condition()).parameter()));
        long acc = 1;
        for (Parameter parameter : Parameter.values()) {
            if (!conditions.containsKey(parameter)) {
                acc *= 4000L; // no condition on this parameter so just use full range
                continue;
            }
            int min = 1;
            int max = 4000;
            for (ConditionPath conditionPath : conditions.get(parameter)) {
                ComparingCondition condition = (ComparingCondition) conditionPath.condition();
                if (condition.comparer() == Comparer.LESS_THAN) {
                    if (conditionPath.met()) {
                        max = Integer.min(max, condition.conditionValue() - 1);
                    } else {
                        min = Integer.max(min, condition.conditionValue());
                    }
                } else { // MORE_THAN case
                    if (conditionPath.met()) {
                        min = Integer.max(min, condition.conditionValue() + 1);
                    } else {
                        max = Integer.min(max, condition.conditionValue());
                    }
                }
            }
            if (min > max) { // no way to fulfill these constraints
                return 0L;
            }
            acc *= max - min + 1;
        }
        // System.out.println("Condition path " + conditionPathArgument + " can be fulfilled in " + acc + " ways");
        return acc;
    }

    public static record ConditionPath(Condition condition, boolean met) {}

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
