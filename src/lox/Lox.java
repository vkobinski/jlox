package lox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import lox.Scanner;

public class Lox {

    static boolean hadError = false;

    public static void main(String[] args) {

        if(args.length > 1) {
            System.out.println("Usage: jlox [script]");
        } else if (args.length  == 1)  {
            runFile(args[0]);
        } else {
            try {
                runPrompt();
            } catch (Exception e) {
                System.out.println("Could not read STDIN");
            }
        }
    }

    private static void runFile(String path) {
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(path));
            run(new String(bytes, Charset.defaultCharset()));

        } catch (IOException ioException) {
            System.out.println("Could not open [" + path + "]");
        }

        if(hadError) System.exit(65);
    }

    private static void runPrompt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        while(true) {
            System.out.println("> ");
            String line = reader.readLine();
            if(line == null) break;

            run(line);
            hadError = false;

        }

    }

    private static void run(String source) {
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();

        for(Token token : tokens) {
            System.out.println(token);
        }
    }

    static void error(int line, String message) {
        report(line, "", message);
    }

    private static void report(int line, String where, String message) {
        System.out.println("[line " + line + "] Error" + where + ": " + message);
        hadError = true;
    }

}