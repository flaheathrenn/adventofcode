package aoc2023.day7;

public enum HandType {
    FIVEOFAKIND(6),
    FOUROFAKIND(5),
    FULLHOUSE(4),
    THREEOFAKIND(3),
    TWOPAIR(2),
    ONEPAIR(1),
    HIGHCARD(0);

    int strength;

    HandType(int strength) {
        this.strength = strength;
    }
}
