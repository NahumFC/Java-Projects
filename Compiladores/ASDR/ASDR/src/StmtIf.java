public class StmtIf extends Statement {
    final Expression condition;
    final Statement thenBranch;
    final Statement elseBranch;

    StmtIf(Expression condition, Statement thenBranch, Statement elseBranch) {
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }

    @Override
    public Object Execute(TablaSimbolos Tabla) {

       if( !condition.Solve(Tabla).getClass().getSimpleName().equals("ExprLogical") && !condition.Solve(Tabla).getClass().getSimpleName().equals("Boolean") ){
           throw new RuntimeException("La condición '" + condition.toString() + "' no se puede cumplir o no es valida.");
       }
       Object condicion = condition.Solve(Tabla);

       if(Boolean.parseBoolean(condicion.toString())){
           return thenBranch.Execute(Tabla);
       }
       else{
           if(elseBranch!=null)
            return elseBranch.Execute(Tabla);
       }
       return null;
    }
}
