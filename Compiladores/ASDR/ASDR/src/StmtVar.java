public class StmtVar extends Statement {
    final Token name;
    final Expression initializer;

    StmtVar(Token name, Expression initializer) {
        this.name = name;
        this.initializer = initializer;
    }

    @Override
    public Object Execute(TablaSimbolos Tabla) {
        if(initializer!=null){
            Object valor = initializer.Solve(Tabla);
            Tabla.asignar(name.lexema,valor);
        }
        else{
            Tabla.asignar(name.lexema, null);
        }
        return null;
    }
}
