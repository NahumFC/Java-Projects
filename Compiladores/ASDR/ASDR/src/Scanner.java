import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scanner {
    private final String source;

    private final List<Token> tokens = new ArrayList<>();

    private static final Map<String, TipoToken> palabrasReservadas;
    static {
        palabrasReservadas = new HashMap<>();
        palabrasReservadas.put("fun", TipoToken.FUN);
        palabrasReservadas.put("var", TipoToken.VAR);
        palabrasReservadas.put("print", TipoToken.PRINT);
        palabrasReservadas.put("return", TipoToken.RETURN);
        palabrasReservadas.put("null", TipoToken.NULL);
        palabrasReservadas.put("if", TipoToken.IF);
        palabrasReservadas.put("else", TipoToken.ELSE);
        palabrasReservadas.put("while", TipoToken.WHILE);
        palabrasReservadas.put("for", TipoToken.FOR);
        palabrasReservadas.put("or", TipoToken.OR);
        palabrasReservadas.put("and", TipoToken.AND);
        palabrasReservadas.put("true", TipoToken.TRUE);
        palabrasReservadas.put("false", TipoToken.FALSE);
    }

    Scanner(String source){
        this.source = source + " ";
    }

    List<Token> scanTokens(){
        int estado = 0;
        char caracter = 0;
        String lexema = "";
        int inicioLexema = 0;

        for(int i=0; i<source.length(); i++){
            caracter = source.charAt(i);

            switch (estado){
                case 0:
                    if(caracter == '*'){
                        tokens.add(new Token(TipoToken.ASTERISCO, "*", i + 1));
                    }
                    else if(caracter == ','){
                        tokens.add(new Token(TipoToken.COMA, ",", i + 1));
                    }
                    else if(caracter == '.'){
                        tokens.add(new Token(TipoToken.PUNTO, ".", i + 1));
                    }
                    else if(caracter == '/'){
                        estado=5;
                        lexema=lexema+caracter;
                        inicioLexema=i;
                    }
                    else if(caracter == ';'){
                        tokens.add(new Token(TipoToken.PUNTO_Y_COMA, ";", i + 1));
                    }
                    else if(caracter == '+'){
                        tokens.add(new Token(TipoToken.MAS, "+", i + 1));
                    }
                    else if(caracter == '-'){
                        tokens.add(new Token(TipoToken.MENOS, "-", i + 1));
                    }
                    else if(caracter == '('){
                        tokens.add(new Token(TipoToken.PARENTESIS_IZQUIERDO, "(", i + 1));
                    }
                    else if(caracter == ')'){
                        tokens.add(new Token(TipoToken.PARENTESIS_DERECHO, ")", i + 1));
                    }
                    else if(caracter == '{'){
                        tokens.add(new Token(TipoToken.CORCHETE_IZQUIERDO, "{", i + 1));
                    }
                    else if(caracter == '}'){
                        tokens.add(new Token(TipoToken.CORCHETE_DERECHO, "}", i + 1));
                    }
                    else if(caracter == '"'){
                        estado=4;
                        lexema = lexema + caracter;
                        inicioLexema = i;
                    }
                    else if(caracter == '!'){
                        estado = 2;
                        lexema = lexema + caracter;
                        inicioLexema = i;
                    }
                    else if(caracter == '='){
                        estado = 2;
                        lexema = lexema + caracter;
                        inicioLexema = i;
                    }
                    else if(caracter == '<'){
                        estado = 2;
                        lexema = lexema + caracter;
                        inicioLexema = i;
                    }
                    else if(caracter == '>'){
                        estado = 2;
                        lexema = lexema + caracter;
                        inicioLexema = i;
                    }
                    else if(Character.isAlphabetic(caracter)){
                        estado = 1;
                        lexema = lexema + caracter;
                        inicioLexema = i;
                    }
                    else if(Character.isDigit(caracter)){
                        estado = 3;
                        lexema = lexema + caracter;
                        inicioLexema = i;
                    }


                    break;

                case 1:
                    if(Character.isAlphabetic(caracter) || Character.isDigit(caracter) ){
                        lexema = lexema + caracter;
                    }
                    else{
                        TipoToken tt = palabrasReservadas.get(lexema);
                        if(tt == null){
                            tokens.add(new Token(TipoToken.IDENTIFICADOR, lexema, inicioLexema + 1));
                        }
                        else{
                            tokens.add(new Token(tt, lexema, inicioLexema + 1));
                        }

                        estado = 0;
                        i--;
                        lexema = "";
                        inicioLexema = 0;
                    }
                    break;

                case 2:
                    if(caracter ==' '){
                        break;
                    }
                    if(caracter == '='){
                        lexema = lexema+caracter;
                        if (lexema.charAt(lexema.length()-2)=='!') {
                            tokens.add(new Token(TipoToken.DIFERENTE, "!=", i + 1));
                        }
                        else if (lexema.charAt(lexema.length()-2)=='<') {
                            tokens.add(new Token(TipoToken.MENOR_IGUAL, "<=", i + 1));
                        }
                        else if (lexema.charAt(lexema.length()-2)=='>') {
                            tokens.add(new Token(TipoToken.MAYOR_IGUAL, ">=", i + 1));
                        }
                        else if (lexema.charAt(lexema.length()-2)=='=') {
                            tokens.add(new Token(TipoToken.IGUAL_IGUAL, "==", i + 1));
                        }
                    }
                    else{
                        i--;
                        if (lexema.charAt(lexema.length()-1)=='!') {
                            tokens.add(new Token(TipoToken.NEGADO, "!", i + 1));
                        }
                        else if (lexema.charAt(lexema.length()-1)=='<') {
                            tokens.add(new Token(TipoToken.MENOR, "<", i + 1));
                        }
                        else if (lexema.charAt(lexema.length()-1)=='>') {
                            tokens.add(new Token(TipoToken.MAYOR, ">", i + 1));
                        }
                        else if (lexema.charAt(lexema.length()-1)=='=') {
                            tokens.add(new Token(TipoToken.IGUAL, "=", i + 1));
                        }
                    }
                    lexema = "";
                    estado = 0;
                    inicioLexema = 0;
                    break;

                case 3:
                    if(Character.isDigit(caracter)){
                        lexema = lexema + caracter;
                    }
                    else if (caracter == '.') {
                        estado = 3;
                        lexema = lexema + caracter;
                    }
                    else{
                        TipoToken tt = palabrasReservadas.get(lexema);
                        if(tt == null){
                            tokens.add(new Token(TipoToken.NUMBER, lexema, inicioLexema + 1));
                        }
                        else{
                            tokens.add(new Token(tt, lexema, inicioLexema + 1));
                        }

                        estado = 0;
                        i--;
                        lexema = "";
                        inicioLexema = 0;
                    }
                    break;

                case 4:
                    if(Character.isAlphabetic(caracter) || Character.isDigit(caracter) || caracter==' ' ){
                        lexema = lexema + caracter;
                    }
                    else if(caracter == '"'){
                        lexema = lexema + caracter;
                        TipoToken tt = palabrasReservadas.get(lexema);
                        if(tt == null){
                            tokens.add(new Token(TipoToken.STRING, lexema.substring(1, lexema.length() - 1), inicioLexema + 1));
                        }
                        else{
                            tokens.add(new Token(tt, lexema, inicioLexema + 1));
                        }

                        estado = 0;
                        lexema = "";
                        inicioLexema = 0;
                    }
                    break;
                case 5:
                    if (caracter == '*') {
                        estado = 6;
                        lexema = lexema + caracter;
                    } else if (caracter == '/') {
                        estado = 8;
                        lexema = lexema + caracter;
                    } else {
                        tokens.add(new Token(TipoToken.DIAGONAL, lexema, inicioLexema));
                        estado = 0;
                        lexema = "";
                        inicioLexema = 0;
                        --i;
                    }
                    break;
                case 6:
                    if (caracter == '*') {
                        estado = 7;
                        lexema = lexema + caracter;
                    } else {
                        estado = 6;
                        lexema = lexema + caracter;
                    }
                    break;
                case 7:
                    if (caracter == '*') {
                        estado = 7;
                        lexema = lexema + caracter;
                    } else if (caracter == '/') {
                        estado = 0;
                        lexema = "";
                    } else {
                        estado = 6;
                        lexema = lexema + caracter;
                    }
                    break;
                case 8:
                    if (caracter == '\n') {
                        estado = 0;
                        lexema = "";
                    } else {
                        estado = 8;
                        lexema = lexema + caracter;
                    }
                    break;
            }
        }
        tokens.add(new Token(TipoToken.EOF, "", source.length()));

        return tokens;
    }

}
