import assembler.Assembler;

import java.io.IOException;
import java.util.Scanner;

public class Driver {
    public static void main(String[] args) throws IOException {
        System.out.println("Enter file path to Hack assembly file: ");
        Scanner scan = new Scanner(System.in);
        String machineLanguagePath = scan.next();
        Assembler assembler = new Assembler(machineLanguagePath, "/Users/schachte/Desktop/test.hack");
    }
}
