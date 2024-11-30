package aoc2023.day5;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class Solver {

    public static void main(String[] args) {
        try (BufferedReader inputFileReader = new BufferedReader(new FileReader("src/aoc2023/day5/input.txt"))) {
            String seedsLine = inputFileReader.readLine();
            List<Long> currentList = Arrays.stream(seedsLine.replace("seeds: ", "").split(" "))
                    .filter(s -> !s.isBlank())
                    .map(Long::parseLong)
                    .toList();
            inputFileReader.readLine(); // throw away blank line
            inputFileReader.readLine(); // throw away first map heading
            List<AlmanacMapEntry> currentAlmanacEntriesList = new ArrayList<>();
            for (String inputLine = inputFileReader.readLine();; inputLine = inputFileReader.readLine()) {
                if (inputLine == null) {
                    // do end run code
                    List<Long> solutionList = makeNextList(currentList, currentAlmanacEntriesList);
                    System.out.println(solutionList.stream().min(Long::compareTo).get());
                    break;
                }
                if (inputLine.endsWith("map:")) {
                    // do end map code
                    List<Long> nextList = makeNextList(currentList, currentAlmanacEntriesList);
                    System.out.println(
                            "Mapped " + currentList.stream().map(String::valueOf).collect(Collectors.joining(","))
                                    + " to " + nextList.stream().map(String::valueOf).collect(Collectors.joining(",")));
                    currentList = nextList;
                    currentAlmanacEntriesList = new ArrayList<>();
                    continue;
                }
                if (inputLine.isBlank()) {
                    continue;
                }
                String[] almanacEntryComponents = inputLine.split(" ");
                currentAlmanacEntriesList.add(new AlmanacMapEntry(
                        Long.parseLong(almanacEntryComponents[0]),
                        Long.parseLong(almanacEntryComponents[1]),
                        Long.parseLong(almanacEntryComponents[2])));
            }
        } catch (IOException e) {
            System.err.println("Exception reading file:");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static List<Long> makeNextList(List<Long> currentList, List<AlmanacMapEntry> almanacEntries) {
        List<Long> currentListCopy = new ArrayList<>(currentList);
        List<Long> nextList = new ArrayList<>(currentList.size());

        for (AlmanacMapEntry almanacEntry : almanacEntries) {
            Iterator<Long> sourceIterator = currentListCopy.iterator();
            while (sourceIterator.hasNext()) {
                Long sourceId = sourceIterator.next();
                almanacEntry.convert(sourceId).ifPresent(destinationId -> {
                    nextList.add(destinationId);
                    sourceIterator.remove();
                });
            }
        }
        nextList.addAll(currentListCopy); // add unmapped entries
        return nextList;
    }
}
