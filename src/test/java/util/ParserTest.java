package util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import java.util.Map;

public class ParserTest {

    @Test
    public void testVariableDeclarationParsingWithComment() {
        String sample = "@myVariable   // comment";
        String result = Parser.parseAInstruction(sample);
        assertEquals(result, "myVariable");
    }

    @Test
    public void testVariableDeclarationParsing() {
        String sample = "@myVariable";
        String result = Parser.parseAInstruction(sample);
        assertEquals(result, "myVariable");
    }

    @Test
    public void testParseNonAlphaNumeric() {
        String sample = "@test_dat";
        String result = Parser.parseAInstruction(sample);
        assertEquals(result, "test_dat");
    }

    @Test
    public void testCInstructionNoDest() {
        String sample = "D+1;JGT ";
        Map<String, String> data = Parser.parseCInstruction(sample);
        assertEquals(data.get("dest"), null);
        assertEquals(data.get("comp"), "D+1");
        assertEquals(data.get("jump"), "JGT");
    }

    @Test
    public void testCInstructionNoJumpWithComment() {
        String sample = "M=1 // i=1";
        Map<String, String> data = Parser.parseCInstruction(sample);
        assertEquals(data.get("dest"), "M");
        assertEquals(data.get("comp"), "1");
        assertEquals(data.get("jump"), null);
    }

    @Test
    public void testCInstructionWithJump() {
        String sample = "D;JGT   ";
        Map<String, String> data = Parser.parseCInstruction(sample);
        assertNull(data.get("dest"));
        assertEquals(data.get("comp"), "D");
        assertEquals(data.get("jump"), "JGT");
    }

    @Test
    public void testCInstructionFull() {
        String sample = "M=D+M;JGT   ";
        Map<String, String> data = Parser.parseCInstruction(sample);
        assertEquals(data.get("dest"), "M");
        assertEquals(data.get("comp"), "D+M");
        assertEquals(data.get("jump"), "JGT");
    }

    @Test
    public void testCInstructionSubtraction() {
        String sample = "D=D-A // D=i-100";
        Map<String, String> data = Parser.parseCInstruction(sample);
        assertEquals(data.get("dest"), "D");
        assertEquals(data.get("comp"), "D-A");
        assertEquals(data.get("jump"), null);
    }
}
