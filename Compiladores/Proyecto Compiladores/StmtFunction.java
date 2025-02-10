package interprete;
import java.util.Iterator;
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

    StmtFunction(Token name, Statement params, Statement body) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    @Override
    public String toString(){
        String stmtFun = "fun " + name.lexema + "(";
        for (Iterator<Token> it = params.iterator(); it.hasNext();) {
            Token token = it.next();
            stmtFun += token.lexema;
            if(it.hasNext())
                stmtFun += ", ";
        }
        stmtFun += ")";
        if(body != null)
            stmtFun += "\n" + body;
        return stmtFun;
    }
}
