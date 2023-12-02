package global;

public class InputProcessorExample {
    public static void main(String[] args) {
        InputProcessor processor = new InputProcessor("src/global/testinput.txt");
        int totalCount = processor.processLines((line) -> {
            return line.length();
        }, (newCount, prevCount) -> {
            return newCount + prevCount;
        }, 0);
        System.out.println(totalCount);
    }
}
