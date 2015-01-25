package token;

public class Token {
    /**
     * Token type
     */
    private TokenType type;
    
    /**
     * Token value
     */
    public final String value;
    
    public final int lineNr;
    
    /**
     * Position in lineNr;
     */
    public final int position;
    
    public final int level;
    
    private Token partner;

    public Token(TokenType type, String value, int lineNr, int position, int level) {
        super();
        this.type = type;
        this.value = value;
        this.lineNr = lineNr;
        this.position = position;
        this.level = level;
    }

    public Token() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Token getPartner() {
        return partner;
    }

    public void setPartner(Token partner) {
        this.partner = partner;
    }
    
    @Override
    public String toString() {
        return value + " | line: " + lineNr + " | pos " + position + " | level: " + level + " | " + getType();
    }

    /**
     * @return the type
     */
    public TokenType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(TokenType type) {
        this.type = type;
    }
    
    
}