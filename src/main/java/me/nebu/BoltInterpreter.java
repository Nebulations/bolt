package me.nebu;

import java.util.List;

public class BoltInterpreter {

    public static int interpret(int index, List<String> tokens) {
        String rootToken = tokens.getFirst();

        if (rootToken.equals("print")) {
            print(tokens);
            return index;
        }

        if (rootToken.equals("define")) {
            define(tokens);
            return index;
        }

        return index;
    }

    private static void print(List<String> tokens) {
        System.out.println(tokens.get(1));
    }

    private static void define(List<String> tokens) {

    }

}
