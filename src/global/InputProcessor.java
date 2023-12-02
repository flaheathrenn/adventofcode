package global;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.function.BiFunction;
import java.util.function.Function;

public class InputProcessor {
    private final String filePath;

    public InputProcessor(String filePath) {
        this.filePath = filePath;
    }

    public <T, U> U processLines(Function<String, T> lineParser, BiFunction<T, U, U> stateUpdater, U accumulator) {
        try (BufferedReader inputFileReader = new BufferedReader(new FileReader(this.filePath))) {
            for (String inputLine = inputFileReader.readLine(); inputLine != null; inputLine = inputFileReader.readLine()) {
                T result = lineParser.apply(inputLine);
                accumulator = stateUpdater.apply(result, accumulator);
            }
            return accumulator;
        } catch (IOException e) {
            System.err.println("Exception reading file " + this.filePath + ":");
            e.printStackTrace();
            System.exit(1);
            return accumulator;
        }
    }
}
