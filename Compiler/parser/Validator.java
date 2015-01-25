package parser;

import token.TokenType;

public class Validator {
    
    public static ContextValidator factory(TokenType type) {
        
        ContextValidator validator;
        switch (type) {
            case OPERATOR_PLUSMIN:
                validator = new UnitaryValidator();
                break;
            default:
                validator = new NullValidator();
                break;
        }
        
        return validator;
    }
    
}