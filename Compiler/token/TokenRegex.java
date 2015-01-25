package token;

import java.util.regex.Pattern;

public class TokenRegex {
    
    /**
     * Regex
     * 
     * If the token matches with this, it is a valid token
     */
    public final Pattern regex;
    
    /**
     * Token type
     * 
     */
    public final TokenType type;

    public TokenRegex(Pattern regex, TokenType type) {
        super();
        this.regex = regex;
        this.type = type;
    }
}