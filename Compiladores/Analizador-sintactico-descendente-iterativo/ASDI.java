import java.util.List;
import java.util.Stack;

public class ASDI implements Parser{

    private int i = 0;
    private boolean hayErrores = false;
    private final List<Token> tokens;
    String [][] TablaAS =
    { {"No terminal",     "select",             "from",      "distinct",      "*",       ",",        "id",        ".",       "$" },
      {     "Q",        "select D from T",       "",             "",           "",       "",          "",          "",       ""  },             
      {     "D",             "",                 "",         "distinct P",     "P",      "",          "P",         "",       ""  },
      {     "P",             "",                 "",             "",           "*",      "",          "A",         "",       ""  },  
      {     "A",             "",                 "",             "",           "",       "",        "A2 A1",       "",       ""  }, 
      {     "A1",            "",                 "e",            "",           "",      ", A",         "",         "",       ""  },
      {     "A2",            "",                 "",             "",           "",       "",        "id A3",       "",       ""  },
      {     "A3",            "",                 "e",            "",           "",       "e",          "",       ". id",     ""  },
      {     "T",             "",                 "",             "",           "",       "",        "T2 T1",       "",       ""  },
      {     "T1",            "",                 "",             "",           "",      ", T",         "",         "",       "e" },
      {     "T2",            "",                 "",             "",           "",       "",        "id T3",       "",       ""  },                                                        
      {     "T3",            "",                 "",             "",           "",       "e",        "id",         "",       "e" }
    };

    public ASDI(List<Token> tokens){
        this.tokens = tokens;
    }

    @Override
    public boolean parse() {

        String entrada;
        int j;
        Stack <String> pila = new Stack<>();
        pila.push("$");
        pila.push("T");
        pila.push("from");
        pila.push("D");
        pila.push("select");

        while( !pila.empty() ){
              

            if(tokens.get(i).tipo == TipoToken.IDENTIFICADOR )
                entrada = "id";
            else 
                entrada = tokens.get(i).lexema;


            if (pila.peek().equals(entrada)){
                pila.pop();
                i++;
            }
            else if( esTerminal(pila.peek()) ){
                hayErrores = true;
                break;
            }
            else if( !esTerminal(pila.peek()) &&
                    TablaAS[buscaNoTerminal(TablaAS, pila.peek())][buscaTerminal(TablaAS, entrada)].isEmpty()){
                hayErrores = true;
                break;
            }else{
                String[] producciones = TablaAS[buscaNoTerminal(TablaAS, pila.peek() )][buscaTerminal(TablaAS, entrada )].split(" "); 
                j = producciones.length;
                pila.pop();
                while(j>0){
                    if(!producciones[j-1].equals("e"))
                        pila.push(producciones[j-1]);
                    j--;
                }
            }
        }

        if( pila.empty() && tokens.get(i-1).tipo == TipoToken.EOF && !hayErrores){
            System.out.println("Consulta correcta");
            return  true;
        }else {
            System.out.println("Consulta Incorrecta");
        }
        return false;
    }

    private boolean esTerminal(String tope){
        return tope.equals("id") || tope.equals("select") || tope.equals("from") || tope.equals("distinct") || tope.equals(",") || tope.equals(".") || tope.equals("*");
    }

    private int buscaNoTerminal(String[][] tablaAS, String noTerminal){
        int l= tablaAS.length;
        for(int i=1; i<l; i++){
            if(tablaAS[i][0].equals(noTerminal)){
                return i;
            }
        }
        return -1;
    }

    private int buscaTerminal(String[][] tablaAS, String terminal){
        int l= tablaAS[0].length;
        for(int i=1; i<l; i++){
            if(tablaAS[0][i].equals(terminal)){
                return i;
            }
        }
        return -1;
    }

}


