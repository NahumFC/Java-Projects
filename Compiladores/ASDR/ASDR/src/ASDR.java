import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ASDR implements Parser{

    private int i = 0;
    private boolean hayErrores = false;
    private Token preanalisis;
    private final List<Token> tokens;


    public ASDR(List<Token> tokens){
        this.tokens = tokens;
        preanalisis = this.tokens.get(i);
    }

    @Override
    public boolean parse() {
        List<Statement> statements = program();

        if(preanalisis.tipo == TipoToken.EOF && !hayErrores){
            System.out.println("Consulta correcta");

            if(!statements.isEmpty()){
                TablaSimbolos Tabla = new TablaSimbolos();
                for(Statement sentencias : statements){
                    sentencias.Execute(Tabla);
                }
            }
            return  true;
        }else {
            System.out.println("Se encontraron errores");
        }
        return false;
    }

    private List<Statement> program(){
        List<Statement> statements = new ArrayList<>();
        return declaration(statements);
    }

    public List<Statement> declaration(List<Statement> statements){
        if(hayErrores)
            return null;

        switch (preanalisis.tipo){
            case FUN :
                statements.add(fun_decl());
                declaration(statements);
                break;
            case VAR:
                statements.add(var_decl());
                declaration(statements);
                break;
            case TRUE, STRING, IDENTIFICADOR, FALSE, NULL, NUMBER, PARENTESIS_IZQUIERDO,FOR,IF,PRINT,RETURN,WHILE, CORCHETE_IZQUIERDO:
                statements.add(statement());
                declaration(statements);
                break;
            default:
        }
        return statements;
    }
    private StmtFunction fun_decl(){
        if(hayErrores)
            return null;

        if(preanalisis.tipo == TipoToken.FUN){
            match(TipoToken.FUN);
            return function();
        }
        else{
            hayErrores = true;
            System.out.println("Se esperaba un 'fun' pero se encontró '"+preanalisis.lexema+"'");
            return null;
        }
    }
    private StmtFunction function(){
        if(hayErrores)
            return null;

        if(preanalisis.tipo == TipoToken.IDENTIFICADOR){
            match(TipoToken.IDENTIFICADOR);
            Token id = previous();
            match(TipoToken.PARENTESIS_IZQUIERDO);
            List<Token> params = parameters_opc();
            match(TipoToken.PARENTESIS_DERECHO);
            StmtBlock body = block();

            return new StmtFunction(id,params,body);
        }
        else{
            hayErrores = true;
            System.out.println("Se esperaba un 'identificador' pero se encontró '"+preanalisis.lexema+"'");
            return null;
        }
    }

    private List<Token> parameters_opc(){
        if(hayErrores)
            return null;

        List<Token> parametros= new ArrayList<>();

        if(preanalisis.tipo == TipoToken.IDENTIFICADOR)
            parametros=parameters(parametros);

        return parametros;
    }

    private List<Token> parameters(List<Token> parametros){
        if(hayErrores)
            return null;

        if(preanalisis.tipo == TipoToken.IDENTIFICADOR){
            match(TipoToken.IDENTIFICADOR);
            Token id = previous();
            parametros.add(id);
            return parameters_2(parametros);
        }
        else{
            hayErrores = true;
            System.out.println("Se esperaba un 'identificador' pero se encontró '"+preanalisis.lexema+"'");
            return null;
        }
    }

    private List<Token> parameters_2(List<Token> parametros){
        if(hayErrores)
            return null;

        if(preanalisis.tipo == TipoToken.COMA){
            match(TipoToken.COMA);
            match(TipoToken.IDENTIFICADOR);
            Token id = previous();
            parametros.add(id);
            return parameters_2(parametros);
        }
        return parametros;
    }

    private StmtBlock block(){
        if(hayErrores)
            return null;

        if(preanalisis.tipo == TipoToken.CORCHETE_IZQUIERDO){
            match(TipoToken.CORCHETE_IZQUIERDO);
            List<Statement> declaraciones = new ArrayList<>();
            List<Statement> declar = declaration(declaraciones);
            match(TipoToken.CORCHETE_DERECHO);
            return new StmtBlock(declar);
        }
        else{
            hayErrores = true;
            System.out.println("Se esperaba un '{' pero se encontró '"+preanalisis.lexema+"'");
            return null;
        }
    }

    private StmtVar var_decl(){
        if(preanalisis.tipo == TipoToken.VAR){
            match(TipoToken.VAR);
            match(TipoToken.IDENTIFICADOR);
            Token id = previous();
            Expression expr = var_init();
            match(TipoToken.PUNTO_Y_COMA);
            return new StmtVar(id,expr);
        }
        else{
            hayErrores = true;
            System.out.println("Se esperaba un 'var' pero se encontró '"+preanalisis.lexema+"'");
            return null;
        }
    }
    private Expression var_init(){
        if(hayErrores)
            return null;
        if(preanalisis.tipo == TipoToken.IGUAL){
            match(TipoToken.IGUAL);
            return expression();
        }
        return null;
    }
    private Expression expression(){
        return assignment();
    }
    private Expression assignment(){
        Expression expr = logic_or();
        expr = assignment_opc(expr);
        return  expr;
    }
    private Expression logic_or(){
        Expression expr = logic_and();
        expr = logic_or_2(expr);
        return expr;
    }
    private Expression logic_and(){
        Expression expr = equality();
        expr = logic_and_2(expr);
        return expr;
    }
    private Expression equality(){
        Expression expr = comparison();
        expr = equality_2(expr);
        return expr;
    }
    private Expression comparison(){
        Expression expr = term();
        expr = comparison_2(expr);
        return expr;
    }
    private Expression term(){
        Expression expr = factor();
        expr = term_2(expr);
        return expr;
    }
    private Expression factor(){
        Expression expr =unary();
        expr=factor_2(expr);
        return expr;
    }
    private Expression unary(){
        if(preanalisis.tipo == TipoToken.NEGADO){
            match(TipoToken.NEGADO);
            Token operador = previous();
            Expression expr = unary();
            return new ExprUnary(operador,expr);
        }
        else if(preanalisis.tipo == TipoToken.MENOS){
            match(TipoToken.MENOS);
            Token operador = previous();
            Expression expr = unary();
            return new ExprUnary(operador,expr);
        }
        else{
            return call();
        }
    }
    private Expression call(){
        Expression expr = primary();
        expr = call_2(expr);
        return expr;
    }
    private Expression primary(){
        if(hayErrores)
            return null;

        switch (preanalisis.tipo){
            case TRUE :
                match(TipoToken.TRUE);
                return new ExprLiteral(true);
            case FALSE:
                match(TipoToken.FALSE);
                return new ExprLiteral(false);
            case NULL:
                return new ExprLiteral(null);
            case NUMBER:
                match(TipoToken.NUMBER);
                Token numero = previous();
                return new ExprLiteral(Double.parseDouble(numero.lexema));
            case STRING:
                match(TipoToken.STRING);
                Token cadena = previous();
                return new ExprLiteral(cadena.lexema);
            case IDENTIFICADOR:
                match(TipoToken.IDENTIFICADOR);
                Token id = previous();
                return new ExprVariable(id);
            case PARENTESIS_IZQUIERDO:
                match(TipoToken.PARENTESIS_IZQUIERDO);
                Expression expr = expression();
                match(TipoToken.PARENTESIS_DERECHO);
                return new ExprGrouping(expr);
            default:
                hayErrores=true;
                System.out.println("Se esperaba un 'true', 'false', 'null', 'number', 'string', 'identificador' o '(' pero se encontró '"+preanalisis.lexema+"'");
                return null;
        }
    }

    private Expression call_2(Expression expr){
        if(preanalisis.tipo == TipoToken.PARENTESIS_IZQUIERDO){
            match(TipoToken.PARENTESIS_IZQUIERDO);
            List<Expression> lstArguments = arguments_opc();
            match(TipoToken.PARENTESIS_DERECHO);
            ExprCallFunction ecf = new ExprCallFunction(expr,lstArguments);
            return call_2(ecf);
        }
        return expr;
    }
    private List<Expression> arguments_opc(){
        List<Expression> expresiones = new ArrayList<>();

        if (primitiva()){
            expresiones.add(expression());
            return arguments(expresiones);
        }
        return expresiones;
    }
    private List<Expression> arguments(List<Expression> expresiones){
        if(preanalisis.tipo == TipoToken.COMA){
            match(TipoToken.COMA);
            expresiones.add(expression());
            return arguments(expresiones);
        }
        return expresiones;
    }
    private Expression factor_2(Expression expr){

        if(preanalisis.tipo == TipoToken.DIAGONAL){
            match(TipoToken.DIAGONAL);
            Token operador = previous();
            Expression expr2=unary();
            ExprBinary expb = new ExprBinary(expr,operador,expr2);
            return factor_2(expb);
        }
        else if(preanalisis.tipo == TipoToken.ASTERISCO){
            match(TipoToken.ASTERISCO);
            Token operador = previous();
            Expression expr2 = unary();
            ExprBinary expb = new ExprBinary(expr,operador,expr2);
            return factor_2(expb);
        }
        return expr;
    }
    private Expression term_2(Expression expr){
        if(hayErrores)
            return null;

        if(preanalisis.tipo == TipoToken.MENOS){
            match(TipoToken.MENOS);
            Token operador = previous();
            Expression expr2 = unary();
            ExprBinary expb = new ExprBinary(expr,operador,expr2);
            return term_2(expb);
        }
        else if(preanalisis.tipo == TipoToken.MAS){
            match(TipoToken.MAS);
            Token operador = previous();
            Expression expr2 = unary();
            ExprBinary expb = new ExprBinary(expr,operador,expr2);
            return term_2(expb);
        }
        return expr;
    }
    private Expression comparison_2(Expression expr){
        if(hayErrores)
            return null;

        switch (preanalisis.tipo){
            case MAYOR :
                match(TipoToken.MAYOR);
                Token opmayor = previous();
                Expression expr2 = term();
                ExprLogical exprl = new ExprLogical(expr,opmayor,expr2);
                return comparison_2(exprl);
            case MAYOR_IGUAL:
                match(TipoToken.MAYOR_IGUAL);
                Token opmayorigual = previous();
                Expression exp2 = term();
                ExprLogical exprlo = new ExprLogical(expr,opmayorigual,exp2);
                return comparison_2(exprlo);
            case MENOR:
                match(TipoToken.MENOR);
                Token opmenor = previous();
                Expression ex2 = term();
                ExprLogical exprlog = new ExprLogical(expr,opmenor,ex2);
                return comparison_2(exprlog);
            case MENOR_IGUAL:
                match(TipoToken.MENOR_IGUAL);
                Token opmenorigual = previous();
                Expression e2 = term();
                ExprLogical exprlogi = new ExprLogical(expr,opmenorigual,e2);
                return comparison_2(exprlogi);
            default:
        }
        return expr;
    }
    private Expression equality_2(Expression expr){
        if(hayErrores)
            return null;

        if(preanalisis.tipo == TipoToken.DIFERENTE){
            match(TipoToken.DIFERENTE);
            Token operador = previous();
            Expression expr2 = comparison();
            ExprLogical exprl = new ExprLogical(expr,operador,expr2);
            return equality_2(exprl);
        }
        else if(preanalisis.tipo == TipoToken.IGUAL_IGUAL){
            match(TipoToken.IGUAL_IGUAL);
            Token operador = previous();
            Expression expr2 = comparison();
            ExprLogical exprl = new ExprLogical(expr,operador,expr2);
            return equality_2(exprl);
        }
        return expr;
    }
    private Expression logic_and_2(Expression expr){
        if(hayErrores)
            return null;

        if(preanalisis.tipo == TipoToken.AND){
            match(TipoToken.AND);
            Token operador = previous();
            Expression expr2 = equality();
            ExprLogical exprl = new ExprLogical(expr,operador,expr2);
            return logic_and_2(exprl);
        }
        return expr;
    }
    private Expression logic_or_2(Expression expr){
        if(hayErrores)
            return null;

        if(preanalisis.tipo == TipoToken.OR){
            match(TipoToken.OR);
            Token operador = previous();
            Expression expr2 = logic_and();
            ExprLogical exprl = new ExprLogical(expr,operador,expr2);
            return logic_or_2(exprl);
        }
        return expr;
    }
    private Expression assignment_opc(Expression expr){
        if(hayErrores)
            return null;

        if(preanalisis.tipo == TipoToken.IGUAL){
            match(TipoToken.IGUAL);
            i=i-1;
            Token operador = previous();
            i=i+1;
            Expression expr2= expression();
            return new ExprAssign(operador,expr2);
        }
        return expr;
    }


    private Statement statement(){
        if(hayErrores)
            return null;

        switch (preanalisis.tipo) {
            case TRUE, STRING, IDENTIFICADOR, FALSE, NULL, NUMBER:
                return expr_stmt();
            case FOR:
                return for_stmt();
            case IF:
                return if_stmt();
            case PRINT:
                return print_stmt();
            case RETURN:
                return return_stmt();
            case WHILE:
                return while_stmt();
            case CORCHETE_IZQUIERDO:
                return block();
            default:
                hayErrores = true;
                System.out.println("Se esperaba un '' pero se encontró '"+preanalisis.lexema+"'");
                return null;
        }
    }
    private StmtExpression expr_stmt(){
        if(hayErrores)
            return null;

        Expression expr = expression();

        if(preanalisis.tipo==TipoToken.PUNTO_Y_COMA) {
            match(TipoToken.PUNTO_Y_COMA);
            return new StmtExpression(expr);
        }
        else{
            hayErrores = true;
            System.out.println("Se esperaba un ';' pero se encontró '"+preanalisis.lexema+"'");
            return null;
        }
    }
    private Statement for_stmt(){
        if(hayErrores)
            return null;

        if(preanalisis.tipo==TipoToken.FOR) {
            match(TipoToken.FOR);
            match(TipoToken.PARENTESIS_IZQUIERDO);
            Statement declaracion = for_stmt_1();
            Expression condicion = for_stmt_2();
            Expression aumento  = for_stmt_3();
            match(TipoToken.PARENTESIS_DERECHO);
            Statement body = statement();
            if(aumento!=null){
                body = new StmtBlock(Arrays.asList(body,new StmtExpression(aumento)));
            }
            body = new StmtLoop(condicion,body);
            if(declaracion!=null){
                body = new StmtBlock(Arrays.asList(declaracion,body));
            }
            return body;
        }
        else{
            hayErrores = true;
            System.out.println("Se esperaba un 'for' pero se encontró '"+preanalisis.lexema+"'");
            return null;
        }
    }
    private Statement for_stmt_1(){
        if(hayErrores)
            return null;

        switch (preanalisis.tipo){
            case VAR:
                return var_decl();
            case TRUE, STRING, IDENTIFICADOR, FALSE, NULL, NUMBER, PARENTESIS_IZQUIERDO:
                return expr_stmt();
            case PUNTO_Y_COMA:
                match(TipoToken.PUNTO_Y_COMA);
                return new StmtExpression(null);
            default:
                hayErrores=true;
                System.out.println("Se esperaba un 'var', true', 'false', 'null', 'number', 'string', 'identificador', '(' o ';' pero se encontró '"+preanalisis.lexema+"'");
                return null;
        }
    }
    private Expression for_stmt_2(){
        if(hayErrores)
            return null;

        if(primitiva()){
            Expression expr= expression();
            match(TipoToken.PUNTO_Y_COMA);
            return expr;
        }
        else if(preanalisis.tipo == TipoToken.PUNTO_Y_COMA){
            match(TipoToken.PUNTO_Y_COMA);
            return new ExprLiteral(true);
        }
        else{
            hayErrores=true;
            System.out.println("Se esperaba un 'true', 'false', 'null', 'number', 'string', 'identificador', '(' o ';' pero se encontró '"+preanalisis.lexema+"'");
            return null;
        }

    }
    private Expression for_stmt_3(){
        if(primitiva()){
            return expression();
        }
        return null;
    }
    private StmtIf if_stmt(){
        if(hayErrores)
            return null;

        if(preanalisis.tipo == TipoToken.IF){
            match(TipoToken.IF);
            match(TipoToken.PARENTESIS_IZQUIERDO);
            Expression expr = expression();
            match(TipoToken.PARENTESIS_DERECHO);
            ExprGrouping exprg = new ExprGrouping(expr);
            Statement stmt = statement();
            Statement stmt_else = else_statement();
            return new StmtIf(exprg,stmt,stmt_else);
        }
        else{
            hayErrores=true;
            System.out.println("Se esperaba un 'if' pero se encontró '"+preanalisis.lexema+"'");
            return null;
        }
    }
    private Statement else_statement(){
        if(hayErrores)
            return null;

        if(preanalisis.tipo == TipoToken.ELSE){
            match(TipoToken.ELSE);
            return statement();
        }
        return null;
    }
    private StmtPrint print_stmt(){
        if(hayErrores)
            return null;

        if(preanalisis.tipo == TipoToken.PRINT){
            match(TipoToken.PRINT);
            Expression expr = expression();
            match(TipoToken.PUNTO_Y_COMA);
            return new StmtPrint(expr) ;
        }
        else{
            hayErrores=true;
            System.out.println("Se esperaba un 'print' pero se encontró '"+preanalisis.lexema+"'");
            return null;
        }
    }
    private StmtReturn return_stmt(){
        if(hayErrores)
            return null;

        if(preanalisis.tipo == TipoToken.RETURN){
            match(TipoToken.RETURN);
            Expression expr = return_exp_opc();
            match(TipoToken.PUNTO_Y_COMA);
            return new StmtReturn(expr);
        }
        else{
            hayErrores=true;
            System.out.println("Se esperaba un 'return' pero se encontró '"+preanalisis.lexema+"'");
            return null;
        }
    }
    private Expression return_exp_opc(){
        if(hayErrores)
            return null;

        if(primitiva()){
            return expression();
        }
        return null;
    }
    private StmtLoop while_stmt(){
        if(hayErrores)
            return null;

        if(preanalisis.tipo == TipoToken.WHILE){
            match(TipoToken.WHILE);
            match(TipoToken.PARENTESIS_IZQUIERDO);
            Expression expr = expression();
            match(TipoToken.PARENTESIS_DERECHO);
            Statement body = statement();
            return new StmtLoop(expr,body);
        }
        else{
            hayErrores=true;
            System.out.println("Se esperaba un 'return' pero se encontró '"+preanalisis.lexema+"'");
            return null;
        }
    }

    private void match(TipoToken tt){
        if(preanalisis.tipo == tt){
            i++;
            preanalisis = tokens.get(i);
        }
        else{
            hayErrores = true;
            System.out.println("Se esperaba un '"+tt.toString().toLowerCase()+"' pero se encontró '"+preanalisis.lexema+"'");
        }

    }
    private boolean primitiva(){
        return switch (preanalisis.tipo) {
            case TRUE, STRING, IDENTIFICADOR, FALSE, NULL, NUMBER, PARENTESIS_IZQUIERDO -> true;
            default -> false;
        };
    }

    private Token previous() {
        return this.tokens.get(i - 1);
    }

}
