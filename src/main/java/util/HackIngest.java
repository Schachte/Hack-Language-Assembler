package util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class HackIngest {

    /**
     * @param path absolute file path of the input file of the assembly code
     * @return list of each line in the input file
     */
    public static List<String> ingestMachineCode(String path) {
        try {
            return postProcess(Files.readAllLines(Paths.get(path)));
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public static void generateBinary(List<String> binary, String outputPath) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath));
        for (String instruction : binary) {
            writer.write(instruction + "\n");
        }
        writer.close();
    }

    /**
     * Cleans up input data for parsing
     */
    private static List<String> postProcess(List<String> parsedInput) {
        return parsedInput.stream()
                // Remove prefixed whitespace
                .map(String::trim)
                // Filter program comments
                .filter(item -> !item.startsWith("//"))
                //Filter whitespace
                .filter(item -> !item.isBlank())
                .collect(Collectors.toList());
    }
}
