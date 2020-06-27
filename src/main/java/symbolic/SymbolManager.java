package symbolic;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles the construction of all hardware defined symbols based on the Hack language specification.
 * Also handles the preliminary construction of the tables for addressing instructions and computation instructions
 * used by the underlying hardware.
 */
public class SymbolManager {
    private static Map<String, Integer> symbolTable;
    private static Map<String, String> compTable;
    private static Map<String, String> destTable;
    private static Map<String, String> jumpTable;

    public static Map<String, Integer> getSymbolTable() {
        if (symbolTable == null) {
            populateSymbolicDefaults();
            return symbolTable;
        }
        return symbolTable;
    }

    public static Map<String, String> getComputationTable() {
        if (compTable == null) {
            compTable = new HashMap<>();
            compTable.put("0", "0101010");
            compTable.put("1", "0111111");
            compTable.put("-1", "0111010");
            compTable.put("D", "0001100");
            compTable.put("A", "0110000");
            compTable.put("M", "1110000");
            compTable.put("!D", "0001101");
            compTable.put("!A", "0110001");
            compTable.put("!M", "1110001");
            compTable.put("-D", "0001111");
            compTable.put("-A", "0110011");
            compTable.put("-M", "1110011");
            compTable.put("D+1", "0011111");
            compTable.put("A+1", "0110111");
            compTable.put("M+1", "1110111");
            compTable.put("D-1", "0001110");
            compTable.put("A-1", "0110010");
            compTable.put("M-1", "1110010");
            compTable.put("D+A", "0000010");
            compTable.put("D+M", "1000010");
            compTable.put("D-A", "0010011");
            compTable.put("D-M", "1010011");
            compTable.put("A-D", "0000111");
            compTable.put("M-D", "1000111");
            compTable.put("D&A", "0000000");
            compTable.put("D&M", "1000000");
            compTable.put("D|A", "0010101");
            compTable.put("D|M", "1010101");
            return compTable;
        }
        return compTable;
    }

    public static Map<String, String> getDestinationTable() {
        if (destTable == null) {
            destTable = new HashMap<>();
            destTable.put("null", "000");
            destTable.put("M", "001");
            destTable.put("D", "010");
            destTable.put("MD", "011");
            destTable.put("A", "100");
            destTable.put("AM", "101");
            destTable.put("AD", "110");
            destTable.put("AMD", "111");
        }
        return destTable;
    }

    public static Map<String, String> getJumpTable() {
        if (jumpTable == null) {
            jumpTable = new HashMap<>();
            jumpTable.put("null", "000");
            jumpTable.put("JGT", "001");
            jumpTable.put("JEQ", "010");
            jumpTable.put("JGE", "011");
            jumpTable.put("JLT", "100");
            jumpTable.put("JNE", "101");
            jumpTable.put("JLE", "110");
            jumpTable.put("JMP", "111");
        }
        return jumpTable;
    }

    public static Map<String, Map<String, String>> getTables() {
        return (
                Map.ofEntries(
                        new AbstractMap.SimpleEntry<>("c", getComputationTable()),
                        new AbstractMap.SimpleEntry<>("j", getJumpTable()),
                        new AbstractMap.SimpleEntry<>("d", getDestinationTable())
                ));
    }

    private static void populateSymbolicDefaults() {
        if (symbolTable != null) return;
        symbolTable = new HashMap<String, Integer>();
        symbolTable.put("SP", 0);
        symbolTable.put("LCL", 1);
        symbolTable.put("ARG", 2);
        symbolTable.put("THIS", 3);
        symbolTable.put("THAT", 4);
        symbolTable.put("R0", 0);
        symbolTable.put("R1", 1);
        symbolTable.put("R2", 2);
        symbolTable.put("R3", 3);
        symbolTable.put("R4", 4);
        symbolTable.put("R5", 5);
        symbolTable.put("R6", 6);
        symbolTable.put("R7", 7);
        symbolTable.put("R8", 8);
        symbolTable.put("R9", 9);
        symbolTable.put("R10", 10);
        symbolTable.put("R11", 11);
        symbolTable.put("R12", 12);
        symbolTable.put("R13", 13);
        symbolTable.put("R14", 14);
        symbolTable.put("R15", 15);
        symbolTable.put("SCREEN", 16384);
        symbolTable.put("KBD", 24576);
    }
}
