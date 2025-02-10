package interprete;

import java.util.List;

public class StmtBlock extends Statement{
    final List<Statement> statements;

    StmtBlock(List<Statement> statements) {
        this.statements = statements;
    }
    
    @Override
    public String toString(){
        String stmtBlock = "";
        if (statements != null) {
            for (int i = 0; i < statements.size(); i++) {
                if (statements.get(i) != null)
                    stmtBlock+= "\t"+ statements.get(i).toString();
                else
                    stmtBlock+= "null";
                if (i < statements.size() - 1)
                    stmtBlock+= "\n";
            }
        }
        return stmtBlock;
    }
}
