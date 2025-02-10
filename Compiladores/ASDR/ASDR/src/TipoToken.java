public enum TipoToken {

    // Expresiones primarias
    IDENTIFICADOR, NULL, NUMBER, STRING,

    // Palabras reservadas
    FUN, VAR, PRINT, RETURN,

    // Condicionante
    IF, ELSE,

    // Ciclos
    WHILE, FOR,

    // Condiciones logicas
    OR, AND, TRUE, FALSE, NEGADO,

    // Condiciones aritmeticas
    IGUAL, IGUAL_IGUAL, MAYOR, MENOR, MAYOR_IGUAL, MENOR_IGUAL, DIFERENTE,

    // Operaciones aritmeticas
    MENOS, MAS,

    // Bloques
    CORCHETE_IZQUIERDO, CORCHETE_DERECHO, PARENTESIS_IZQUIERDO, PARENTESIS_DERECHO,

    // Caracteres
    COMA, PUNTO, ASTERISCO, DIAGONAL, PUNTO_Y_COMA,

    // Final de cadena
    EOF
}
