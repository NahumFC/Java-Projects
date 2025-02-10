public class ExprUnary extends Expression{
    final Token operator;
    final Expression right;

    ExprUnary(Token operator, Expression right) {
        this.operator = operator;
        this.right = right;
    }

    @Override
    public Object Solve(TablaSimbolos Tabla) {
        Object expr = right.Solve(Tabla);

        if (operator.tipo == TipoToken.NEGADO) {
            return !Boolean.parseBoolean(expr.toString());
        } else if (operator.tipo == TipoToken.MENOS) {
            return -Double.parseDouble(expr.toString());
        } else {
            throw new RuntimeException("Operador '" + operator.lexema + "' no reconocido");
        }
    }
}
