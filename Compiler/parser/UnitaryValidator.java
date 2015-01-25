package parser;

public class UnitaryValidator extends ContextValidator {



    @Override
    public boolean isValid() {
        boolean flag = false;
        
        switch (getPreContext().getType()) {
            case CURLY_OPEN:
            case PARENTHESES_OPEN:
            case ENDLINE:
                flag = true;
                break;
        }
        
         switch (getPostContext().getType()) {
            case INT:
            case METHOD:
            case VARIABLE:
                flag = true;
                break;
        }
        return flag;
        
    }
    
    
    
}