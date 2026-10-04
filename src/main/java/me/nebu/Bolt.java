package me.nebu;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Bolt {

    private static List<String> code = new ArrayList<>();

    public static void run(File workDirectory) {
        File mainFile = new File(workDirectory, "main.blt");

        try (Scanner scanner = new Scanner(mainFile)) {
            while (scanner.hasNextLine()) {
                code.add(scanner.nextLine());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        interpret();
    }

    private static void interpret() {
        for (int i = 0; i < code.size(); i++) {
            i = BoltInterpreter.interpret(i, tokenize(code.get(i)));
        }
    }

    private static List<String> tokenize(String line) {
        List<String> tokens = new ArrayList<>();

        StringBuilder sb = new StringBuilder();
        boolean buildingString = false;

        for (int i = 0; i < line.length(); i++) {
            char currentCharacter = line.charAt(i);

            // We're building a string -> ignore spaces
            if (currentCharacter == ' ' && buildingString) {
                sb.append(currentCharacter);
                continue;
            }

            if (currentCharacter == '"') {
                char lastCharacter = line.charAt(i-1);
                // Escaped character
                if (lastCharacter == '\\') {
                    sb.append(currentCharacter);
                    continue;
                }

                buildingString = !buildingString;

                if (!buildingString) {
                    tokens.add(sb.toString());
                    sb.delete(0, sb.length());
                }

                continue;
            }

            // Current char is a space, or we reached the end of the line
            if (currentCharacter == ' ' || i == line.length()-1) {
                if (i == line.length()-1) sb.append(currentCharacter);

                tokens.add(sb.toString());
                sb.delete(0, sb.length());
                continue;
            }

            sb.append(currentCharacter);
        }

        return tokens;
    }

}
