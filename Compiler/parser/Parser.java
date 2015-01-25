package parser;

import parser.ContextValidator;
import parser.Validator;
import token.Token;
import token.TokenType;
import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Stack;

/**
 * Parses tokenlist
 * 
 * Checks whether token list is parseble. For instance, "sina()" will result in a valid TokenSet
 * sin, a, (, )
 * yet it is not valid, as a function should always be followed by an (
 * 
 * @author Gijs
 */
public class Parser {
    
    private Stack<Token> parseStack;
    /**
     * Todo: DoubleLinkedListIfY this
     */
    private final List<Token> tokenList;

    public Parser(List<Token> tokenList) {
        this.tokenList = tokenList;
        parseStack = new Stack<>();
    }
    
    /**
     * @todo refactor this with DoubleLinkedList as it is easier to use
     * @todo should contain more checks than (
     */
    public void parse() {
        Token previous = null;
        for (Token token : tokenList) {
            
            Token stackedToken;
            
            switch(token.getType()) {
                case PARENTHESES_OPEN:
                case CURLY_OPEN:
                case IF:
                    parseStack.add(token);
                    break;

                case ELSE:
                    stackedToken = parseStack.pop();
                    
                    assertToken(stackedToken, TokenType.IF);
                    
                    createPartnership(token, stackedToken);
                    break;
                case PARENTHESES_CLOSE:
                    stackedToken = popTokenExclude(TokenType.IF);
                    
                    assertToken(stackedToken, TokenType.PARENTHESES_OPEN);
                    
                    createPartnership(token, stackedToken);
                    
                    break;
                
                case CURLY_CLOSE:
                    stackedToken =  popTokenExclude(TokenType.IF);
                   
                    assertToken(stackedToken, TokenType.CURLY_OPEN);
                    
                    createPartnership(token, stackedToken);
                    break;                    
                
            }


            if (previous != null) {
                
                if (previous.getType() == TokenType.METHOD && token.getType() != TokenType.PARENTHESES_OPEN) {
                   throw new RuntimeException("Parse Error: Unexpected '"  + token.value + "' Function must be followed by (");
                }
            }
            
            previous = token;            
        }
        
        //remove IF's from stack
        popTokenExclude(TokenType.IF);
        
        if (parseStack.size() > 0) {
            throw new RuntimeException("Bracket or parentheses mismatch!");
        }
        
        postParse();
    }
    
    private void postParse() {
        
        ListIterator<Token> it = (ListIterator) tokenList.iterator();

        Token preContext = null;
        Token postContext = null;
        Token subject = null;
        
        while(it.hasNext()) {
            
            if (it.hasPrevious()) {
                preContext = tokenList.get(it.previousIndex());
            }
            
            if (it.hasNext()) {
                postContext = tokenList.get(it.nextIndex());
            }
            
            subject = it.next();
            
            // only used by operator for now.
            if (subject.getType() != TokenType.OPERATOR_PLUSMIN) continue;
            
            ContextValidator val = Validator.factory(subject.getType());
            val.setPreContextToken(preContext);
            val.setPostContextToken(postContext);
            
            if (val.isValid()) {
                subject.setType(TokenType.OPERATOR_PLUSMIN_UNI);
            }
            
        }
        
    }
    
    /**
     * Make sure that the token type is correct
     * 
     * @param token
     * @param type Expected type
     */
    private void assertToken(Token token, TokenType type) {
        if (token == null || token.getType() != type) {
            throw new RuntimeException("Unexpected token, expected: " + type);
        }
    }
    

    
    /**
     * Create a partnership between tokens:
     * 
     * @param token1
     * @param token2 
     */
    private void createPartnership(Token token1, Token token2) {
        token1.setPartner(token2);
        token2.setPartner(token1);
    }
    
    /**
     * Pops from the list, igoring a token
     * 
     * @param type
     * @return Token
     */
    private Token popTokenExclude(TokenType type) {
        
        try {
            Token token = parseStack.pop();


            while (token.getType() == type) {
                token = parseStack.pop();
            }

            return token;
        } catch (EmptyStackException e) {
            return null;
        }
    }
}