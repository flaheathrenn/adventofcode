package day5;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class ExtensionSolver {

    public static void main(String[] args) {
        System.out.println(System.currentTimeMillis());
        try (BufferedReader inputFileReader = new BufferedReader(new FileReader("src/day5/input.txt"))) {
            String seedsLine = inputFileReader.readLine();
            List<Long> seedList = Arrays.stream(seedsLine.replace("seeds: ", "").split(" "))
            .filter(s -> !s.isBlank())
            .map(Long::parseLong)
            .toList();
            List<AlmanacMap> fullAlmanac = new ArrayList<>();
            inputFileReader.readLine(); // throw away blank line
            inputFileReader.readLine(); // throw away first map heading
            SortedSet<AlmanacMapEntry> currentAlmanacEntriesList = new TreeSet<>();
            for (String inputLine = inputFileReader.readLine();; inputLine = inputFileReader.readLine()) {
                if (inputLine == null) {
                    // do end run code
                    fullAlmanac.add(new AlmanacMap(currentAlmanacEntriesList));
                    break;
                }
                if (inputLine.endsWith("map:")) {
                    // do end map code
                    fullAlmanac.add(new AlmanacMap(currentAlmanacEntriesList));
                    currentAlmanacEntriesList = new TreeSet<>();
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

            List<SeedEntry> seedEntries = new ArrayList<>();
            for (int i = 0; i < seedList.size(); i += 2) {
                seedEntries.add(new SeedEntry(seedList.get(i), seedList.get(i + 1)));
            }

            List<SeedEntry> nextStage = new ArrayList<>(seedEntries);
            for (AlmanacMap almanacMap : fullAlmanac) {
                nextStage = transformSeeds(nextStage, almanacMap);
            }
            System.out.println(nextStage.stream().min(Comparable::compareTo).get().start());
        } catch (IOException e) {
            System.err.println("Exception reading file:");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static List<SeedEntry> transformSeeds(List<SeedEntry> inputList, AlmanacMap almanacMap) {
        System.out.println("Initial seed list: " + inputList.stream().map(SeedEntry::toString).collect(Collectors.joining(", ")));
        List<SeedEntry> outputList = new ArrayList<>();
        ListIterator<SeedEntry> iter = inputList.listIterator();
        outer: while (iter.hasNext()) {
            SeedEntry seedEntry = iter.next();
            System.out.println("Processing seed entry " + seedEntry);
            for (AlmanacMapEntry almanacEntry : almanacMap.entries()) {

                // no overlap cases

                if (almanacEntry.sourceRangeStart() > seedEntry.end()) {
                    System.out.println("No overlap between " + seedEntry + " and " + almanacEntry + ", continuing");
                    continue;
                }
                if (seedEntry.start() > almanacEntry.sourceRangeEnd()) {
                    System.out.println("No overlap between " + seedEntry + " and " + almanacEntry + ", continuing");
                    continue;
                }

                // overlap cases
                // seed entry wholly contained in almanac entry
                if (seedEntry.start() >= almanacEntry.sourceRangeStart() &&
                    seedEntry.end() <= almanacEntry.sourceRangeEnd()) {
                    SeedEntry mappedEntry = new SeedEntry(almanacEntry.convert(seedEntry.start()).get(), seedEntry.range());
                    outputList.add(mappedEntry);
                    System.out.println("Seed entry " + seedEntry + " contained within " + almanacEntry + ", mapping to " + mappedEntry + " and breaking");
                    continue outer;
                }

                // almanac entry wholly contained in seed entry
                if (seedEntry.start() < almanacEntry.sourceRangeStart() &&
                    seedEntry.end() > almanacEntry.sourceRangeEnd()) {
                    long nonOverlappingRangeLeft = almanacEntry.sourceRangeStart() - seedEntry.start();
                    SeedEntry unmappedEntryLeft = new SeedEntry(seedEntry.start(), nonOverlappingRangeLeft);
                    addToIteratedList(iter, unmappedEntryLeft); // I think sorting means this can't be mapped but better safe than sorry
                    SeedEntry mappedEntry = new SeedEntry(almanacEntry.destinationRangeStart(), almanacEntry.rangeLength());
                    outputList.add(mappedEntry);
                    long nonOverlappingRangeRight = seedEntry.end() - almanacEntry.sourceRangeEnd();
                    SeedEntry unmappedEntryRight = new SeedEntry(almanacEntry.sourceRangeEnd() + 1, nonOverlappingRangeRight);
                    addToIteratedList(iter, unmappedEntryRight);
                    System.out.println("Seed entry " + seedEntry + " contains within " + almanacEntry + ", mapping to " + mappedEntry + " and adding " + unmappedEntryLeft + " and " + unmappedEntryRight + " and breaking");
                    continue outer;
                }

                // seed entry starts in range but runs over end
                if (seedEntry.start() >= almanacEntry.sourceRangeStart() &&
                    seedEntry.end() > almanacEntry.sourceRangeEnd()) {
                    long overlappingRange = almanacEntry.sourceRangeEnd() - seedEntry.start() + 1;
                    SeedEntry mappedEntry = new SeedEntry(almanacEntry.convert(seedEntry.start()).get(), overlappingRange);
                    outputList.add(mappedEntry);
                    SeedEntry unmappedEntry = new SeedEntry(almanacEntry.sourceRangeEnd() + 1, seedEntry.range() - overlappingRange);
                    addToIteratedList(iter, unmappedEntry); // since it might be mapped by another almanac map entry
                    System.out.println("Seed entry " + seedEntry + " overlaps " + almanacEntry + ", mapping to " + mappedEntry + " and adding " + unmappedEntry + " and breaking");
                    continue outer;
                }

                // seed entry starts outside range but runs into it (but not over)
                if (seedEntry.start() < almanacEntry.sourceRangeStart() &&
                    seedEntry.end() >= almanacEntry.sourceRangeStart()) {
                    long nonOverlappingRange = almanacEntry.sourceRangeStart() - seedEntry.start();
                    SeedEntry unmappedEntry = new SeedEntry(seedEntry.start(), nonOverlappingRange);
                    addToIteratedList(iter, unmappedEntry); // I think sorting means this can't be mapped but better safe than sorry
                    SeedEntry mappedEntry = new SeedEntry(almanacEntry.destinationRangeStart(), almanacEntry.rangeLength());
                    outputList.add(mappedEntry);
                    System.out.println("Seed entry " + seedEntry + " overlaps " + almanacEntry + ", mapping to " + mappedEntry + " and adding " + unmappedEntry + " and breaking");
                    continue outer;
                }
            }
            // no almanac entries were hit so just pass through unchanged
            System.out.println("Seed entry " + seedEntry + " matched no almanac entries, passing through unchanged");
            outputList.add(seedEntry);
        }
        System.out.println("Transformed seed list: " + outputList.stream().map(SeedEntry::toString).collect(Collectors.joining(", ")));
        return outputList;
    }

    private static <T> void addToIteratedList(ListIterator<T> iter, T item) {
        iter.add(item);
        iter.previous();
    }
}
