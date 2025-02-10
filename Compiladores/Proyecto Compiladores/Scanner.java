package interprete;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scanner {

    private static final Map<String, TipoToken> palabrasReservadas;

    static {
        palabrasReservadas = new HashMap<>();
        palabrasReservadas.put("and",    TipoToken.AND);
        palabrasReservadas.put("else",   TipoToken.ELSE);
        palabrasReservadas.put("false",  TipoToken.FALSE);
        palabrasReservadas.put("for",    TipoToken.FOR);
        palabrasReservadas.put("fun",    TipoToken.FUN);
        palabrasReservadas.put("if",     TipoToken.IF);
        palabrasReservadas.put("null",   TipoToken.NULL);
        palabrasReservadas.put("or",     TipoToken.OR);
        palabrasReservadas.put("print",  TipoToken.PRINT);
        palabrasReservadas.put("return", TipoToken.RETURN);
        palabrasReservadas.put("true",   TipoToken.TRUE);
        palabrasReservadas.put("var",    TipoToken.VAR);
        palabrasReservadas.put("while",  TipoToken.WHILE);
    }

    private final String source;

    private final List<Token> tokens = new ArrayList<>();
    
    public Scanner(String source){
        this.source = source + " ";
    }

    public List<Token> scan() throws Exception {
        String lexema = "";
        int estado = 0;
        int linea = 1;
        char c;

        for(int i=0; i<source.length(); i++){
            c = source.charAt(i);
            
            if (c == '\n')
                linea ++;
            
            switch (estado){
                case 0:
                    if(c == '>'){
                        estado = 1;
                        lexema += c;
                    }
                    else if(c == '<'){
                        estado = 4;
                        lexema += c;
                    }
                    else if(c == '='){
                        estado = 7;
                        lexema += c;
                    }
                    else if(c == '!'){
                        estado = 10;
                        lexema += c;
                    }
                    else if(Character.isLetter(c)){
                        estado = 13;
                        lexema += c;
                    }
                    else if(Character.isDigit(c)){
                        estado = 15;
                        lexema += c;

                        /*while(Character.isDigit(c)){
                            lexema += c;
                            i++;
                            c = source.charAt(i);
                        }
                        Token t = new Token(TipoToken.NUMBER, lexema);
                        lexema = "";
                        estado = 0;
                        tokens.add(t);
                        */
                    }
                    else if(c == '"'){
                        estado = 24;
                        lexema += c;
                    }
                    else if(c == '/'){
                        estado = 26;
                        lexema += c;
                    }
                    else if(c == '+'){
                        //estado = 33;
                        lexema += c;
                        Token t = new Token(TipoToken.PLUS, lexema, linea);
                        tokens.add(t);
                        estado = 0;
                        lexema = "";
                    }
                    else if(c == '-'){
                        lexema += c;
                        //estado = 34;
                        Token t = new Token(TipoToken.MINUS, lexema, linea);
                        tokens.add(t);
                        estado = 0;
                        lexema = "";
                    }
                    
                    else if(c == '*'){
                        lexema += c;
                        //estado = 35;
                        Token t = new Token(TipoToken.STAR, lexema, linea);
                        tokens.add(t);
                        estado = 0;
                        lexema = "";
                    }
                    else if(c == '{'){
                        lexema += c;
                        //estado = 36;
                        Token t = new Token(TipoToken.LEFT_BRACE, lexema, linea);
                        tokens.add(t);
                        estado = 0;
                        lexema = "";
                    }
                    else if(c == '}'){
                        lexema += c;
                        //estado = 37;
                        Token t = new Token(TipoToken.RIGHT_BRACE, lexema, linea);
                        tokens.add(t);
                        estado = 0;
                        lexema = "";
                    }
                    else if(c == '('){
                        lexema += c;
                        //estado = 38;
                        Token t = new Token(TipoToken.LEFT_PAREN, lexema, linea);
                        tokens.add(t);
                        estado = 0;
                        lexema = "";
                    }
                    else if(c == ')'){
                        lexema += c;
                        //estado = 39;
                        Token t = new Token(TipoToken.RIGHT_PAREN, lexema, linea);
                        tokens.add(t);
                        estado = 0;
                        lexema = "";
                    }
                    else if(c == ';'){
                        lexema += c;
                        //estado = 40;
                        Token t = new Token(TipoToken.SEMICOLON, lexema, linea);
                        tokens.add(t);
                        estado = 0;
                        lexema = "";
                    }
                    else if(c == ','){
                        lexema += c;
                        //estado = 41;
                        Token t = new Token(TipoToken.COMMA, lexema, linea);
                        tokens.add(t);
                        estado = 0;
                        lexema = "";
                    }
                    else if(c == '.'){
                        lexema += c;
                        //estado = 42;
                        Token t = new Token(TipoToken.DOT, lexema, linea);
                        tokens.add(t);
                        estado = 0;
                        lexema = "";
                    }
                    else if(!Character.isWhitespace(c)){
                        Interprete.error(linea, "Caracter desconocido "+"'"+c+"'");
                        System.exit(65);
                    }
                    break;

                case 1:
                    if(c == '='){
                        //estado = 2;
                        lexema += c;
                        Token t = new Token(TipoToken.GREATER_EQUAL, lexema, linea);
                        tokens.add(t);

                        estado = 0;
                        lexema = "";
                    }
                    else{
                        //estado = 3;
                        Token t = new Token(TipoToken.GREATER, lexema, linea);
                        tokens.add(t);

                        estado = 0;
                        lexema = "";
                        i--;
                    }
                    break;
                
                case 4:
                    if(c == '='){
                        //estado = 5;
                        lexema += c;
                        Token t = new Token(TipoToken.LESS_EQUAL, lexema, linea);
                        tokens.add(t);

                        estado = 0;
                        lexema = "";
                    }
                    else{
                        //estado = 6;
                        Token t = new Token(TipoToken.LESS, lexema, linea);
                        tokens.add(t);

                        estado = 0;
                        lexema = "";
                        i--;
                    }
                    break;    

                case 7:
                    if(c == '='){
                        //estado = 8;
                        lexema += c;
                        Token t = new Token(TipoToken.EQUAL_EQUAL, lexema, linea);
                        tokens.add(t);

                        estado = 0;
                        lexema = "";
                    }
                    else{
                        //estado = 9;
                        Token t = new Token(TipoToken.EQUAL, lexema, linea);
                        tokens.add(t);

                        estado = 0;
                        lexema = "";
                        i--;
                    }
                    break;
                
                case 10:
                    if(c == '='){
                        //estado = 11;
                        lexema += c;
                        Token t = new Token(TipoToken.BANG_EQUAL, lexema, linea);
                        tokens.add(t);

                        estado = 0;
                        lexema = "";
                    }
                    else{
                        //estado = 12;
                        Token t = new Token(TipoToken.BANG, lexema, linea);
                        tokens.add(t);

                        estado = 0;
                        lexema = "";
                        i--;
                    }
                    break;
                    
                case 13:
                    if(Character.isLetterOrDigit(c)){
                        estado = 13;
                        lexema += c;
                    }
                    else{
                        //estado = 14;
                        TipoToken tt = palabrasReservadas.get(lexema);

                        if(tt == null){
                            Token t = new Token(TipoToken.IDENTIFIER, lexema, linea);
                            tokens.add(t);
                        }
                        else{
                            Token t = new Token(tt, lexema, linea);
                            tokens.add(t);
                        }
                        
                        estado = 0;
                        lexema = "";
                        i--;

                    }
                    break;
                    
                case 15:
                    if(Character.isDigit(c)){
                        estado = 15;
                        lexema += c;
                    }
                    else if(c == '.'){
                        estado = 16;
                        lexema += c;
                    }
                    else if(c == 'E'){
                        estado = 18;
                        lexema += c;
                    }
                    else{
                        //estado = 22;
                        Token t = new Token(TipoToken.NUMBER, lexema, Integer.valueOf(lexema), linea);
                        tokens.add(t);

                        estado = 0;
                        lexema = "";
                        i--;
                    }
                    break;
                
                case 16:
                    if(Character.isDigit(c)){
                        estado = 17;
                        lexema += c;
                    }
                    break;
                
                case 17:
                    if(Character.isDigit(c)){
                        estado = 17;
                        lexema += c;
                    }
                    else if(c == 'E'){
                        estado = 18;
                        lexema += c;
                    }
                    else{
                        //estado = 23;
                        Token t = new Token(TipoToken.NUMBER, lexema, Double.valueOf(lexema), linea);
                        tokens.add(t);

                        estado = 0;
                        lexema = "";
                        i--;
                    }
                    break;   
                    
                case 18:
                    if(Character.isDigit(c)){
                        estado = 20;
                        lexema += c;
                    }
                    else if(c == '+' || c == '-'){
                        estado = 19;
                        lexema += c;
                    }
                    break; 
                    
                case 19:
                    if(Character.isDigit(c)){
                        estado = 20;
                        lexema += c;
                    }
                    break;
                    
                case 20:
                    if(Character.isDigit(c)){
                        estado = 20;
                        lexema += c;
                    }
                    else{
                        //estado = 21;
                        Token t = new Token(TipoToken.NUMBER, lexema, Double.valueOf(lexema), linea); 
                        tokens.add(t);

                        estado = 0;
                        lexema = "";
                        i--;
                    }
                    break;
                    
                case 24:
                    if(c == '"'){
                        //estado = 25;
                        lexema += c;
                        Token t = new Token(TipoToken.STRING, lexema,lexema.substring(1, lexema.length()-1), linea);
                        tokens.add(t);

                        estado = 0;
                        lexema = "";
                    }
                    else if(c == '\n'){
                        Interprete.error(linea-1, "Comillas sin cerrar");
                        System.exit(65);
                    }
                    else if(i == source.length() - 1){
                        Interprete.error(linea, "Comillas sin cerrar");
                        System.exit(65);
                    }
                    else{
                        estado = 24;
                        lexema += c;
                    }
                    break;
                    
                case 26:
                    if(c == '*'){
                        estado = 27;
                        lexema += c;
                    }
                    else if(c == '/'){
                        estado = 30;
                        lexema += c;
                    }
                    else{
                        //estado = 32;
                        Token t = new Token(TipoToken.SLASH, lexema, linea);
                        tokens.add(t);

                        estado = 0;
                        lexema = "";
                        i--;
                    }
                    break;
                    
                case 27:
                    if(c == '*'){
                        estado = 28;
                        lexema += c;
                    }
                    else{
                        estado = 27;
                        lexema += c;
                    }
                    break;
                    
                case 28:
                    if(c == '*'){
                        estado = 28;
                        lexema += c;
                    }
                    else if(c == '/'){
                        estado = 0;
                        lexema = "";
                    }
                    else{
                        estado = 27;
                        lexema += c;
                    }
                    break;
                    
                case 30:
                    if (c == '\n') { 
                       estado = 0;  
                       lexema = "";
                    } 
                    else{  
                        estado = 30; 
                        lexema += c;
                    }
                    break;
            }
        }
        tokens.add(new Token(TipoToken.EOF, "", source.length(), linea));
        
        return tokens;
    }
}