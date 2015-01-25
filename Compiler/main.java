
import parser.Parser;
import token.Token;
import token.TokenType;
import token.Tokenizer;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class main {

    public static void main(String[] args) {
        Tokenizer tokenizer = new Tokenizer();
        tokenizer.add("while", TokenType.WHILE);
        tokenizer.add("shout", TokenType.METHOD); // function
        tokenizer.add("if", TokenType.IF);
        tokenizer.add("else", TokenType.ELSE);
        tokenizer.add("\\{", TokenType.CURLY_OPEN);
        tokenizer.add("\\}", TokenType.CURLY_CLOSE);
        tokenizer.add("\\(", TokenType.PARENTHESES_OPEN); // open bracket
        tokenizer.add("\\)", TokenType.PARENTHESES_CLOSE); // close bracket
        tokenizer.add("\\+\\+|\\-\\-", TokenType.OPERATOR_PLUSMIN_UNI_DOUBLE);
        tokenizer.add("[-+]", TokenType.OPERATOR_PLUSMIN);
       
        tokenizer.add("[<>]", TokenType.OPERATOR_BIGSMALL);
        tokenizer.add("==", TokenType.OPERATOR_EQUAL); 
         tokenizer.add("!=", TokenType.OPERATOR_NOT_EQUAL); 
        tokenizer.add("[=]", TokenType.OPERATOR_ASSIGN); 
        tokenizer.add("[*/]", TokenType.OPERATOR_MULTDIV); // mult or divide
        tokenizer.add("[0-9]+", TokenType.INT); // integer number
        tokenizer.add("\"[a-zA-Z][a-zA-Z0-9_]*\"", TokenType.STRING); // string number
        tokenizer.add("[a-zA-Z][a-zA-Z0-9_]*", TokenType.VARIABLE); // variable
        tokenizer.add("[;]", TokenType.ENDLINE); // variable

        try {
            tokenizer.tokenize(readFile("C:\\Users\\Gijs\\Desktop\\lang.txt", Charset.defaultCharset()));

            
            Parser parser = new Parser(tokenizer.getTokens());
            parser.parse();

            for (Token tok : tokenizer.getTokens()) {
                    System.out.println(tok);
                    
                    Token partner = tok.getPartner();
                    
                    if (partner != null) {
                        System.out.println("==> HasPartner: " + partner);
                    }
                    
                    System.out.println("");
            }
        } 
        catch (IOException e) {
            System.err.println("IOError: " + e.getMessage());
        }
        catch (RuntimeException e) {
             System.err.println("Error: " + e.getMessage());
        }
    }
    
    public static String readFile(String path, Charset encoding) 
        throws IOException 
      {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
      }
}