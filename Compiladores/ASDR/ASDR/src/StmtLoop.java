public class StmtLoop extends Statement {
    final Expression condition;
    final Statement body;

    StmtLoop(Expression condition, Statement body) {
        this.condition = condition;
        this.body = body;
    }

    @Override
    public Object Execute(TablaSimbolos Tabla) {
        if( !condition.Solve(Tabla).getClass().getSimpleName().equals("ExprLogical") && !condition.Solve(Tabla).getClass().getSimpleName().equals("Boolean") ){
            throw new RuntimeException("La condición '" + condition.toString() + "' no se puede cumplir o no es valida.");
        }

        while((boolean) condition.Solve(Tabla)){
            body.Execute(Tabla);
        }
        return null;
    }
}
