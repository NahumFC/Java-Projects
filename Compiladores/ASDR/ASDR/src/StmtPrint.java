public class StmtPrint extends Statement {
    final Expression expression;

    StmtPrint(Expression expression) {
        this.expression = expression;
    }

    @Override
    public Object Execute(TablaSimbolos Tabla) {
        System.out.println(expression.Solve(Tabla).toString());
        return null;
    }
}
