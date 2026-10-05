package me.nebu;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Bolt {

    public static void run(File workDirectory) {
        File mainFile = new File(workDirectory, "main.blt");

        List<String> code = new ArrayList<>();

        try (Scanner scanner = new Scanner(mainFile)) {
            while (scanner.hasNextLine()) {
                code.add(scanner.nextLine());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        BoltInterpreter interpreter = new BoltInterpreter(code);

        interpreter.run();
    }

}
