package interprete;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ASDR implements Parser{

    private int i = 0;
    private boolean hayErrores = false;
    private Token preanalisis;
    private final List<Token> tokens;
    private List<Statement> statements;

    public ASDR(List<Token> tokens){
        this.tokens = tokens;
        preanalisis = this.tokens.get(i);
        statements = new ArrayList<>();
    }

    @Override
    public boolean parse() {
        PROGRAM();
        if(preanalisis.tipo == TipoToken.EOF && !hayErrores){
            System.out.println("NO SE ENCONTRARON ERRORES ");
            return true;
        }
        else {
            System.out.println("SE ENCONTRARON ERRORES");
        }
        return false;
    }
    public List<Statement> getStatements() {
        return statements;
    }
    //PROGRAM -> DECLARATION
    public List<Statement> PROGRAM() {
        if (hayErrores)
            return null;
        if(preanalisis.tipo != TipoToken.EOF){
            DECLARATION(statements);
            return statements;
        }
        else{
            hayErrores = true;
            Interprete.error(preanalisis.linea, "Se esperaba 'FUN' or 'VAR' or 'BANG' or 'MINUS' or 'TRUE' or 'FALSE' or 'NULL' "
                    + "or 'NUMBER' or 'STRING' or 'IDENTIFIER' or 'LEFT_PAREN' or 'FOR' or 'IF' or 'PRINT' or 'RETURN' or 'WHILE'"
                    + "or 'LEFT_BRACE' pero se encontro '" + preanalisis.tipo + "'");
            return null;
        }
    }


    private void DECLARATION(List<Statement> statements) {
        if(hayErrores)
            return;
        if(preanalisis.tipo == TipoToken.FUN){
            Statement fundecl = FUN_DECL();
            statements.add(fundecl);
            DECLARATION(statements);
        }
        else if(preanalisis.tipo == TipoToken.VAR){
            Statement vardecl = VAR_DECL();
            statements.add(vardecl);
            DECLARATION(statements);
        }
        else if(preanalisis.tipo == TipoToken.BANG || preanalisis.tipo == TipoToken.MINUS || preanalisis.tipo == TipoToken.FALSE || preanalisis.tipo == TipoToken.TRUE|| preanalisis.tipo == TipoToken.NULL
                || preanalisis.tipo == TipoToken.NUMBER || preanalisis.tipo == TipoToken.STRING || preanalisis.tipo == TipoToken.IDENTIFIER || preanalisis.tipo == TipoToken.LEFT_PAREN || preanalisis.tipo ==TipoToken.PRINT
                || preanalisis.tipo == TipoToken.FOR || preanalisis.tipo == TipoToken.IF || preanalisis.tipo == TipoToken.RETURN || preanalisis.tipo == TipoToken.WHILE || preanalisis.tipo == TipoToken.LEFT_BRACE){
            Statement statement = STATEMENT();
            statements.add(statement);
            DECLARATION(statements);
        }
    }

    private Statement FUN_DECL() {
        if(hayErrores)
            return null;
        if(preanalisis.tipo == TipoToken.FUN){
            match(TipoToken.FUN);
            Statement stmtfun = FUNCTION();
            return stmtfun;
        }
        else{
            hayErrores = true;
            Interprete.error(preanalisis.linea, "Se esperaba 'FUN' pero se encontro '" + preanalisis.tipo + "'");
            return null;
        }
    }

    private Statement VAR_DECL() {
        if(hayErrores)
            return null;
        if(preanalisis.tipo == TipoToken.VAR){
            match(TipoToken.VAR);
            match(TipoToken.IDENTIFIER);
            Token name = previous();
            Expression initializer = VAR_INIT();
            match(TipoToken.SEMICOLON);
            return new StmtVar(name, initializer);
        }
        else{
            hayErrores = true;
            Interprete.error(preanalisis.linea, "Se esperaba 'VAR' pero se encontro '" + preanalisis.tipo + "'");
        }
        return null;
    }

    private Expression VAR_INIT() {
        if(hayErrores)
            return null;
        if(preanalisis.tipo == TipoToken.EQUAL){
            match(TipoToken.EQUAL);
            Expression initializer = EXPRESSION();
            return initializer;
        }
        return null;
    }
    

    private Statement STATEMENT() {
        if(hayErrores)
            return null;   
        if(preanalisis.tipo == TipoToken.BANG || preanalisis.tipo == TipoToken.MINUS || preanalisis.tipo == TipoToken.FALSE || preanalisis.tipo == TipoToken.TRUE|| preanalisis.tipo == TipoToken.NULL
                || preanalisis.tipo == TipoToken.NUMBER || preanalisis.tipo == TipoToken.STRING || preanalisis.tipo == TipoToken.IDENTIFIER || preanalisis.tipo == TipoToken.LEFT_PAREN){
           return EXPR_STMT();
        }
        else if (preanalisis.tipo == TipoToken.FOR){
            return FOR_STMT();
        }
        else if(preanalisis.tipo == TipoToken.IF){
            return IF_STMT();
        }
        else if (preanalisis.tipo == TipoToken.PRINT){
            return PRINT_STMT();
        }
        else if(preanalisis.tipo == TipoToken.RETURN){
            return RETURN_STMT();
        }
        else if (preanalisis.tipo == TipoToken.WHILE){
            return WHILE_STMT();
        }
        else if (preanalisis.tipo == TipoToken.LEFT_BRACE){
           return  BLOCK();
        }
        else{
            hayErrores = true;
            Interprete.error(preanalisis.linea, "Se esperaba 'BANG' or 'MINUS' or 'TRUE' or 'FALSE' or 'NULL' or 'NUMBER' or 'STRING' or 'IDENTIFIER' or "
                    + "'LEFT_PAREN' or 'FOR' or 'IF' or 'PRINT' or 'RETURN' or 'WHILE' or 'LEFT_BRACE' pero se encontro '" + preanalisis.tipo + "'");
            return null;
        }
    }

    private Statement EXPR_STMT() {
        if(hayErrores)
            return null;
        if(preanalisis.tipo == TipoToken.BANG || preanalisis.tipo == TipoToken.MINUS || preanalisis.tipo == TipoToken.FALSE || preanalisis.tipo == TipoToken.TRUE|| preanalisis.tipo == TipoToken.NULL
                || preanalisis.tipo == TipoToken.NUMBER || preanalisis.tipo == TipoToken.STRING || preanalisis.tipo == TipoToken.IDENTIFIER || preanalisis.tipo == TipoToken.LEFT_PAREN){
            Expression expr = EXPRESSION();
            match(TipoToken.SEMICOLON);
            return new StmtExpression(expr);
        }
        else{
            hayErrores = true;
            Interprete.error(preanalisis.linea, "Se esperaba 'BANG' or 'MINUS' or 'TRUE' or 'FALSE' or 'NULL' or 'NUMBER' or 'STRING' or 'IDENTIFIER' or 'LEFT_PAREN' pero se encontro '" + preanalisis.tipo + "'");
            return null;
        }
    }

    private Statement FOR_STMT() {
        if(hayErrores)
            return  null;
        if(preanalisis.tipo == TipoToken.FOR){
            match(TipoToken.FOR);
            match(TipoToken.LEFT_PAREN);
            Statement initializer = FOR_STMT_1();
            Expression condition = FOR_STMT_2();
            Expression increment = FOR_STMT_3();
            match(TipoToken.RIGHT_PAREN);
            Statement body = STATEMENT();
            if (increment != null)
                body = new StmtBlock(Arrays.asList(body, new StmtExpression(increment)));
            if (condition == null)
                condition = new ExprLiteral(true);
            body = new StmtLoop(condition, body);
            if (initializer != null)
                body = new StmtBlock(Arrays.asList(initializer, body));
            return body;
        }
        else{
            hayErrores = true;
            Interprete.error(preanalisis.linea, "Se esperaba 'FOR' pero se encontro '" + preanalisis.tipo + "'");
            return null;
        }
    }

    private Statement FOR_STMT_1() {
        if(hayErrores)
            return null;
        if(preanalisis.tipo == TipoToken.VAR){
            return VAR_DECL();
        }
        else if (preanalisis.tipo == TipoToken.BANG || preanalisis.tipo == TipoToken.MINUS || preanalisis.tipo == TipoToken.FALSE || preanalisis.tipo == TipoToken.TRUE|| preanalisis.tipo == TipoToken.NULL
                || preanalisis.tipo == TipoToken.NUMBER || preanalisis.tipo == TipoToken.STRING || preanalisis.tipo == TipoToken.IDENTIFIER || preanalisis.tipo == TipoToken.LEFT_PAREN){
            Statement expr = EXPR_STMT();
            return expr;
        }
        else if(preanalisis.tipo == TipoToken.SEMICOLON){
            match(TipoToken.SEMICOLON);
            return null;
        }
        else{
            hayErrores = true;
            Interprete.error(preanalisis.linea, "Se esperaba 'VAR' or 'BANG' or 'MINUS' or 'TRUE' or 'FALSE' or 'NULL' or 'NUMBER' or 'STRING' or 'IDENTIFIER' or 'LEFT_PAREN' or 'SEMICOLON' pero se encontro '" + preanalisis.tipo + "'");
            return null;
        }
    }

    private Expression FOR_STMT_2() {
        if(hayErrores)
            return null;
        if(preanalisis.tipo == TipoToken.BANG || preanalisis.tipo == TipoToken.MINUS || preanalisis.tipo == TipoToken.FALSE || preanalisis.tipo == TipoToken.TRUE|| preanalisis.tipo == TipoToken.NULL
                || preanalisis.tipo == TipoToken.NUMBER || preanalisis.tipo == TipoToken.STRING || preanalisis.tipo == TipoToken.IDENTIFIER || preanalisis.tipo == TipoToken.LEFT_PAREN){
            Expression expr = EXPRESSION();
            match(TipoToken.SEMICOLON);
            return expr;
        }
        else if(preanalisis.tipo == TipoToken.SEMICOLON){
            match(TipoToken.SEMICOLON);
            return null;
        }
        else{
            hayErrores = true;
            Interprete.error(preanalisis.linea, "Se esperaba 'BANG' or 'MINUS' or 'TRUE' or 'FALSE' or 'NULL' or 'NUMBER' or 'STRING' or 'IDENTIFIER' or 'LEFT_PAREN' or 'SEMICOLON' pero se encontro '" + preanalisis.tipo + "'");
            return null;
        }
    }

    private Expression FOR_STMT_3() {
        if(hayErrores)
            return null;
        if(preanalisis.tipo == TipoToken.BANG || preanalisis.tipo == TipoToken.MINUS || preanalisis.tipo == TipoToken.FALSE || preanalisis.tipo == TipoToken.TRUE|| preanalisis.tipo == TipoToken.NULL
                || preanalisis.tipo == TipoToken.NUMBER || preanalisis.tipo == TipoToken.STRING || preanalisis.tipo == TipoToken.IDENTIFIER || preanalisis.tipo == TipoToken.LEFT_PAREN){
            Expression expr = EXPRESSION();
            return expr;
        }
        return null;
    }

    private Statement IF_STMT() {
         if(hayErrores)
            return null;
        if(preanalisis.tipo == TipoToken.IF){
            match(TipoToken.IF);
            match(TipoToken.LEFT_PAREN);
            Expression condition = EXPRESSION();
            match(TipoToken.RIGHT_PAREN);
            Statement thenBranch = STATEMENT();
            Statement elseBranch = ELSE_STATEMENT();
            return new StmtIf(condition, thenBranch, elseBranch);
        }
        else{
            hayErrores = true;
            Interprete.error(preanalisis.linea, "Se esperaba 'IF' pero se encontro '" + preanalisis.tipo + "'");
            return null;
        }
    }

    private Statement ELSE_STATEMENT() {
        if(hayErrores)
           return null;
        if(preanalisis.tipo == TipoToken.ELSE){
            match(TipoToken.ELSE);
            Statement elseBranch = STATEMENT();
            return elseBranch;
        }
        return null;
    }

    private Statement PRINT_STMT() {
        if(hayErrores)
           return null;
        if(preanalisis.tipo == TipoToken.PRINT){
            match(TipoToken.PRINT);
            Expression expr = EXPRESSION();
            match(TipoToken.SEMICOLON);
            return new StmtPrint(expr);
        }
        else{
            hayErrores = true;
            Interprete.error(preanalisis.linea, "Se esperaba 'PRINT' pero se encontro '" + preanalisis.tipo + "'");
            return null;
        }
    }

    private Statement RETURN_STMT() {
        if(hayErrores)
           return null;
        if(preanalisis.tipo == TipoToken.RETURN){
            match(TipoToken.RETURN);
            Expression value = null;
            value = RETURN_EXP_OPC(value);
            match(TipoToken.SEMICOLON);
            return new StmtReturn(value);
        }
        else{
            hayErrores = true;
            Interprete.error(preanalisis.linea, "Se esperaba 'RETURN' pero se encontro '" + preanalisis.tipo + "'");
            return null;
        }
    }

    private Expression RETURN_EXP_OPC(Expression value) {
        if(hayErrores)
           return null;
        if (preanalisis.tipo == TipoToken.BANG || preanalisis.tipo == TipoToken.MINUS || preanalisis.tipo == TipoToken.FALSE || preanalisis.tipo == TipoToken.TRUE|| preanalisis.tipo == TipoToken.NULL
                || preanalisis.tipo == TipoToken.NUMBER || preanalisis.tipo == TipoToken.STRING || preanalisis.tipo == TipoToken.IDENTIFIER || preanalisis.tipo == TipoToken.LEFT_PAREN){
            value = EXPRESSION();
            return value;
        }
        return value;
        
    }

    private Statement WHILE_STMT() {
        if(hayErrores)
           return null;  
        if(preanalisis.tipo == TipoToken.WHILE){
            match(TipoToken.WHILE);
            match(TipoToken.LEFT_PAREN);
            Expression condition = EXPRESSION();
            match(TipoToken.RIGHT_PAREN);
            Statement body = STATEMENT();
            return new StmtLoop(condition , body);
        }
        else{
            hayErrores = true;
            Interprete.error(preanalisis.linea, "Se esperaba 'WHILE' pero se encontro '" + preanalisis.tipo + "'");
            return null;
        }
    }

    private Statement BLOCK() {
        if(hayErrores)
           return null;  
        if(preanalisis.tipo == TipoToken.LEFT_BRACE){
            List<Statement> stmts = new ArrayList<>();
            match(TipoToken.LEFT_BRACE);
            DECLARATION(stmts);
            match(TipoToken.RIGHT_BRACE);
            return new StmtBlock(stmts);
        }
        else{
            hayErrores = true;
            Interprete.error(preanalisis.linea, "Se esperaba 'LEFT_BRACE' pero se encontro '" + preanalisis.tipo + "'");
            return null;
        }   
    }
    

    private Expression EXPRESSION() {
        if(hayErrores)
           return null; 
        if(preanalisis.tipo == TipoToken.BANG || preanalisis.tipo == TipoToken.MINUS || preanalisis.tipo == TipoToken.FALSE || preanalisis.tipo == TipoToken.TRUE|| preanalisis.tipo == TipoToken.NULL
                || preanalisis.tipo == TipoToken.NUMBER || preanalisis.tipo == TipoToken.STRING || preanalisis.tipo == TipoToken.IDENTIFIER || preanalisis.tipo == TipoToken.LEFT_PAREN){
            Expression expr = ASSIGNMENT();
            return expr;
        }
        else{
            hayErrores = true;
            Interprete.error(preanalisis.linea, "Se esperaba 'BANG' or 'MINUS' or 'TRUE' or 'FALSE' or 'NULL' or 'NUMBER' or 'STRING' or 'IDENTIFIER' or 'LEFT_PAREN' pero se encontro '" + preanalisis.tipo + "'");
            return null;
        }
    }

    private Expression ASSIGNMENT() {
        if(hayErrores)
           return null; 
        if(preanalisis.tipo == TipoToken.BANG || preanalisis.tipo == TipoToken.MINUS || preanalisis.tipo == TipoToken.FALSE || preanalisis.tipo == TipoToken.TRUE|| preanalisis.tipo == TipoToken.NULL
                || preanalisis.tipo == TipoToken.NUMBER || preanalisis.tipo == TipoToken.STRING || preanalisis.tipo == TipoToken.IDENTIFIER || preanalisis.tipo == TipoToken.LEFT_PAREN){
            Expression expr = LOGIC_OR();
            expr = ASSIGNMENT_OPC(expr);
            return expr;
        }
        else{
            hayErrores = true;
            Interprete.error(preanalisis.linea, "Se esperaba 'BANG' or 'MINUS' or 'TRUE' or 'FALSE' or 'NULL' or 'NUMBER' or 'STRING' or 'IDENTIFIER' or 'LEFT_PAREN' pero se encontro '" + preanalisis.tipo + "'");
            return null;
        }
    }

    private Expression ASSIGNMENT_OPC(Expression expr) {
        if(hayErrores)
           return null; 
        if(preanalisis.tipo == TipoToken.EQUAL){
            Token name = previous();
            match(TipoToken.EQUAL);
            Expression value = EXPRESSION();
            return new ExprAssign(name, value);
        }
        return expr;
    }

    private Expression LOGIC_OR() {
        if(hayErrores)
           return null; 
        if(preanalisis.tipo == TipoToken.BANG || preanalisis.tipo == TipoToken.MINUS || preanalisis.tipo == TipoToken.FALSE || preanalisis.tipo == TipoToken.TRUE|| preanalisis.tipo == TipoToken.NULL
                || preanalisis.tipo == TipoToken.NUMBER || preanalisis.tipo == TipoToken.STRING || preanalisis.tipo == TipoToken.IDENTIFIER || preanalisis.tipo == TipoToken.LEFT_PAREN){
            Expression expr = LOGIC_AND();
            expr = LOGIC_OR_2(expr);
            return expr;
        }
        else{
            hayErrores = true;
            Interprete.error(preanalisis.linea, "Se esperaba 'BANG' or 'MINUS' or 'TRUE' or 'FALSE' or 'NULL' or 'NUMBER' or 'STRING' or 'IDENTIFIER' or 'LEFT_PAREN' pero se encontro '" + preanalisis.tipo + "'");
            return null;
        }
    }

    private Expression LOGIC_OR_2(Expression left) {
        if(hayErrores)
           return null; 
        if(preanalisis.tipo == TipoToken.OR){
            match(TipoToken.OR);
            Token operator = previous();
            Expression right = LOGIC_AND();
            ExprLogical expl = new ExprLogical(left, operator, right);
            return LOGIC_OR_2 (expl);
        }
        return left;
    }

    private Expression LOGIC_AND() {
        if(hayErrores)
           return null; 
        if(preanalisis.tipo == TipoToken.BANG || preanalisis.tipo == TipoToken.MINUS || preanalisis.tipo == TipoToken.FALSE || preanalisis.tipo == TipoToken.TRUE|| preanalisis.tipo == TipoToken.NULL
                || preanalisis.tipo == TipoToken.NUMBER || preanalisis.tipo == TipoToken.STRING || preanalisis.tipo == TipoToken.IDENTIFIER || preanalisis.tipo == TipoToken.LEFT_PAREN){
            Expression expr = EQUALITY();
            expr = LOGIC_AND_2(expr);
            return expr;
        }
        else{
            hayErrores = true;
            Interprete.error(preanalisis.linea, "Se esperaba 'BANG' or 'MINUS' or 'TRUE' or 'FALSE' or 'NULL' or 'NUMBER' or 'STRING' or 'IDENTIFIER' or 'LEFT_PAREN' pero se encontro '" + preanalisis.tipo + "'");
            return null;
        }
    }

    private Expression LOGIC_AND_2(Expression left) {
        if(hayErrores)
           return null; 
        if(preanalisis.tipo == TipoToken.AND){
            match(TipoToken.AND);
            Token operator = previous();
            Expression right = EQUALITY();
            ExprLogical expl = new ExprLogical(left, operator, right);
            return LOGIC_AND_2 (expl);
        }
        return left;
    }

    private Expression  EQUALITY() {
        if(hayErrores)
           return null; 
        if(preanalisis.tipo == TipoToken.BANG || preanalisis.tipo == TipoToken.MINUS || preanalisis.tipo == TipoToken.FALSE || preanalisis.tipo == TipoToken.TRUE|| preanalisis.tipo == TipoToken.NULL
                || preanalisis.tipo == TipoToken.NUMBER || preanalisis.tipo == TipoToken.STRING || preanalisis.tipo == TipoToken.IDENTIFIER || preanalisis.tipo == TipoToken.LEFT_PAREN){
            Expression expr = COMPARISON();
            expr = EQUALITY_2(expr);
            return expr;
        }
        else{
            hayErrores = true;
            Interprete.error(preanalisis.linea, "Se esperaba 'BANG' or 'MINUS' or 'TRUE' or 'FALSE' or 'NULL' or 'NUMBER' or 'STRING' or 'IDENTIFIER' or 'LEFT_PAREN' pero se encontro '" + preanalisis.tipo + "'");
            return null;
        }
    }

    private Expression EQUALITY_2(Expression left) {
        if(hayErrores)
           return null; 
        if(preanalisis.tipo == TipoToken.BANG_EQUAL){
            match(TipoToken.BANG_EQUAL);
            Token operator = previous();
            Expression right = COMPARISON();
            ExprBinary expb = new ExprBinary(left, operator, right);
            return EQUALITY_2(expb);
        }
        else if(preanalisis.tipo == TipoToken.EQUAL_EQUAL){
            match(TipoToken.EQUAL_EQUAL);
            Token operator = previous();
            Expression right = COMPARISON();
            ExprBinary expb = new ExprBinary(left, operator, right);
            return EQUALITY_2(expb);
        }
        return left;
    }

    private Expression COMPARISON() {
        if(hayErrores)
           return null; 
        if(preanalisis.tipo == TipoToken.BANG || preanalisis.tipo == TipoToken.MINUS || preanalisis.tipo == TipoToken.FALSE || preanalisis.tipo == TipoToken.TRUE|| preanalisis.tipo == TipoToken.NULL
                || preanalisis.tipo == TipoToken.NUMBER || preanalisis.tipo == TipoToken.STRING || preanalisis.tipo == TipoToken.IDENTIFIER || preanalisis.tipo == TipoToken.LEFT_PAREN){
            Expression expr = TERM();
            expr = COMPARISON_2(expr);
            return expr;
        }
        else{
            hayErrores = true;
            Interprete.error(preanalisis.linea, "Se esperaba 'BANG' or 'MINUS' or 'TRUE' or 'FALSE' or 'NULL' or 'NUMBER' or 'STRING' or 'IDENTIFIER' or 'LEFT_PAREN' pero se encontro '" + preanalisis.tipo + "'");
            return null;
        }
    }

    private Expression COMPARISON_2(Expression left) {
        if(hayErrores)
           return null; 
        if(preanalisis.tipo == TipoToken.GREATER){
            match(TipoToken.GREATER);
            Token operator = previous();
            Expression right = TERM();
            ExprBinary expb = new ExprBinary(left, operator, right);
            return COMPARISON_2(expb);
        }
        else if(preanalisis.tipo == TipoToken.GREATER_EQUAL){
            match(TipoToken.GREATER_EQUAL);
            Token operator = previous();
            Expression right = TERM();
            ExprBinary expb = new ExprBinary(left, operator, right);
            return COMPARISON_2(expb);
        }
        else if(preanalisis.tipo == TipoToken.LESS){
            match(TipoToken.LESS);
            Token operator = previous();
            Expression right = TERM();
            ExprBinary expb = new ExprBinary(left, operator, right);
            return COMPARISON_2(expb);
        }
        else if(preanalisis.tipo == TipoToken.LESS_EQUAL){
            match(TipoToken.LESS_EQUAL);
            Token operator = previous();
            Expression right = TERM();
            ExprBinary expb = new ExprBinary(left, operator, right);
            return COMPARISON_2(expb);
        }
        return left;
    }

    private Expression TERM() {
        if(hayErrores)
           return null; 
        if(preanalisis.tipo == TipoToken.BANG || preanalisis.tipo == TipoToken.MINUS || preanalisis.tipo == TipoToken.FALSE || preanalisis.tipo == TipoToken.TRUE|| preanalisis.tipo == TipoToken.NULL
                || preanalisis.tipo == TipoToken.NUMBER || preanalisis.tipo == TipoToken.STRING || preanalisis.tipo == TipoToken.IDENTIFIER || preanalisis.tipo == TipoToken.LEFT_PAREN){
            Expression expr = FACTOR();
            expr = TERM_2(expr);
            return expr;
        }
        else{
            hayErrores = true;
            Interprete.error(preanalisis.linea, "Se esperaba 'BANG' or 'MINUS' or 'TRUE' or 'FALSE' or 'NULL' or 'NUMBER' or 'STRING' or 'IDENTIFIER' or 'LEFT_PAREN' pero se encontro '" + preanalisis.tipo + "'");
            return null;
        }
    }

    private Expression TERM_2(Expression left) {
        if(hayErrores)
           return null; 
        if(preanalisis.tipo == TipoToken.MINUS){
            match(TipoToken.MINUS);
            Token operator = previous();
            Expression right = FACTOR();
            ExprBinary expb = new ExprBinary(left, operator, right);
            return TERM_2(expb);
        }
        else if(preanalisis.tipo == TipoToken.PLUS){
            match(TipoToken.PLUS);
            Token operator = previous();
            Expression right = FACTOR();
            ExprBinary expb = new ExprBinary(left, operator, right);
            return TERM_2(expb);
        }
    return left;
    }

    private Expression FACTOR() { 
        if(hayErrores)
           return null;
        if(preanalisis.tipo == TipoToken.BANG || preanalisis.tipo == TipoToken.MINUS || preanalisis.tipo == TipoToken.FALSE || preanalisis.tipo == TipoToken.TRUE|| preanalisis.tipo == TipoToken.NULL
                || preanalisis.tipo == TipoToken.NUMBER || preanalisis.tipo == TipoToken.STRING || preanalisis.tipo == TipoToken.IDENTIFIER || preanalisis.tipo == TipoToken.LEFT_PAREN){
            Expression expr = UNARY();
            expr = FACTOR_2(expr);
            return expr;
        }
        else{
            hayErrores = true;
            Interprete.error(preanalisis.linea, "Se esperaba 'BANG' or 'MINUS' or 'TRUE' or 'FALSE' or 'NULL' or 'NUMBER' or 'STRING' or 'IDENTIFIER' or 'LEFT_PAREN' pero se encontro '" + preanalisis.tipo + "'");
            return null;
        }
    }

    private Expression FACTOR_2(Expression left) {
        if(hayErrores)
           return null; 
        if(preanalisis.tipo == TipoToken.SLASH){
            match(TipoToken.SLASH);
            Token operator = previous();
            Expression right = UNARY();
            ExprBinary expb = new ExprBinary(left, operator, right);
            return FACTOR_2(expb);
        }
        else if (preanalisis.tipo == TipoToken.STAR) {
            match(TipoToken.STAR);  
            Token operator = previous();
            Expression right = UNARY();  
            ExprBinary expb = new ExprBinary(left, operator, right);
            return FACTOR_2(expb);
        }
        return left;
    }    

    private Expression UNARY (){
        if(hayErrores)
           return null; 
        if(preanalisis.tipo == TipoToken.BANG){
            match(TipoToken.BANG);
            Token operator = previous();
            Expression expr = UNARY();
            return new ExprUnary(operator, expr);
        }
        else if (preanalisis.tipo == TipoToken.MINUS) {
            match(TipoToken.MINUS);  
            Token operator = previous();
            Expression expr = UNARY();
            return new ExprUnary(operator, expr);              
        }
        else if(preanalisis.tipo == TipoToken.FALSE || preanalisis.tipo == TipoToken.TRUE|| preanalisis.tipo == TipoToken.NULL
                || preanalisis.tipo == TipoToken.NUMBER || preanalisis.tipo == TipoToken.STRING || preanalisis.tipo == TipoToken.IDENTIFIER || preanalisis.tipo == TipoToken.LEFT_PAREN){
            return CALL();
        }
        else{
            hayErrores = true;
            Interprete.error(preanalisis.linea, "Se esperaba 'BANG' or 'MINUS' or 'TRUE' or 'FALSE' or 'NULL' or 'NUMBER' or 'STRING' or 'IDENTIFIER' or 'LEFT_PAREN' pero se encontro '" + preanalisis.tipo + "'");
            return null;
        }
    }

    private Expression CALL(){
        if(hayErrores)
           return null;
        if(preanalisis.tipo == TipoToken.FALSE || preanalisis.tipo == TipoToken.TRUE|| preanalisis.tipo == TipoToken.NULL
                || preanalisis.tipo == TipoToken.NUMBER || preanalisis.tipo == TipoToken.STRING || preanalisis.tipo == TipoToken.IDENTIFIER || preanalisis.tipo == TipoToken.LEFT_PAREN){
            Expression expr = PRIMARY();
            expr = CALL_2(expr);
            return expr;
        }
        else{
            hayErrores = true;
            Interprete.error(preanalisis.linea, "Se esperaba 'TRUE' or 'FALSE' or 'NULL' or 'NUMBER' or 'STRING' or 'IDENTIFIER' or 'LEFT_PAREN' pero se encontro '" + preanalisis.tipo + "'");
            return null;
        }
    } 

    private Expression CALL_2(Expression expr){
        if(hayErrores)
           return null; 
        if(preanalisis.tipo == TipoToken.LEFT_PAREN){
            match(TipoToken.LEFT_PAREN);
            List <Expression> arguments = ARGUMENTS_OPC();
            match(TipoToken.RIGHT_PAREN);
            ExprCallFunction ecf = new ExprCallFunction(expr, arguments);
            return ecf;
        }
        return expr;
   }

    private Expression PRIMARY(){
        if(hayErrores)
           return null; 
        if(preanalisis.tipo == TipoToken.TRUE){
            match(TipoToken.TRUE); 
            return new ExprLiteral(true);
        }
        else if(preanalisis.tipo == TipoToken.FALSE){
            match(TipoToken.FALSE);    
            return new ExprLiteral(false);
        }
        else if(preanalisis.tipo == TipoToken.NULL){
            match(TipoToken.NULL);      
            return new ExprLiteral(null);
        }
        else if(preanalisis.tipo == TipoToken.NUMBER){
            match(TipoToken.NUMBER);  
            Token numero = previous();
            return new ExprLiteral(numero.literal);
        }
        else if(preanalisis.tipo == TipoToken.STRING){
            match(TipoToken.STRING);
            Token cadena = previous();
            return new ExprLiteral(cadena.literal);
        }
        else if(preanalisis.tipo == TipoToken.IDENTIFIER){
            match(TipoToken.IDENTIFIER);
            Token name = previous();
            return new ExprVariable(name);
        }
        else if(preanalisis.tipo == TipoToken.LEFT_PAREN){
            match(TipoToken.LEFT_PAREN);
            Expression expr = EXPRESSION();
            match(TipoToken.RIGHT_PAREN);
            return new ExprGrouping(expr);
        }else{
            hayErrores = true;
            Interprete.error(preanalisis.linea, "Se esperaba 'TRUE' or 'FALSE' or 'NULL' or 'NUMBER' or 'STRING' or 'IDENTIFIER' or 'LEFT_PAREN' pero se encontro '" + preanalisis.tipo + "'");
            return null;
        }
    }
 

    private Statement FUNCTION() {
        if(hayErrores)
           return null; 
       if(preanalisis.tipo == TipoToken.IDENTIFIER){
            match(TipoToken.IDENTIFIER);
            Token name = previous();
            match (TipoToken.LEFT_PAREN);
            List <Token> params = PARAMETERS_OPC();
            match(TipoToken.RIGHT_PAREN);
            Statement body = BLOCK();
            return new StmtFunction(name, params, (StmtBlock) body);            
        }
        else{
            hayErrores = true;
            Interprete.error(preanalisis.linea, "Se esperaba 'IDENTIFIER' pero se encontro '" + preanalisis.tipo + "'");
            return null;
        }
    }

    private void FUNCTIONS(){
        if(hayErrores)
           return; 
        if(preanalisis.tipo == TipoToken.FUN){
            FUN_DECL();
            FUNCTIONS();
        }      
    }

    private List <Token> PARAMETERS_OPC(){
        if(hayErrores)
           return null; 
        if(preanalisis.tipo == TipoToken.IDENTIFIER){
            List <Token> params = new ArrayList<>();
            params = PARAMETERS(params);  
            return params;
        } 
        return null;
    }

    private List <Token> PARAMETERS(List <Token> params){
        if(hayErrores)
           return null; 
        if(preanalisis.tipo == TipoToken.IDENTIFIER){
            Token paramToken=preanalisis;
            match(TipoToken.IDENTIFIER);
            params.add(paramToken);
            params = PARAMETERS_2(params);
            return params;
        }
        else{
            hayErrores = true;
            Interprete.error(preanalisis.linea, "Se esperaba 'IDENTIFIER' pero se encontro '" + preanalisis.tipo + "'");
            return null;
        }
    }

     private List <Token> PARAMETERS_2(List <Token> params){
        if(hayErrores)
           return null; 
        if(preanalisis.tipo == TipoToken.COMMA){
            match(TipoToken.COMMA);
            match(TipoToken.IDENTIFIER);
            Token paramToken = previous();
            params.add(paramToken);
            params = PARAMETERS_2(params);
        }    
        return params;
    }

    private List <Expression> ARGUMENTS_OPC(){
        if(hayErrores)
           return null; 
        if(preanalisis.tipo == TipoToken.BANG || preanalisis.tipo == TipoToken.MINUS || preanalisis.tipo == TipoToken.FALSE || preanalisis.tipo == TipoToken.TRUE|| preanalisis.tipo == TipoToken.NULL
                || preanalisis.tipo == TipoToken.NUMBER || preanalisis.tipo == TipoToken.STRING || preanalisis.tipo == TipoToken.IDENTIFIER || preanalisis.tipo == TipoToken.LEFT_PAREN){
            List <Expression> arguments = new ArrayList<>();
            arguments.add(EXPRESSION());
            arguments = ARGUMENTS(arguments);
            return arguments;
        }  
        return null;
    }
    

    private List<Expression> ARGUMENTS(List <Expression> arguments){
        if(hayErrores)
           return null; 
        if(preanalisis.tipo == TipoToken.COMMA){
            match(TipoToken.COMMA);
            arguments.add(EXPRESSION());
            ARGUMENTS(arguments);
         }
        return arguments;
    }
    
    private void match(TipoToken tt){
        if(preanalisis.tipo == tt){
            i++;
            preanalisis = tokens.get(i);
        }
        else{
            hayErrores = true;
            preanalisis = previous();
            Interprete.error(preanalisis.linea, "Se esperaba '" + tt + "' pero se encontro '" + preanalisis.tipo + "'");
        }
    }
    private Token previous() {
        return this.tokens.get(i - 1);
    }
}