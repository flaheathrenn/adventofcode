package aoc2025.day3;

public class Accumulator {
    // State
    int star1 = 0;
    long star2 = 0;

    // Update state from parsed line
    public Accumulator update(ParsedLine parsedLine) {
        star1 += findJoltage_star1(parsedLine.bank);
        star2 += findJoltage_star2(parsedLine.bank);
        return this;
    }

    private int findJoltage_star1(int[] bank) {
        int firstDigit = 0, firstDigitLocation = 0, lastDigit = 0;
        for (int i = 0; i < bank.length - 1; i++) {
            if (bank[i] > firstDigit) {
                firstDigit = bank[i];
                firstDigitLocation = i;
            }
        }
        for (int j = firstDigitLocation + 1; j < bank.length; j++) {
            if (bank[j] > lastDigit) {
                lastDigit = bank[j];
            }
        }
        return 10 * firstDigit + lastDigit;
    }

    private long findJoltage_star2(int[] bank) {
        long joltage = 0;
        int previousDigitPosition = -1;
        for (int digit = 1; digit <= 12; digit++) {
            int currentDigitMax = 0, currentDigitPosition = 0;
            for (int i = previousDigitPosition + 1; i < bank.length - (12 - digit); i++) {
                if (bank[i] > currentDigitMax) {
                    currentDigitMax = bank[i];
                    currentDigitPosition = i;
                }
            }
            joltage = joltage * 10 + currentDigitMax;
            previousDigitPosition = currentDigitPosition;
        }
//        System.out.println("Found joltage: " + joltage);
        return joltage;
    }

    // Extract solution
    public String star1() {
        return Integer.toString(star1);
    }

    public String star2() {
        return Long.toString(star2);
    }
}
