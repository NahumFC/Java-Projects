import java.util.ArrayList;
import java.util.List;

public class ExprCallFunction extends Expression{
    final Expression callee;
    // final Token paren;
    final List<Expression> arguments;

    ExprCallFunction(Expression callee, /*Token paren,*/ List<Expression> arguments) {
        this.callee = callee;
        // this.paren = paren;
        this.arguments = arguments;
    }

    @Override
    public Object Solve(TablaSimbolos Tabla) {

        Object call = callee.Solve(Tabla);

        if(Tabla.existeIdentificador(((StmtFunction)call).name.lexema)){
            List<Object> args = new ArrayList<>();

            for(Expression expr : arguments){
                args.add(expr.Solve(Tabla));
            }

            TablaSimbolos Tabla_fun = new TablaSimbolos();

            for(int i=0; i< args.size(); i++){
                Tabla_fun.asignar(((StmtFunction)call).params.get(i).lexema,args.get(i));
            }

            return ((StmtFunction)call).body.Execute(Tabla_fun);

        }
        else {
            throw new RuntimeException("La funcion '" + ((StmtFunction)call).name.lexema + "' no existe.");
        }

    }
}
