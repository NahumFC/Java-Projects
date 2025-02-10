package Analizador;

public class Token {

    final TipoToken tipo;
    final String lexema;

    public Token(TipoToken tipo, String lexema) {
        this.tipo = tipo;
        this.lexema = lexema;
    }



    public String toString() {
        return "<" + tipo + "> " + lexema + " ";
    }
}
