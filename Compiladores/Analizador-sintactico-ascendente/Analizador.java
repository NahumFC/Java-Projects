package Analizador;

import java.util.List;
import java.util.Stack;

public class Analizador {

    private int Elemento_token=0;
    private final List<Token> tokens;

    private final String[][] Tabla =
            {{"Estado","*", "," , "." , "id" ,"select","distinct","from", "$" , "Q" , "D" , "P" , "A" , "A1" , "A2" , "A3" , "T" , "T1" , "T2" , "T3" },
             {  "0",   "" ,  "" , ""  ,  ""  ,  "s2"  ,    ""    ,  ""  ,  "" , "1" , ""  , ""  , ""  ,  ""  ,  ""  ,  ""  , ""  ,  ""  ,  ""  ,  ""  },
             {  "1",   "" ,  "" , ""  ,  ""  ,   ""   ,    ""    ,  ""  ,"acc", ""  , ""  , ""  , ""  ,  ""  ,  ""  ,  ""  , ""  ,  ""  ,  ""  ,  ""  },
             {  "2",  "s6",  "" , ""  , "s9" ,   ""   ,   "s4"   ,  ""  ,  "" , ""  , "3" , "5" , "7" ,  ""  ,  "8" ,  ""  , ""  ,  ""  ,  ""  ,  ""  },
             {  "3",   "" ,  "" , ""  ,  ""  ,   ""   ,    ""    , "s10",  "" , ""  , ""  , ""  , ""  ,  ""  ,  ""  ,  ""  , ""  ,  ""  ,  ""  ,  ""  },
             {  "4",  "s6",  "" , ""  , "s9" ,   ""   ,    ""    ,  ""  ,  "" , ""  , ""  ,"11" , "7" ,  ""  ,  "8" ,  ""  , ""  ,  ""  ,  ""  ,  ""  },
             {  "5",   "" ,  "" , ""  ,  ""  ,   ""   ,    ""    , "r2" ,  "" , ""  , ""  , ""  , ""  ,  ""  ,  ""  ,  ""  , ""  ,  ""  ,  ""  ,  ""  },
             {  "6",   "" ,  "" , ""  ,  ""  ,   ""   ,    ""    , "r3" ,  "" , ""  , ""  , ""  , ""  ,  ""  ,  ""  ,  ""  , ""  ,  ""  ,  ""  ,  ""  },
             {  "7",   "" ,  "" , ""  ,  ""  ,   ""   ,    ""    , "r4" ,  "" , ""  , ""  , ""  , ""  ,  ""  ,  ""  ,  ""  , ""  ,  ""  ,  ""  ,  ""  },
             {  "8",   "" ,"s13", ""  ,  ""  ,   ""   ,    ""    , "r7" ,  "" , ""  , ""  , ""  , ""  , "12" ,  ""  ,  ""  , ""  ,  ""  ,  ""  ,  ""  },
             {  "9",   "" ,"r10","s15",  ""  ,   ""   ,    ""    , "r10",  "" , ""  , ""  , ""  , ""  ,  ""  ,  ""  , "14" , ""  ,  ""  ,  ""  ,  ""  },
             {  "10",  "" ,  "" , ""  ,"s18" ,   ""   ,    ""    ,  ""  ,  "" , ""  , ""  , ""  , ""  ,  ""  ,  ""  ,  ""  ,"16" ,  ""  , "17" ,  ""  },
             {  "11",  "" ,  "" , ""  ,  ""  ,   ""   ,    ""    , "r1" ,  "" , ""  , ""  , ""  , ""  ,  ""  ,  ""  ,  ""  , ""  ,  ""  ,  ""  ,  ""  },
             {  "12",  "" ,  "" , ""  ,  ""  ,   ""   ,    ""    , "r5" ,  "" , ""  , ""  , ""  , ""  ,  ""  ,  ""  ,  ""  , ""  ,  ""  ,  ""  ,  ""  },
             {  "13",  "" ,  "" , ""  , "s9" ,   ""   ,    ""    ,  ""  ,  "" , ""  , ""  , ""  ,"19" ,  ""  ,  "8" ,  ""  , ""  ,  ""  ,  ""  ,  ""  },
             {  "14",  "" , "r8", ""  ,  ""  ,   ""   ,    ""    , "r8" ,  "" , ""  , ""  , ""  , ""  ,  ""  ,  ""  ,  ""  , ""  ,  ""  ,  ""  ,  ""  },
             {  "15",  "" ,  "" , ""  ,"s20" ,   ""   ,    ""    ,  ""  ,  "" , ""  , ""  , ""  , ""  ,  ""  ,  ""  ,  ""  , ""  ,  ""  ,  ""  ,  ""  },
             {  "16",  "" ,  "" , ""  ,  ""  ,   ""   ,    ""    ,  ""  , "r0", ""  , ""  , ""  , ""  ,  ""  ,  ""  ,  ""  , ""  ,  ""  ,  ""  ,  ""  },
             {  "17",  "" ,"s22", ""  ,  ""  ,   ""   ,    ""    ,  ""  ,"r13", ""  , ""  , ""  , ""  ,  ""  ,  ""  ,  ""  , ""  , "21" ,  ""  ,  ""  },
             {  "18",  "" ,"r16", ""  ,"s24" ,   ""   ,    ""    ,  ""  ,"r16", ""  , ""  , ""  , ""  ,  ""  ,  ""  ,  ""  , ""  ,  ""  ,  ""  , "23" },
             {  "19",  "" ,  "" , ""  ,  ""  ,   ""   ,    ""    , "r6" ,  "" , ""  , ""  , ""  , ""  ,  ""  ,  ""  ,  ""  , ""  ,  ""  ,  ""  ,  ""  },
             {  "20",  "" , "r9", ""  ,  ""  ,   ""   ,    ""    , "r9" ,  "" , ""  , ""  , ""  , ""  ,  ""  ,  ""  ,  ""  , ""  ,  ""  ,  ""  ,  ""  },
             {  "21",  "" ,  "" , ""  ,  ""  ,   ""   ,    ""    ,  ""  ,"r11", ""  , ""  , ""  , ""  ,  ""  ,  ""  ,  ""  , ""  ,  ""  ,  ""  ,  ""  },
             {  "22",  "" ,  "" , ""  ,"s18" ,   ""   ,    ""    ,  ""  ,  "" , ""  , ""  , ""  , ""  ,  ""  ,  ""  ,  ""  ,"25" ,  ""  , "17" ,  ""  },
             {  "23",  "" ,"r14", ""  ,  ""  ,   ""   ,    ""    ,  ""  ,"r14", ""  , ""  , ""  , ""  ,  ""  ,  ""  ,  ""  , ""  ,  ""  ,  ""  ,  ""  },
             {  "24",  "" ,"r15", ""  ,  ""  ,   ""   ,    ""    ,  ""  ,"r15", ""  , ""  , ""  , ""  ,  ""  ,  ""  ,  ""  , ""  ,  ""  ,  ""  ,  ""  },
             {  "25",  "" ,  "" , ""  ,  ""  ,   ""   ,    ""    ,  ""  ,"r12", ""  , ""  , ""  , ""  ,  ""  ,  ""  ,  ""  , ""  ,  ""  ,  ""  ,  ""  } };

    private final int[][] Simbolos_Reducir ={{9,4},{10,2},{10,1},{11,1},{11,1},{12,2},{13,2},{13,0},{14,2},{15,2},{15,0},{16,2},{17,2},{17,0},{18,2},{19,1},{19,0}};
    public Analizador(List<Token> tokens){
        this.tokens=tokens;
    }

    public String Comportamiento(){

        Stack <String> Pila = new Stack<>();
        String simbolo=Simbolo();
        Pila.push("0");

        while(true) {
            String accion=Accion(Pila.peek(),simbolo);
            if(!accion.isEmpty() &&  accion.charAt(0)=='s'){
                Pila.push(accion.substring(1));
                simbolo=Simbolo();

            }
            else if(!accion.isEmpty() && accion.charAt(0)=='r'){
                for(int i=0;i<Simbolos_Reducir[Integer.parseInt(accion.substring(1))][1];i++){
                    Pila.pop();
                }
                Pila.push(Ir_A(Pila.peek(),Simbolos_Reducir[Integer.parseInt(accion.substring(1))][0]));
            }
            else if (accion.equals("acc")){
                return "[ Sintaxis correcta ]";
            }
            else{
                return "[ Sintaxis incorrecta ]";
            }
        }
    }

    private String Simbolo(){
        String Simbolo_Entrada = (tokens.get(Elemento_token).tipo == TipoToken.IDENTIFICADOR) ? "id" : tokens.get(Elemento_token).lexema;
        Elemento_token++;
        return Simbolo_Entrada;
    }

    private String Accion (String estado, String simbolo){
        int fila=-1, columna=-1;

        for(int i=0;i<27;i++){
            if(Tabla[i][0].equals(estado)){
                columna=i;
                break;
            }
        }

        for(int i=0;i<20;i++){
            if(Tabla[0][i].equals(simbolo)){
                fila=i;
                break;
            }
        }
        return Tabla[columna][fila];
    }

    private String Ir_A (String estado, int simbolo){
        int columna=-1;

        for(int i=0;i<27;i++){
            if(Tabla[i][0].equals(estado)){
                columna=i;
                break;
            }
        }
        return Tabla[columna][simbolo];
    }

}