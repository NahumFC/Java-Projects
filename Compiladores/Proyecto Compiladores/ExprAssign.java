package interprete;


public class ExprAssign extends Expression{
    final Token name;
    final Expression value;

    ExprAssign(Token name, Expression value) {
        this.name = name;
        this.value = value;
    }
    
    @Override
    public String toString(){
       return name.lexema+" = " + value;
    }
}
