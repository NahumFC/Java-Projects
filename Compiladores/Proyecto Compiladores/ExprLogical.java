package interprete;

public class ExprLogical extends Expression{
    final Expression left;
    final Token operator;
    final Expression right;

    ExprLogical(Expression left, Token operator, Expression right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }
    
    @Override
    public String toString(){
       return "(" + left + " " + operator.lexema + " " + right + ")";
    }
}

