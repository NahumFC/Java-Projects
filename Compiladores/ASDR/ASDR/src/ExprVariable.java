class ExprVariable extends Expression {
    final Token name;

    ExprVariable(Token name) {
        this.name = name;
    }

    @Override
    public Object Solve(TablaSimbolos Tabla) {
        if(Tabla.existeIdentificador(name.lexema))
            return Tabla.obtener(name.lexema);

        return new RuntimeException("Variable no definida '" + name.lexema + "'.");
    }
}