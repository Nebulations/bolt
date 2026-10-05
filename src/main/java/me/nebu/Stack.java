package me.nebu;

import java.util.HashMap;

public class Stack {

    private final HashMap<String, String> variables = new HashMap<>();
    private final HashMap<String, Function> functions = new HashMap<>();

    public HashMap<String, Function> getFunctions() {
        return functions;
    }

    public HashMap<String, String> getVariables() {
        return variables;
    }
}
