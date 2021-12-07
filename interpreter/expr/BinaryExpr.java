package interpreter.expr;

public class BinaryExpr extends Expr{
    private Expr left;
    private BinaryOp op;
    private Expr right;

    public BinaryExpr(int line, Expr left, Expr right, BinaryOp op){
        super(line);
        this.left = left;
        this.right = right;
        this.op = op;
    }

    @Override
    public Value<?> expr(){
        
    }
}
