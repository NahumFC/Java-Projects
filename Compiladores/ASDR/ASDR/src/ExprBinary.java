public class ExprBinary extends Expression{
    final Expression left;
    final Token operator;
    final Expression right;

    ExprBinary(Expression left, Token operator, Expression right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    public Object Solve(TablaSimbolos Tabla) {

        Object a = left.Solve(Tabla);
        Object b = right.Solve(Tabla);

        switch (operator.tipo){
            case MENOS :
                return Double.parseDouble(a.toString()) - Double.parseDouble(b.toString());
            case MAS:
                if(a.getClass().getSimpleName().equals("String") || b.getClass().getSimpleName().equals("String")){
                    return a.toString() + b.toString();
                }
                return Double.parseDouble(a.toString()) + Double.parseDouble(b.toString());
            case DIAGONAL:
                return Double.parseDouble(a.toString()) / Double.parseDouble(b.toString());
            case ASTERISCO:
                return Double.parseDouble(a.toString()) * Double.parseDouble(b.toString());
        }

        throw new RuntimeException("Operador '"+operator.lexema+"' no reconocido");
    }
}
