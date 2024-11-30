package aoc2023.day19;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParsedLine {

    // State
    boolean isWorkflow;
    boolean isPart;

    Workflow workflow;
    Part part;

    // Parsing
    private static final Pattern PART_PATTERN = Pattern.compile("\\{x=(\\d+),m=(\\d+),a=(\\d+),s=(\\d+)\\}");
    private static final Pattern CONDITION_PATTERN = Pattern.compile("([xmas])([<>])(\\d+)");

    public ParsedLine(String line) {
        if (line.startsWith("{")) {
            isPart = true;
            isWorkflow = false;
            Matcher partMatcher = PART_PATTERN.matcher(line);
            partMatcher.find();
            int x = Integer.parseInt(partMatcher.group(1));
            int m = Integer.parseInt(partMatcher.group(2));
            int a = Integer.parseInt(partMatcher.group(3));
            int s = Integer.parseInt(partMatcher.group(4));
            part = new Part(x, m, a, s);
            return;
        }

        if (line.isBlank()) {
            return;
        }

        isPart = false;
        isWorkflow = true;
        // gzb{s>1279:R,m>2526:pzh,s>1141:ls,jzh}
        String[] highLevelSplit = line.split("[{}]");
        String name = highLevelSplit[0];
        workflow = new Workflow(name, new ArrayList<Condition>());
        String[] conditionStrings = highLevelSplit[1].split(",");
        for (String conditionString : conditionStrings) {
            if (!conditionString.contains(":")) {
                workflow.add(new DefaultCaseCondition(conditionString));
            } else {
                String[] highLevelConditionSplit = conditionString.split(":");
                Matcher conditionMatcher = CONDITION_PATTERN.matcher(highLevelConditionSplit[0]);
                conditionMatcher.find();
                Parameter parameter = Parameter.of(conditionMatcher.group(1));
                Comparer comparer = Comparer.of(conditionMatcher.group(2));
                int conditionValue = Integer.parseInt(conditionMatcher.group(3));
                workflow.add(new ComparingCondition(parameter, comparer, conditionValue, highLevelConditionSplit[1]));
            }
        }
    }

    public static record Part(int x, int m, int a, int s) {
    };

    public static record Workflow(String name, List<Condition> conditions) {
        public void add(Condition c) {
            this.conditions.add(c);
        }
    }

    public static interface Condition {
        public String test(Part p);
        public String result();
    }

    public static record ComparingCondition(Parameter parameter, Comparer comparer, int conditionValue, String result)
            implements Condition {

        @Override
        public String test(Part p) {
            int parameterValue = switch (parameter) {
                case X -> p.x;
                case M -> p.m;
                case A -> p.a;
                case S -> p.s;
                default -> throw new IllegalStateException();
            };
            boolean conditionMet = switch (comparer) {
                case LESS_THAN -> parameterValue < conditionValue;
                case MORE_THAN -> parameterValue > conditionValue;
                default -> throw new IllegalStateException();
            };
            if (conditionMet) {
                return result;
            }
            return null;
        }

    }

    public static enum Parameter {
        X, M, A, S;

        public static Parameter of(String s) {
            return switch (s) {
                case "x" -> X;
                case "m" -> M;
                case "a" -> A;
                case "s" -> S;
                default -> throw new IllegalArgumentException();
            };
        }
    }

    public static enum Comparer {
        LESS_THAN, MORE_THAN;

        public static Comparer of(String s) {
            return switch (s) {
                case "<" -> LESS_THAN;
                case ">" -> MORE_THAN;
                default -> throw new IllegalArgumentException();
            };
        }
    }

    public record DefaultCaseCondition(String result) implements Condition {

        @Override
        public String test(Part p) {
            return result;
        }

    }

}