public class ExprGrouping extends Expression {
    final Expression expression;

    ExprGrouping(Expression expression) {
        this.expression = expression;
    }

    @Override
    public Object Solve(TablaSimbolos Tabla) {
        return expression.Solve(Tabla);
    }
}
