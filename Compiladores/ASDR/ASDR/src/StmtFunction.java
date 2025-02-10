import java.util.List;

public class StmtFunction extends Statement {
    final Token name;
    final List<Token> params;
    final StmtBlock body;

    StmtFunction(Token name, List<Token> params, StmtBlock body) {
        this.name = name;
        this.params = params;
        this.body = body;
    }

    @Override
    public Object Execute(TablaSimbolos Tabla) {
        if(!Tabla.existeIdentificador(name.lexema)){
            Tabla.asignar(name.lexema,this);
        }
        else{
            throw new RuntimeException("La funcion '" + name.lexema + "' ya existe.");
        }
        return null;
    }
}
