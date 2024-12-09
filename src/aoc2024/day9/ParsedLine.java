package aoc2024.day9;

import java.util.HashSet;
import java.util.Set;

public class ParsedLine {

    // State
    long star1checksum = 0;
    long star2checksum = 0;

    // Parsing
    public ParsedLine(String line) {
        calculateStar1Checksum(line);
        calculateStar2Checksum(line);
        System.out.println();
    }

    private void calculateStar1Checksum(String line) {
        long maximumFileId = line.length() / 2;

        long currentFileId = 0;
        long currentDiskPosition = 0;
        long currentEndFileId = maximumFileId;
        int currentEndFilePointer = line.length() - 1;
        int currentBlocksOfEndFileRemaining = readDigit(currentEndFilePointer, line);
        main: for (int diskPointer = 0; diskPointer < line.length(); diskPointer += 2) {
            // have we finished?
            if (currentFileId == currentEndFileId) {
                break;
            }

            // read in blocks of two digits
            // first, the first character is just a file block
            int fileBlockLength = readDigit(diskPointer, line);
            for (long diskPosition = currentDiskPosition; diskPosition < currentDiskPosition + fileBlockLength; diskPosition++) {
                // System.out.println(String.format("%d * %d = %d", diskPosition, currentFileId, diskPosition * currentFileId));
                // System.out.print(currentFileId);
                star1checksum += currentFileId * diskPosition;
            }
            currentDiskPosition += fileBlockLength;
            currentFileId++;
            // now, deal with the free space
            if (diskPointer + 1 == line.length()) {
                return; // reached end of string, though this should never happen, whoops
            }
            int freeSpaceLength = readDigit(diskPointer + 1, line);
            for (int blocksMovedNow = 0; blocksMovedNow < freeSpaceLength; blocksMovedNow++) {
                if (currentBlocksOfEndFileRemaining == 0) {
                    // start moving a new file
                    currentEndFileId--;
                    if (currentEndFileId <= currentFileId) {
                        // implies end
                        break main;
                    }
                    currentEndFilePointer -= 2;
                    currentBlocksOfEndFileRemaining = readDigit(currentEndFilePointer, line);
                }
                // System.out.println(String.format("%d * %d = %d", currentDiskPosition + blocksMovedNow, currentEndFileId, (currentDiskPosition + blocksMovedNow) * currentEndFileId));
                // System.out.print(currentEndFileId);
                star1checksum += (currentDiskPosition + blocksMovedNow) * currentEndFileId;
                currentBlocksOfEndFileRemaining--;
            }
            currentDiskPosition = currentDiskPosition + freeSpaceLength;
        }

        if (currentBlocksOfEndFileRemaining != 0) {
            int blockCounter = 0;
            // System.out.print(currentEndFileId);
            star1checksum += (currentDiskPosition + blockCounter) * currentEndFileId;
            currentBlocksOfEndFileRemaining--;
        }
    }

    private void calculateStar2Checksum(String line) {
        long maximumFileId = line.length() / 2;
        int endOfString = line.length() - 1;

        long currentFileId = 0;
        long currentDiskPosition = 0;
        Set<Long> idsAlreadyProcessed = new HashSet<>();
        for (int diskPointer = 0; diskPointer < line.length(); diskPointer += 2) {
            // read in blocks of two digits
            // first, the first character is just a file block
            int fileBlockLength = readDigit(diskPointer, line);
            for (long diskPosition = currentDiskPosition; diskPosition < currentDiskPosition + fileBlockLength; diskPosition++) {
                boolean used = idsAlreadyProcessed.contains(currentFileId);
                // System.out.print(used ? "." : currentFileId);
                star2checksum += (used ? 0 : currentFileId) * diskPosition;
            }
            idsAlreadyProcessed.add(currentFileId);
            currentDiskPosition += fileBlockLength;
            currentFileId++;
            // now, deal with the free space
            if (diskPointer + 1 == line.length()) {
                return; // reached end of string, though this should never happen
            }
            int freeSpaceLength = readDigit(diskPointer + 1, line);
            if (freeSpaceLength == 0) {
                continue;
            }
            long foundFileId = maximumFileId;
            for (int pointer = endOfString; pointer > diskPointer + 1; pointer -= 2) {
                if (idsAlreadyProcessed.contains(foundFileId)) {
                    foundFileId--;
                    continue;
                }
                int blockLength = readDigit(pointer, line);
                if (blockLength <= freeSpaceLength) {
                    // move file
                    for (long diskPosition = currentDiskPosition; diskPosition < currentDiskPosition + blockLength; diskPosition++) {
                        // System.out.print(foundFileId);
                        star2checksum += foundFileId * diskPosition;
                    }
                    idsAlreadyProcessed.add(foundFileId);
                    freeSpaceLength -= blockLength;
                    currentDiskPosition += blockLength;
                    if (freeSpaceLength == 0) {
                        break;
                    }
                }
                foundFileId--;
            }
            // for (int i = 0; i < freeSpaceLength; i++) {
            //     System.out.print(".");
            // }
            currentDiskPosition = currentDiskPosition + freeSpaceLength;
        }
    }

    private static int readDigit(int index, String s) {
        return Integer.valueOf(Character.toString(s.charAt(index)));
    }

}