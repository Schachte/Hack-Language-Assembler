package assembler;

import symbolic.SymbolManager;
import util.HackIngest;
import util.Parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static util.Parser.*;

public class Assembler {

    private static final int INITIAL_MEMORY_REGISTER_LOCATION = 16;
    private static final int STARTING_COMMAND_LOCATION = 0;
    private static final String A_INSTRUCTION_PREFIX = "0";
    private static final String C_INSTRUCTION_PREFIX = "111";

    int cmdTracker = STARTING_COMMAND_LOCATION;
    int memTracker = INITIAL_MEMORY_REGISTER_LOCATION;

    private Map<String, Integer> symbolicTable;
    private List<String> parsedCode;
    private List<String> binaryTranslation;

    public Assembler(String filePath, String outputFilePath) throws IOException {
        binaryTranslation = new ArrayList<>();
        symbolicTable = SymbolManager.getSymbolTable();
        initializeAssembler(filePath, outputFilePath);
    }

    private void initializeAssembler(String filePath, String outputFilePath) throws IOException {
        parsedCode = preProcessIngest(filePath);
        labelPopulation();
        variablePopulation();
        processTranslation();
        HackIngest.generateBinary(binaryTranslation, outputFilePath);
    }

    private List<String> preProcessIngest(String filepath) {
        if (filepath == null) {
            throw new IllegalArgumentException("Unable to process nil filepath");
        }
        return HackIngest.ingestMachineCode(filepath);
    }

    private void labelPopulation() {
        for (String row : parsedCode) {
            if (row.startsWith("(")) {
                String currRow = row.substring(1, row.length() - 1);
                if (symbolicTable.containsKey(currRow)) {
                    continue;
                }
                symbolicTable.put(currRow, cmdTracker);
                continue;
            }
            cmdTracker++;
        }
    }

    private void variablePopulation() {
        for (String row : parsedCode) {
            if (row.startsWith("@")) {
                String currRow = Parser.parseAInstruction(row);
                if (symbolicTable.containsKey(currRow) || Character.isDigit(currRow.charAt(0))) {
                    continue;
                }
                symbolicTable.put(currRow, memTracker++);
            }
        }
    }

    private void processTranslation() {
        for (String row : parsedCode) {
            if (row.startsWith("(")) continue;
            if (isAInstruction(row)) {
                String parsedInstruction = Parser.parseAInstruction(row);
                if (Character.isDigit(parsedInstruction.charAt(0))) {
                    String aInstruction = A_INSTRUCTION_PREFIX + Parser.getBinaryRepresentation(Integer.parseInt(parsedInstruction));
                    binaryTranslation.add(aInstruction);
                } else {
                    int instruction = symbolicTable.get(parsedInstruction);
                    String instructionBinary = Parser.getBinaryRepresentation(instruction);
                    String aInstruction = A_INSTRUCTION_PREFIX + instructionBinary;
                    binaryTranslation.add(aInstruction);
                }
            } else {
                StringBuilder cInstruction = new StringBuilder();
                cInstruction.append(C_INSTRUCTION_PREFIX);
                Map<String, String> parsedC = Parser.parseCInstruction(row);

                if (parsedC.get(COMPUTATION) != null) {
                    String cInstructionBinary = SymbolManager.getComputationTable().get(parsedC.get(COMPUTATION));
                    cInstruction.append(cInstructionBinary);
                }

                if (parsedC.get(DESTINATION) != null) {
                    String dInstructionBinary = SymbolManager.getDestinationTable().get(parsedC.get(DESTINATION));
                    cInstruction.append(dInstructionBinary);
                } else {
                    // fallback control bits for no dest
                    cInstruction.append("000");
                }

                if (parsedC.get(JUMP) != null) {
                    String jInstructionBinary = SymbolManager.getJumpTable().get(parsedC.get(JUMP));
                    cInstruction.append(jInstructionBinary);
                } else {
                    // fallback control bits for no jump
                    cInstruction.append("000");
                }

                binaryTranslation.add(cInstruction.toString());
            }
        }
    }

    private boolean isAInstruction(String line) {
        return line.startsWith("@");
    }
}
