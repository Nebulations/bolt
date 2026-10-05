package me.nebu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BoltInterpreter {

    private final List<String> code;

    private final Stack globalStack = new Stack();

    public BoltInterpreter(List<String> code) {
        this.code = code;
    }

    public void run() {
        for (int i = 0; i < code.size(); i++) {
            String line = code.get(i);
            if (line.isEmpty()) continue;
            i = interpret(i, Tokenizer.tokenize(line), globalStack);
        }
    }

    public int interpret(int index, List<String> tokens, Stack stack) {
        String rootToken = tokens.getFirst();

        switch (rootToken) {
            case "print" -> {
                print(tokens, stack);
                return index;
            }
            case "define" -> {
                define(tokens, stack);
                return index;
            }
            case "function" -> {
                return defineFunction(index, tokens, stack);
            }
            case "call" -> {
                return callFunction(index, tokens, stack);
            }
        }

        return index;
    }

    private void print(List<String> tokens, Stack stack) {
        String content = tokens.get(1);

        var variables = stack.getVariables();

        for (String name : variables.keySet()) {
            content = content.replace("{" + name + "}", variables.get(name));
        }

        System.out.println(content);
    }

    private void define(List<String> tokens, Stack stack) {
        String name = tokens.get(1);
        String value = tokens.get(2);

        stack.getVariables().put(name, value);
    }

    private int defineFunction(int index, List<String> tokens, Stack stack) {
        String functionName = tokens.get(1);

        var args = new ArrayList<>(tokens.subList(2, tokens.size()));

        List<String> functionCode = new ArrayList<>();

        for (int i = index+1; i < code.size(); i++) {
            String line = code.get(i);

            int indentation = line.length() - line.stripIndent().length();

            if (indentation == 0) {
                break;
            }

            functionCode.add(line.substring(indentation));
        }

        Function function = new Function(null, functionName, functionCode, args);
        stack.getFunctions().put(function.name(), function);

        return index;
    }

    private int callFunction(int index, List<String> tokens, Stack stack) {
        String functionName = tokens.get(1);

        var function = stack.getFunctions().get(functionName);

        Stack localStack = new Stack();

        for (int i = 0; i < function.arguments().size(); i++) {
            localStack.getVariables().put(function.arguments().get(i), tokens.get(i+2));
        }

        for (int i = 0; i < function.code().size(); i++) {
            String line = function.code().get(i);

            var functionTokens = Tokenizer.tokenize(line);

            if (functionTokens.getFirst().equals("global")) {
                localStack.getVariables().put(functionTokens.get(1), globalStack.getVariables().get(functionTokens.get(1)));
            }

            interpret(-1, functionTokens, localStack);
        }

        return index;
    }

}
