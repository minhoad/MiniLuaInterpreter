package syntatic;

import java.util.ArrayList;
import java.util.List;

import interpreter.command.AssignCommand;
import interpreter.command.BlocksCommand;
import interpreter.command.Command;
import interpreter.command.GenericForCommand;
import interpreter.command.IfCommand;
import interpreter.command.NumericForCommand;
import interpreter.command.PrintCommand;
import interpreter.command.RepeatCommand;
import interpreter.command.WhileCommand;
import interpreter.expr.AccessExpr;
import interpreter.expr.BinaryExpr;
import interpreter.expr.BinaryOp;
import interpreter.expr.ConstExpr;
import interpreter.expr.Expr;
import interpreter.expr.SetExpr;
import interpreter.expr.TableEntry;
import interpreter.expr.TableExpr;
import interpreter.expr.Variable;
import interpreter.expr.UnaryExpr;
import interpreter.expr.UnaryOp;
import interpreter.value.BooleanValue;
import interpreter.value.NumberValue;
import interpreter.value.StringValue;
import interpreter.value.Value;
import lexical.Lexeme;
import lexical.LexicalAnalysis;
import lexical.TokenType;
import java.util.Vector;

public class SyntaticAnalysis {

    private LexicalAnalysis lex;
    private Lexeme current;

    public SyntaticAnalysis(LexicalAnalysis lex) {
        this.lex = lex;
        this.current = lex.nextToken();
    }

    public Command start() {
        BlocksCommand cmds = procCode();
        eat(TokenType.END_OF_FILE);
        
        return cmds;
    }

    private void advance() {
        // System.out.println("Advanced (\"" + current.token + "\", " +
        //     current.type + ")");
        current = lex.nextToken();
    }

    private void eat(TokenType type) {
        // System.out.println("Expected (..., " + type + "), found (\"" + 
        //     current.token + "\", " + current.type + ")");
        if (type == current.type) {
            current = lex.nextToken();
        } else {
            showError();
        }
    }

    private void showError() {
        System.out.printf("%02d: ", lex.getLine());

        switch (current.type) {
            case INVALID_TOKEN:
                System.out.printf("Lexema inválido [%s]\n", current.token);
                break;
            case UNEXPECTED_EOF:
            case END_OF_FILE:
                System.out.printf("Fim de arquivo inesperado\n");
                break;
            default:
                System.out.printf("Lexema não esperado [%s]\n", current.token);
                break;
        }

        System.exit(1);
    }

    // <code> ::= { <cmd> }
    private BlocksCommand procCode() {
        int line = lex.getLine();
        List<Command> cmds = new ArrayList<Command>();
        while (current.type == TokenType.IF ||
                current.type == TokenType.WHILE ||
                current.type == TokenType.REPEAT ||
                current.type == TokenType.FOR ||
                current.type == TokenType.PRINT ||
                current.type == TokenType.ID) {
            Command cmd = procCmd();
            cmds.add(cmd);
        }

        BlocksCommand bc = new BlocksCommand(line, cmds);
        return bc;
    }

    // <cmd> ::= (<if> | <while> | <repeat> | <for> | <print> | <assign>) [';']
    private Command procCmd() {
        Command cmd = null;
        if (current.type == TokenType.IF) {
            cmd = procIf();
        } else if (current.type == TokenType.WHILE) {
            cmd = procWhile();
        } else if (current.type == TokenType.REPEAT) {
            cmd = procRepeat();
        } else if (current.type == TokenType.FOR) {
            cmd = procFor();
        } else if (current.type == TokenType.PRINT) {
            cmd = procPrint();
        } else if (current.type == TokenType.ID) {
           cmd = procAssign();
        } else {
            showError();
        }

        if (current.type == TokenType.SEMI_COLON)
            advance();
        
        return cmd;
    }

    // <if> ::= if <expr> then <code> { elseif <expr> then <code> } [ else <code> ] end
    private IfCommand procIf() {
        eat(TokenType.IF);
        int line = lex.getLine();
        Expr expr = procExpr();
        eat(TokenType.THEN);
        Command cmd = procCode();
        while (current.type == TokenType.ELSEIF) {
            advance();
            expr = procExpr();
            eat(TokenType.THEN);
            cmd = procCode();
        }
        IfCommand ic = new IfCommand(line, cmd, expr);
        if (current.type == TokenType.ELSE) {
            advance();
            cmd = procCode();
            ic.setElseCommands(cmd);
        }
        eat(TokenType.END);
        return ic;
    }

    // <while> ::= while <expr> do <code> end
    private WhileCommand procWhile() {
        int line = lex.getLine();
        eat(TokenType.WHILE);
        Expr expr = procExpr();
        eat(TokenType.DO);
        Command cmd = procCode();
        eat(TokenType.END);

        WhileCommand wc = new WhileCommand(line, expr, cmd);
        return wc;
    }

    // <repeat> ::= repeat <code> until <expr>
    private RepeatCommand procRepeat() {
        int line = lex.getLine();
        eat(TokenType.REPEAT);
        Command cmd = procCode();
        eat(TokenType.UNTIL);
        Expr expr = procExpr();

        RepeatCommand rc = new RepeatCommand(line, cmd, expr);
        return rc;
    }

    // <for> ::= for <name> (('=' <expr> ',' <expr> [',' <expr>]) | ([',' <name>] in <expr>)) do <code> end
    private Command procFor() {
        eat(TokenType.FOR);
        Variable var = procName();
        if(current.type == TokenType.ASSIGN){ // FOR NUMERIC
            advance();
            Expr expr1 = procExpr();
            eat(TokenType.COLON);
            Expr expr2 = procExpr();
            Expr expr3 = null;
            if(current.type == TokenType.COLON){
                advance();
                expr3 = procExpr();
            }
            eat(TokenType.DO);
            Command cmd = procCode();
            eat(TokenType.END);
            int line = lex.getLine();
            NumericForCommand nfc = new NumericForCommand(line, var ,expr1, expr2, expr3, cmd);
            return nfc;
        }else{                              // FOR GENERIC
            Variable var2 = null;
            if(current.type == TokenType.COLON){
                advance();
                var2 = procName();
            }
            eat(TokenType.IN);
            Expr expr = procExpr();
            eat(TokenType.DO);
            Command cmd = procCode();
            eat(TokenType.END);
            int line = lex.getLine();
            GenericForCommand gfc = new GenericForCommand(line, var, var2, expr, cmd);
            return gfc; 
        }
    }

    // <print> ::= print '(' [ <expr> ] ')'
    private PrintCommand procPrint() {
        eat(TokenType.PRINT);
        int line = lex.getLine();
        eat(TokenType.OPEN_PAR);

        Expr expr = null;
        if (current.type == TokenType.OPEN_PAR ||
                current.type == TokenType.SUB ||
                current.type == TokenType.SIZE ||
                current.type == TokenType.NOT ||
                current.type == TokenType.NUMBER ||
                current.type == TokenType.STRING ||
                current.type == TokenType.FALSE ||
                current.type == TokenType.TRUE ||
                current.type == TokenType.NIL ||
                current.type == TokenType.READ ||
                current.type == TokenType.TONUMBER ||
                current.type == TokenType.TOSTRING ||
                current.type == TokenType.OPEN_CUR ||
                current.type == TokenType.ID) {
            expr = procExpr();  
        }

        eat(TokenType.CLOSE_PAR);

        PrintCommand pc = new PrintCommand(line, expr);
        return pc;
    }

    // <assign> ::= <lvalue> { ',' <lvalue> } '=' <expr> { ',' <expr> }
    private AssignCommand procAssign() {
        Vector<SetExpr> lhs = new Vector<SetExpr>();
        Vector<Expr> rhs = new Vector<Expr>();

        lhs.add(procLValue());
        while (current.type == TokenType.COLON) {
            advance();
            lhs.add(procLValue());
        }
        
        eat(TokenType.ASSIGN);
        int line = lex.getLine();
        
        rhs.add(procExpr());
        while (current.type == TokenType.COLON) {
            advance();
            rhs.add(procExpr());
        }

        return new AssignCommand(line, lhs, rhs);
    }

    // <expr> ::= <rel> { (and | or) <rel> }
    private Expr procExpr() {
        Expr expr = procRel();
        while (current.type == TokenType.AND || current.type == TokenType.OR) {
            BinaryOp op = null;
            if(current.type == TokenType.AND){
                op = BinaryOp.AndOp;
                advance();
            }else {
                op = BinaryOp.OrOp;
                advance();
            }
            
            Expr expr2 = procRel();
            int line = lex.getLine();
            return new BinaryExpr(line, expr, expr2, op);
        }
        return expr;
    }

    // <rel> ::= <concat> [ ('<' | '>' | '<=' | '>=' | '~=' | '==') <concat> ]
    private Expr procRel() {
        Expr expr = procConcat();

        if(current.type == TokenType.LOWER_THAN ||
        current.type == TokenType.GREATER_THAN ||
        current.type == TokenType.LOWER_EQUAL ||
        current.type == TokenType.GREATER_EQUAL ||
        current.type == TokenType.NOT_EQUAL ||
        current.type == TokenType.EQUAL){

            BinaryOp op = null;
            if(current.type == TokenType.LOWER_THAN){
                op = BinaryOp.LowerThanOp;
                advance();
            }else if(current.type == TokenType.GREATER_THAN){
                op = BinaryOp.GreaterThanOp;
                advance();
            }else if(current.type == TokenType.LOWER_EQUAL){
                op = BinaryOp.LowerEqualOp;
                advance();
            }else if(current.type == TokenType.GREATER_EQUAL){
                op = BinaryOp.GreaterEqualOp;
                advance();
            }else if(current.type == TokenType.NOT_EQUAL){
                op = BinaryOp.NotEqualOp;
                advance();
            }else {
                op = BinaryOp.EqualOp;
                advance();
            }
            
            Expr expr2 = procConcat();
            int line = lex.getLine();
            return new BinaryExpr(line, expr, expr2, op);
        }
        return expr;
    }

    // <concat> ::= <arith> { '..' <arith> }
    private Expr procConcat() {
        Expr expr = procArith();
        while(current.type == TokenType.CONCAT){
            advance();
            Expr expr2 = procArith();
            int line = lex.getLine();
            return new BinaryExpr(line, expr, expr2, BinaryOp.ConcatOp);
        }
        return expr;
    }

    // <arith> ::= <term> { ('+' | '-') <term> }
    private Expr procArith() {
        Expr expr = procTerm();
        while(current.type == TokenType.ADD || current.type == TokenType.SUB){
            BinaryOp op = null;
            if(current.type == TokenType.ADD){
                op = BinaryOp.AddOp;
                advance();
            }else{
                op = BinaryOp.SubOp;
                advance();
            }
            
            Expr expr2 = procTerm();
            int line = lex.getLine();
            return new BinaryExpr(line, expr, expr2, op);
        }
        return expr;
    }

    // <term> ::= <factor> { ('*' | '/' | '%') <factor> }
    private Expr procTerm() {
        int line = lex.getLine();
        Expr expr = procFactor();
     
        while(current.type == TokenType.MUL || current.type == TokenType.DIV || current.type == TokenType.MOD){
            BinaryOp op = null;
            if(current.type == TokenType.MUL){
                op = BinaryOp.MulOp;
            }else if(current.type == TokenType.DIV){
                op = BinaryOp.DivOp;
            }else {
                op = BinaryOp.ModOp;
            }
            advance();
            Expr expr2 = procFactor();
            return new BinaryExpr(line, expr, expr2, op);
        }
        return expr;
    }

    // <factor> ::= '(' <expr> ')' | [ '-' | '#' | not] <rvalue>
    private Expr procFactor() {
        Expr expr = null;
        if (current.type == TokenType.OPEN_PAR) {
            advance(); 
            expr = procExpr();
            eat(TokenType.CLOSE_PAR);
        } else {
            UnaryOp op = null;
            if (current.type == TokenType.SUB) {
                advance();
                op = UnaryOp.NegOp;
            } else if (current.type == TokenType.SIZE) {
                advance();
                op = UnaryOp.SizeOp;
            } else if  (current.type == TokenType.NOT){
                advance();
                op = UnaryOp.NotOp;
            }
            int line = lex.getLine();
            expr = procRValue();
            if (op != null)
                expr = new UnaryExpr(line, expr, op);
        }
        return expr;
    }

    // <lvalue> ::= <name> { '.' <name> | '[' <expr> ']' }
    private SetExpr procLValue() {
        Variable var = procName();

        while (current.type == TokenType.DOT ||
                current.type == TokenType.OPEN_BRA) {
            if (current.type == TokenType.DOT) {
                advance();
                Variable aux_ = procName();
                int line = lex.getLine();
                String name = aux_.getName();

                StringValue sv = new StringValue(name);
                ConstExpr index = new ConstExpr(line, sv);

                Variable var2 = new Variable(line, sv.value());
                var2.SetValue(index.expr());

                return new AccessExpr(line, var, var2);
            } else {
                advance();
                Expr index = procExpr();
                int line = lex.getLine();
                eat(TokenType.CLOSE_BRA);
                return new AccessExpr(line, var, index);
            }
        }
        return var;
    }

    // <rvalue> ::= <const> | <function> | <table> | <lvalue>
    private Expr procRValue() {
        Expr expr = null;
        if (current.type == TokenType.NUMBER ||
                current.type == TokenType.STRING ||
                current.type == TokenType.FALSE ||
                current.type == TokenType.TRUE ||
                current.type == TokenType.NIL) {
            Value<?> v = procConst();
            int line = lex.getLine();
            expr = new ConstExpr(line, v);
        } else if (current.type == TokenType.READ ||
                current.type == TokenType.TONUMBER ||
                current.type == TokenType.TOSTRING) {
            expr = procFunction();
        } else if (current.type == TokenType.OPEN_CUR) {
            expr = procTable();
        } else if (current.type == TokenType.ID) {
            expr = procLValue();
        } else {
            showError();
        }
        return expr;
    }

    // <const> ::= <number> | <string> | false | true | nil
    private Value<?> procConst() {
        Value<?> v = null;
        if (current.type == TokenType.NUMBER) {
            v = procNumber();
        } else if (current.type == TokenType.STRING) {
            v = procString();
        } else if (current.type == TokenType.FALSE) {
            advance();
            v = new BooleanValue(false);
        } else if (current.type == TokenType.TRUE) {
            advance();
            v = new BooleanValue(true);
        } else if (current.type == TokenType.NIL) {
            advance();
            v = null;
        } else {
            showError();
        }

        return v;
    }

    // <function> ::= (read | tonumber | tostring) '(' [ <expr> ] ')'
    private UnaryExpr procFunction() {
        UnaryOp op = null;
            
        if(current.type == TokenType.TOSTRING){
            op = UnaryOp.ToStringOp;
            advance();
        }else if(current.type == TokenType.TONUMBER){
            op = UnaryOp.ToNumberOp;
            advance();
        }else if(current.type == TokenType.READ){
            op = UnaryOp.ReadOp;
            advance();
        }else{
            showError();
        }

        int line = lex.getLine();
        eat(TokenType.OPEN_PAR);
        
        Expr expr = null;

        if(current.type == TokenType.OPEN_PAR ||
        current.type == TokenType.SUB ||
        current.type == TokenType.SIZE ||
        current.type == TokenType.NOT ||
        current.type == TokenType.NUMBER ||
        current.type == TokenType.STRING ||
        current.type == TokenType.FALSE ||
        current.type == TokenType.TRUE ||
        current.type == TokenType.NIL ||
        current.type == TokenType.READ ||
        current.type == TokenType.TONUMBER ||
        current.type == TokenType.TOSTRING ||
        current.type == TokenType.OPEN_CUR ||
        current.type == TokenType.ID){ 
            expr = procExpr();
        }

        eat(TokenType.CLOSE_PAR);
        
        return new UnaryExpr(line, expr, op);
    }

    // <table> ::= '{' [ <elem> { ',' <elem> } ] '}'
    private TableExpr procTable() {
        eat(TokenType.OPEN_CUR);
        if(current.type == TokenType.CLOSE_CUR){
            advance();
            int line = lex.getLine();
            TableExpr aux = new TableExpr(line);
            return aux;
        }
        int line = lex.getLine();
        
        TableExpr aux = new TableExpr(line); 
        TableEntry aux_entry = procElem(); 
        
        if(aux_entry != null){    
            aux.addEntry(aux_entry);
            while(current.type == TokenType.COLON){
                advance();
                aux_entry = (TableEntry) procElem();
                aux.addEntry(aux_entry);
            }
        }
        eat(TokenType.CLOSE_CUR);
        return aux;
    }

    // <elem> ::= [ '[' <expr> ']' '=' ] <expr>
    private TableEntry procElem() {
        int line = lex.getLine();
        Expr expr = null;
        Expr expr2 = null;
        if(current.type == TokenType.OPEN_BRA){
            advance();
            expr2 = procExpr();
            eat(TokenType.CLOSE_BRA);
            eat(TokenType.ASSIGN);
        }
        expr = procExpr();
        if(expr2 != null && expr != null){ // retorna a dupla completa key e value escolhidos
            TableEntry aux = new TableEntry(line);
            aux.key = expr2;
            aux.value = expr;
            return aux;
        }else{ // só tem value,  retorna a dupla incompleta key e value escolhidos
            TableEntry aux = new TableEntry(line);
            aux.value = expr;
            aux.key = null;
            return aux;
        }
        
    }

    private Variable procName() {
        String name = current.token;
        eat(TokenType.ID);
        int line = lex.getLine();

        Variable var = new Variable(line,name);
        return var;
    }

    private NumberValue procNumber() {
        String tmp = current.token;
        eat(TokenType.NUMBER);

        Double d = Double.valueOf(tmp);
        NumberValue nv = new NumberValue(d);
        return nv;
    }

    private StringValue procString() {
        String tmp = current.token;
        eat(TokenType.STRING);

        StringValue sv = new StringValue(tmp);
        return sv;
    }

}