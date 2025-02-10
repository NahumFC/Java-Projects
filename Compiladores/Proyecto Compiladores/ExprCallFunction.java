package interprete;



import java.util.List;

public class ExprCallFunction extends Expression{
    final Expression callee;
    // final Token paren;
    final List<Expression> arguments;

    ExprCallFunction(Expression callee, /*Token paren,*/ List<Expression> arguments) {
        this.callee = callee;
        // this.paren = paren;
        this.arguments = arguments;
    }
    
    @Override
    public String toString(){
        String exprCall = "";
        if (arguments != null) {
            for (int i = 0; i < arguments.size(); i++) {
                if (arguments.get(i) != null)
                    exprCall+= arguments.get(i).toString();
                else
                    exprCall+= "null";
                if (i < arguments.size() - 1)
                    exprCall+= ", ";
            }
        }
        return callee + "(" + exprCall + ")";
    }
}
