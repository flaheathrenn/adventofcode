package day1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;

public class day1 {
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
        String firstDigit = "";
        String lastDigit = "";
        for (String character: line.split("")) {
            if (isDigit(character)) {
                if (firstDigit.isEmpty()) {
                    firstDigit = character;
                }
                lastDigit = character;
            }
        }
        return Integer.parseInt(firstDigit + lastDigit);
    }

    private static final Set<String> DIGITS = Set.of("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");

    private static boolean isDigit(String s) {
        return DIGITS.contains(s);
    }


}
