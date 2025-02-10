class ExprLiteral extends Expression {
    final Object value;

    ExprLiteral(Object value) {
        this.value = value;
    }

    @Override
    public Object Solve(TablaSimbolos Tabla) {
        return value;
    }
}
