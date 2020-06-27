package util;

import java.util.HashMap;
import java.util.Map;

public class Parser {

    public static final String DESTINATION = "dest";
    public static final String COMPUTATION = "comp";
    public static final String JUMP = "jump";
    public static final Character JUMP_DELIM = ';';
    public static final Character COMP_DELIM = '=';

    /**
     * Parses and returns all valid variable assignments.
     * Example: "@data //this is a comment" returns "data"
     *
     * @param var input variable declaration that needs to be parsed
     * @return parsed variable that returns all valid values after the assignment declaration
     */
    public static String parseAInstruction(String var) {
        if (var.startsWith("@")) {
            StringBuilder tmp = new StringBuilder();
            char[] dat = var.toCharArray();
            for (int i = 1; i < dat.length; i++) {
                if (dat[i] != ' ' && dat[i] != '/') {
                    tmp.append(dat[i]);
                    continue;
                }
                break;
            }
            return tmp.toString();
        }
        return null;
    }

    /**
     * Preliminary instruction segmentation required to generate binary
     */
    public static Map<String, String> parseCInstruction(String instruction) {
        int idx = 0;
        Map<String, String> instrMapping = new HashMap<>();
        instrMapping.put(DESTINATION, null);
        instrMapping.put(COMPUTATION, null);
        instrMapping.put(JUMP, null);

        boolean isDest = instruction.indexOf(COMP_DELIM) > 0;
        boolean isJump = instruction.indexOf(JUMP_DELIM) > 0;

        StringBuilder currentExec = new StringBuilder();
        if (isDest) {
            while (instruction.charAt(idx) != COMP_DELIM) {
                currentExec.append(instruction.charAt(idx++));
            }
            idx++;
            instrMapping.put(DESTINATION, currentExec.toString());
            currentExec = new StringBuilder();
        }

        if (isJump) {
            while (instruction.charAt(idx) != JUMP_DELIM) {
                currentExec.append(instruction.charAt(idx++));
            }
            idx++;
            instrMapping.put(COMPUTATION, currentExec.toString());
            currentExec = new StringBuilder();

            return evaluateCharacterValue(instruction, idx, instrMapping, currentExec, JUMP);
        }

        return evaluateCharacterValue(instruction, idx, instrMapping, currentExec, COMPUTATION);
    }

    private static Map<String, String> evaluateCharacterValue(String instruction,
                                                              int idx,
                                                              Map<String, String> instrMapping,
                                                              StringBuilder currentExec,
                                                              String jump) {
        while (idx < instruction.length() && instruction.charAt(idx) != ' ' && instruction.charAt(idx) != '/') {
            currentExec.append(instruction.charAt(idx++));
        }
        idx++;
        instrMapping.put(jump, currentExec.toString());
        return instrMapping;
    }

    /**
     * 15-bit padded binary (op-code prefixed via assembler)
     */
    public static String getBinaryRepresentation(int input) {
        return String.format("%15s", Integer.toBinaryString(input)).replace(' ', '0');
    }
}
