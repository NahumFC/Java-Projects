public class ExprAssign extends Expression{
    final Token name;
    final Expression value;

    ExprAssign(Token name, Expression value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public Object Solve(TablaSimbolos Tabla) {

        if(Tabla.existeIdentificador(name.lexema)){
            Object valor = value.Solve(Tabla);
            Tabla.asignar(name.lexema,valor);
            return valor;
        }
        throw new RuntimeException("Variable no definida '" + name.lexema + "'.");
    }
}
