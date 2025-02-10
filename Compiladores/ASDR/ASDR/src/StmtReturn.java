public class StmtReturn extends Statement {
    final Expression value;

    StmtReturn(Expression value) {
        this.value = value;
    }

    @Override
    public Object Execute(TablaSimbolos Tabla) {
        return value.Solve(Tabla);
    }
}
