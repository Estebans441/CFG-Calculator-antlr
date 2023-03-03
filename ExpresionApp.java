import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.tree.*;

import java.io.*;
import java.util.Scanner;

public class ExpresionApp {
    public static void main(String[] args) throws IOException {
        // 1. Lee entrada crea un flujo de caracteres
        System.out.println("-------------------------------------------");
        System.out.println("Ingrese la ruta del archivo de la expresion...");
        System.out.println("-------------------------------------------");
        Scanner scanner = new Scanner(System.in);
        String filePath = scanner.nextLine();
        FileReader in = new FileReader(filePath);
        //BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        CharStream inputStream = CharStreams.fromReader(in);
        System.out.println("---");
        System.out.println(inputStream.getText(new Interval(0,inputStream.size())));

        // 2. Hace el analisis lexico del flujo y revisa errores
        System.out.println("-------------------------------------------");
        System.out.println("Realizando analisis lexico...");
        ExprLexer lexer = new ExprLexer(inputStream);
        lexer.addErrorListener(new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> r, Object o, int l, int c, String msg, RecognitionException e) {
                throw new RuntimeException(e);
            }
        });
        CommonTokenStream commonTokenStream = new CommonTokenStream(lexer);

        // 3. analiza (parsea) el flujo de tokens generado por el lexer
        ExprParser parser = new ExprParser(commonTokenStream);
        parser.removeErrorListeners();
        parser.setErrorHandler(new BailErrorStrategy() {
            @Override
            public void recover(Parser recognizer, RecognitionException e) {
                throw new RuntimeException(e);
            }
        });
        ParseTree tree;
        try {
            tree = parser.prog();
        } catch (RuntimeException e) {
            System.out.println("Error en la sintaxis... " + e.getMessage());
            return;
        }

        // 4. Evalua el arbol generado
        System.out.println("-------------------------------------------");
        System.out.println("Analisis sintactico...");
        System.out.println("---");
        EvalExprVisitor evaluator = new EvalExprVisitor();
        Integer result = null;
        try {
            result = evaluator.visit(tree);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        System.out.println("-------------------------------------------");
        // 5. Imprime el resultado
        if (result != null) {
            System.out.print("El resultado de la ultima expresion es: ");
            System.out.println(result);
        }
        System.out.println("-------------------------------------------");
    }
}
