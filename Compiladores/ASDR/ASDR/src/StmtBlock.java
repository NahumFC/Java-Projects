import java.util.List;

public class StmtBlock extends Statement{
    final List<Statement> statements;

    StmtBlock(List<Statement> statements) {
        this.statements = statements;
    }

    @Override
    public Object Execute(TablaSimbolos Tabla) {
        for(Statement Ejecutar : statements){
            if(Ejecutar.getClass().getSimpleName().equals("StmtReturn")) {
                return Ejecutar.Execute(Tabla);
            }
            Ejecutar.Execute(Tabla);
        }
        return null;
    }
}
