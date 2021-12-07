package syntatic;

import java.util.ArrayList;
import java.util.List;

import interpreter.command.BlocksCommand;
import interpreter.command.Command;
import interpreter.command.PrintCommand;
import interpreter.expr.ConstExpr;
import interpreter.expr.Expr;
import interpreter.value.BooleanValue;
import interpreter.value.NumberValue;
import interpreter.value.StringValue;
import interpreter.value.Value;
import lexical.Lexeme;
import lexical.LexicalAnalysis;
import lexical.TokenType;

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
            procIf();
        } else if (current.type == TokenType.WHILE) {
            procWhile();
        } else if (current.type == TokenType.REPEAT) {
            procRepeat();
        } else if (current.type == TokenType.FOR) {
            procFor();
        } else if (current.type == TokenType.PRINT) {
            cmd = procPrint();
        } else if (current.type == TokenType.WHILE) {
            procWhile();
        } else if (current.type == TokenType.ID) {
            procAssign();
        } else {
            showError();
        }

        if (current.type == TokenType.SEMI_COLON)
            advance();
        
        return cmd;
    }

    // <if> ::= if <expr> then <code> { elseif <expr> then <code> } [ else <code> ] end
    private void procIf() {
        eat(TokenType.IF);
        procExpr();
        eat(TokenType.THEN);
        procCode();
        while (current.type == TokenType.ELSEIF) {
            advance();
            procExpr();
            eat(TokenType.THEN);
            procCode();
        }
        if (current.type == TokenType.ELSE) {
            advance();
            procCode();
        }
        eat(TokenType.END);
    }

    // <while> ::= while <expr> do <code> end
    private void procWhile() {
        eat(TokenType.WHILE);
        procExpr();
        eat(TokenType.DO);
        procCode();
        eat(TokenType.END);
    }

    // <repeat> ::= repeat <code> until <expr>
    private void procRepeat() {
    }

    // <for> ::= for <name> (('=' <expr> ',' <expr> [',' <expr>]) | ([',' <name>] in <expr>)) do <code> end
    private void procFor() {
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
    private void procAssign() {
        procLValue();
        while (current.type == TokenType.COLON) {
            advance();
            procLValue();
        }
        eat(TokenType.ASSIGN);
        procExpr();
        while (current.type == TokenType.COLON) {
            advance();
            procExpr();
        }
    }

    // <expr> ::= <rel> { (and | or) <rel> }
    private Expr procExpr() {
        Expr expr = procRel();
        while (current.type == TokenType.AND || current.type == TokenType.OR) {
            advance();
            // FIXME: Implement me!
            procRel();
        }

        return expr;
    }

    // <rel> ::= <concat> [ ('<' | '>' | '<=' | '>=' | '~=' | '==') <concat> ]
    private Expr procRel() {
        Expr expr = procConcat();
        // FIXME: Implement me!

        return expr;
    }

    // <concat> ::= <arith> { '..' <arith> }
    private Expr procConcat() {
        Expr expr = procArith();
        // FIXME: Implement me!

        return expr;
    }

    // <arith> ::= <term> { ('+' | '-') <term> }
    private Expr procArith() {
        Expr expr = procTerm();
        // FIXME: Implement me!

        return expr;
    }

    // <term> ::= <factor> { ('*' | '/' | '%') <factor> }
    private Expr procTerm() {
        Expr expr = procFactor();
        // FIXME: Implement me!

        return expr;
    }

    // <factor> ::= '(' <expr> ')' | [ '-' | '#' | not] <rvalue>
    private Expr procFactor() {
        Expr expr = null;
        if (current.type == TokenType.OPEN_PAR) {
            // FIXME: Implement me!
        } else {
            // FIXME: Implement me!

            expr = procRValue();
        }

        return expr;
    }

    // <lvalue> ::= <name> { '.' <name> | '[' <expr> ']' }
    private void procLValue() {
        procName();
        while (current.type == TokenType.DOT ||
                current.type == TokenType.OPEN_BRA) {
            if (current.type == TokenType.DOT) {
                advance();
                procName();
            } else {
                advance();
                procExpr();
                eat(TokenType.CLOSE_BRA);
            }
        }
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
            procFunction();
        } else if (current.type == TokenType.OPEN_CUR) {
            procTable();
        } else if (current.type == TokenType.ID) {
            procLValue();
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
    private void procFunction() {
    }

    // <table> ::= '{' [ <elem> { ',' <elem> } ] '}'
    private void procTable() {
    }

    // <elem> ::= [ '[' <expr> ']' '=' ] <expr>
    private void procElem() {
    }

    private void procName() {
        eat(TokenType.ID);
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