package token;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizer {

    /**
     * Token description list
     * 
     */
    private final LinkedList<TokenRegex> tokenRegex;
    
    
    /**
     * Token result set
     * 
     * List will be empty until tokenize() is called.
     */
    private final LinkedList<Token> tokens;
    
    /**
     * Counter for the token line number
     */
    private int lineNr  = 1;
    
    
    /**
     * Token position in text
     */
    private int position = 1;
    
    /**
     * Counter for level
     */    
    private int level = 1;
    

    /**
     * Constructor
     */
    public Tokenizer() {
        tokenRegex = new LinkedList<>();
        tokens = new LinkedList<>();
    }
    
    private void init() {
        tokens.clear();
        lineNr = 1;
        position = 1;
        level = 1;
        add("\\n", TokenType.NEWLINE);
    }

    /**
     * Adds a token description
     * 
     * @param regex A valid regex string
     * @param type The token type
     */
    public void add(String regex, TokenType type) {
        tokenRegex.add(new TokenRegex(Pattern.compile("^("+regex+")"), type));
    }

    /**
     * Tokenizes input recursively
     * 
     * @param str   Input string which must be tokenized
     */
    private void _tokenize(String str) {
        boolean match = false;


        Iterator<TokenRegex> it = tokenRegex.iterator();
        while (it.hasNext() && !match) {
            TokenRegex info = it.next();

            Matcher m = info.regex.matcher(customTrim(str));
            if (m.find()) {
                match = true;
                String tok = m.group().trim();
                
                switch (info.type) {
                    case NEWLINE:
                        lineNr++;
                        position = 1;
                        break;
                    case CURLY_OPEN:
                    case PARENTHESES_OPEN:
                        tokens.add(new Token(info.type, tok, lineNr, position, level));
                        level++;
                        break;
                    case CURLY_CLOSE:
                    case PARENTHESES_CLOSE:
                        level--;
                        tokens.add(new Token(info.type, tok, lineNr, position, level));
                        break;
                    default:
                        tokens.add(new Token(info.type, tok, lineNr, position, level));     
                }              

                position += tok.length();
                str = m.replaceFirst("");
            }
        }

        if (!match) {
            throw new RuntimeException("Unexpected character: " + str.substring(0, 1) + " at line " + lineNr);
        }

        if (!str.isEmpty()) {
            _tokenize(str);
        }
    }
    
    /**
     * Custom trim function
     * 
     * Does everything trim() does except for removing new lines
     * 
     * @param str
     * @return cleaned string
     */
    private String customTrim(String str) {
        return str.replaceAll("\t", "")
                  .replaceAll("\r", "")
                  .replaceAll(" ", "");
    }
    
    /**
     * Tokenizes input recursively
     * 
     * @param str   Input string which must be tokenized
     */
    public void tokenize(String str) {
        init();
        _tokenize(str);
    }
    

    /**
     * Retrieve token list
     * 
     * @return List of tokens
     */
    public LinkedList<Token> getTokens() {
        return tokens;
    }
}