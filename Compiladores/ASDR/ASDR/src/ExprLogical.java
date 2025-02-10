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
    public Object Solve(TablaSimbolos Tabla) {
        Object a = left.Solve(Tabla);
        Object b = right.Solve(Tabla);

        switch (operator.tipo){
            case MAYOR:
                return Double.parseDouble(a.toString()) > Double.parseDouble(b.toString());
            case MENOR:
                return Double.parseDouble(a.toString()) < Double.parseDouble(b.toString());
            case MAYOR_IGUAL:
                return Double.parseDouble(a.toString()) >= Double.parseDouble(b.toString());
            case MENOR_IGUAL:
                return Double.parseDouble(a.toString()) <= Double.parseDouble(b.toString());
            case DIFERENTE:
                return Double.parseDouble(a.toString()) != Double.parseDouble(b.toString());
            case IGUAL_IGUAL:
                return Double.parseDouble(a.toString()) == Double.parseDouble(b.toString());
            case AND:
                return Boolean.parseBoolean(a.toString()) &&  Boolean.parseBoolean(b.toString());
            case OR:
                return Boolean.parseBoolean(a.toString()) || Boolean.parseBoolean(b.toString());
        }

        throw new RuntimeException("Operador '"+operator.lexema+"' no reconocido");
    }
}

