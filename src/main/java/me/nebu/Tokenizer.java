package me.nebu;

import java.util.ArrayList;
import java.util.List;

public class Tokenizer {

    public static List<String> tokenize(String line) {
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
