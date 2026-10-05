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
                callFunction(tokens, stack);
                return index;
            }
        }

        return index;
    }

    private void print(List<String> tokens, Stack stack) {
        String content = tokens.get(1);

        content = placeVariablesInText(content, stack);

        System.out.println(content);
    }

    private String placeVariablesInText(String text, Stack stack) {
        var variables = stack.getVariables();

        for (String name : variables.keySet()) {
            text = text.replace("{" + name + "}", variables.get(name));
        }

        return text;
    }

    private void define(List<String> tokens, Stack stack) {
        String name = tokens.get(1);
        String value = tokens.get(2);

        // Variable is the result of a function call
        if (value.equals("as")) {
            String functionName = tokens.get(3);
            var arguments = tokens.subList(4, tokens.size());

            Function function = stack.getFunctions().get(functionName);

            // Function does not exist -> Return null
            if (function == null) {
                stack.getVariables().put(name, "null");
                return;
            }

            var res = callFunction(function, arguments, new Stack());
            stack.getVariables().put(name, res);
            return;
        }

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

    private String callFunction(List<String> tokens, Stack stack) {
        String functionName = tokens.get(1);

        var function = stack.getFunctions().get(functionName);

        Stack localStack = new Stack();

        return callFunction(function, tokens, localStack);
    }

    private String callFunction(Function function, List<String> arguments, Stack stack) {
        // Load the function arguments onto the stack
        for (int i = 0; i < function.arguments().size(); i++) {
            stack.getVariables().put(function.arguments().get(i), arguments.get(i));
        }

        // Go through each line in the function and run it
        for (int i = 0; i < function.code().size(); i++) {
            String line = function.code().get(i);

            var functionTokens = Tokenizer.tokenize(line);

            // Detect if a global variable should be used, and add it to the stack.
            if (functionTokens.getFirst().equals("global")) {
                stack.getVariables().put(functionTokens.get(1), globalStack.getVariables().get(functionTokens.get(1)));
            }

            // Returning a value
            if (functionTokens.getFirst().equals("return")) {
                String content = functionTokens.get(1);

                content = placeVariablesInText(content, stack);

                return content;
            }

            // Interpret the line at the code
            interpret(-1, functionTokens, stack);
        }

        return "null";
    }

}
