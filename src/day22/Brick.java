package day22;

import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Brick implements Comparable<Brick> {
    final static Pattern regex = Pattern.compile("(\\d+),(\\d+),(\\d+)~(\\d+),(\\d+),(\\d+)");

    // State
    final int startX;
    final int startY;
    final int startZ;
    final int endX;
    final int endY;
    final int endZ;

    final Set<Brick> supportedBricks = new TreeSet<>();
    final Set<Brick> supportingBricks = new TreeSet<>();

    // Parsing
    public Brick(String line) {
        // 1,0,1~1,2,1
        Matcher m = regex.matcher(line);
        m.find();
        startX = Integer.parseInt(m.group(1));
        startY = Integer.parseInt(m.group(2));
        startZ = Integer.parseInt(m.group(3));
        endX = Integer.parseInt(m.group(4));
        endY = Integer.parseInt(m.group(5));
        endZ = Integer.parseInt(m.group(6));
    }

    public int lowestZ() {
        return Integer.min(startZ, endZ);
    }

    @Override
    public int compareTo(Brick o) {
        int comparisonResult = Integer.compare(this.lowestZ(), o.lowestZ());
        if (comparisonResult != 0) {
            return comparisonResult;
        }
        comparisonResult = Integer.compare(this.startY, o.startY);
        if (comparisonResult != 0) {
            return comparisonResult;
        }
        return Integer.compare(this.startX, o.startX);
    }

}