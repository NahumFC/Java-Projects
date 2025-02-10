public class StmtExpression extends Statement {
    final Expression expression;

    StmtExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public Object Execute(TablaSimbolos Tabla) {
        return expression.Solve(Tabla);
    }
}
