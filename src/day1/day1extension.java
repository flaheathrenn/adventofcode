package day1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class day1extension {
    public static void main(String[] args) {
        final String calibrationDocumentFilename = "src/day1/input.txt";
        try (BufferedReader calibrationDocumentReader = new BufferedReader(new FileReader(calibrationDocumentFilename))) {
            int acc = 0;
            for (String calibrationLine = calibrationDocumentReader.readLine(); calibrationLine != null; calibrationLine = calibrationDocumentReader.readLine()) {
                acc += extractCalibrationValue(calibrationLine);
            }
            System.out.println("Sum of all calibration values:");
            System.out.println(acc);
            System.exit(0);
        } catch (IOException e) {
            System.err.println("Exception reading file " + calibrationDocumentFilename + ":");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static int extractCalibrationValue(String line) {
        String firstDigitText = findFirstToken(line, TOKENS.keySet());
        String lastDigitTextReversed = findFirstToken(
            reverse(line),
            TOKENS.keySet().stream().map(s -> reverse(s)).collect(Collectors.toSet())
        );
        return Integer.parseInt(TOKENS.get(firstDigitText) + TOKENS.get(reverse(lastDigitTextReversed)));
    }

    /**
     * Find the first occurrence of any of a set of strings inside a larger string,
     * and return the member of the set that was found.
     */
    private static String findFirstToken(String line, Set<String> tokens) {
        for (String token : tokens) {
            if (line.startsWith(token)) {
                return token;
            }
        }
        return findFirstToken(line.substring(1), tokens);
    }

    private static String reverse(String s) {
        return new StringBuffer(s).reverse().toString();
    }

    private static final Map<String, String> TOKENS = Map.ofEntries(
        Map.entry("0", "0"),
        Map.entry("1", "1"),
        Map.entry("2", "2"),
        Map.entry("3", "3"),
        Map.entry("4", "4"),
        Map.entry("5", "5"),
        Map.entry("6", "6"),
        Map.entry("7", "7"),
        Map.entry("8", "8"),
        Map.entry("9", "9"),
        Map.entry("one", "1"),
        Map.entry("two", "2"),
        Map.entry("three", "3"),
        Map.entry("four", "4"),
        Map.entry("five", "5"),
        Map.entry("six", "6"),
        Map.entry("seven", "7"),
        Map.entry("eight", "8"),
        Map.entry("nine", "9")
    );

}
