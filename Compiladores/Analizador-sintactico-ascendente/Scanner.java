package Analizador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static Analizador.Interprete.error;
public class Scanner {

    private static final Map<String, TipoToken> palabrasReservadas;

    static {
        palabrasReservadas = new HashMap<>();
        palabrasReservadas.put("select",    TipoToken.SELECT);
        palabrasReservadas.put("from",   TipoToken.FROM);
        palabrasReservadas.put("distinct",  TipoToken.DISTINCT);
    }

    private final String source;

    private final List<Token> tokens = new ArrayList<>();
    
    public Scanner(String source){
        this.source = source + " ";
    }

    public List<Token> scan() throws Exception {
        String lexema = "";
        int estado = 0;
        int linea = 0;
        char c;

        for(int i=0; i<source.length(); i++){
            c = source.charAt(i);

            if (c == '\n'){
                linea ++;}

            switch (estado){
                case 0:
                    if(c == '*') {
                        lexema += c;
                        tokens.add(new Token(TipoToken.ASTERISCO, lexema));
                        estado = 0;
                        lexema = "";
                    }
                    else if(c == ','){
                        lexema += c;
                        //estado = 34;
                        tokens.add(new Token(TipoToken.COMA, lexema));
                        estado = 0;
                        lexema = "";
                    }

                    else if(c == '.'){
                        lexema += c;
                        //estado = 35;
                        tokens.add(new Token(TipoToken.PUNTO, lexema));
                        estado = 0;
                        lexema = "";
                    }
                    else if(Character.isLetter(c)){
                        estado = 1;
                        lexema += c;
                    }
                    else if(!Character.isWhitespace(c)){
                        error(linea+1, "Caracter desconocido "+"'"+c+"'");
                    }
                    break;

                case 1:
                    if(Character.isLetterOrDigit(c)){
                        estado = 1;
                        lexema += c;
                    }
                    else{
                        //estado = 14;
                        TipoToken tt = palabrasReservadas.get(lexema);

                        if(tt == null){
                            tokens.add(new Token(TipoToken.IDENTIFICADOR, lexema));
                        }
                        else{
                            tokens.add(new Token(tt, lexema));
                        }
                        estado = 0;
                        lexema = "";
                        i--;
                    }
                    break;

            }
        }tokens.add(new Token(TipoToken.EOF, "$"));
        return tokens;
    }
}
