package interprete;

public class ExprUnary extends Expression{
    final Token operator;
    final Expression right;

    ExprUnary(Token operator, Expression right) {
        this.operator = operator;
        this.right = right;
    }
    
    @Override
    public String toString(){
       return "(" + operator.lexema + " " + right + ")";
    }
}
