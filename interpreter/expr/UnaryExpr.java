package interpreter.expr;

public class UnaryExpr extends Expr{
    private Expr expr;
    UnaryOp op;

    public UnaryExpr(int line, Expr expr, UnaryOp op){
        super(line);
        this.expr = expr;
        this.op = op;
    }

    @Override
    public Value<?> expr(){

    }
}
