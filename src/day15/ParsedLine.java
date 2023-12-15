package day15;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParsedLine {
    private final Pattern STEP_PATTERN = Pattern.compile("([a-z]+)([-=])(\\d)?");

    // State
    Map<Integer, List<Lens>> boxes = new HashMap<Integer, List<Lens>>();
    long totalFocusingPower = 0;

    // Parsing
    public ParsedLine(String line) {
        // Init boxes map
        for (int i = 0; i < 256; i++) {
            boxes.put(i, new ArrayList<Lens>());
        }

        // Perform steps
        for (String step : line.split(",")) {
            Matcher matcher = STEP_PATTERN.matcher(step);
            matcher.matches();
            String label = matcher.group(1);
            String operation = matcher.group(2);
            switch (operation) {
                case "-":
                    Lens lensToRemove = new Lens(label, 0);
                    boxes.get(lensToRemove.hashCode()).remove(lensToRemove);
                    break;
                case "=":
                    int focalLength = Integer.parseInt(matcher.group(3));
                    Lens lensToAdd = new Lens(label, focalLength);
                    List<Lens> boxContents = boxes.get(lensToAdd.hashCode());
                    int index = boxContents.indexOf(lensToAdd);
                    if (index == -1) { // not already present
                        boxContents.add(lensToAdd);
                        break;
                    }
                    boxContents.remove(index);
                    boxContents.add(index, lensToAdd);
                    break;
                default:
                    throw new IllegalStateException();
            }
        }

        // Calculate total focusing power
        for (int boxIndex = 0; boxIndex < 256; boxIndex++) {
            List<Lens> boxContents = boxes.get(boxIndex);
            if (boxContents.isEmpty()) {
                continue;
            }
            /*
             * One plus the box number of the lens in question.
The slot number of the lens within the box: 1 for the first lens, 2 for the second lens, and so on.
The focal length of the lens.
             */

             for (int slotIndex = 0; slotIndex < boxContents.size(); slotIndex++) {
                Lens lens = boxContents.get(slotIndex);
                long focusingPower = (1+boxIndex) * (slotIndex+1) * (long) lens.focalLength();
                // System.out.println(String.format(
                //     "%s: %d (box %d) * %d (%dth slot) * %d (focal length) = %d",
                //     lens.label(), boxIndex+1, boxIndex, slotIndex+1, slotIndex, lens.focalLength(), focusingPower));
                totalFocusingPower += focusingPower;
             }
        }
    }

}