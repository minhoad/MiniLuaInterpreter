package interpreter.command;

import java.util.Vector;
import interpreter.expr.Expr;
import interpreter.expr.SetExpr;

public class AssignCommand extends Command{
    private Vector<SetExpr> lhs = new Vector<SetExpr>();
    private Vector<Expr> rhs = new Vector<Expr>();

    public AssignCommand(int line, Vector<SetExpr> lhs, Vector<Expr> rhs){
        super(line);
        this.lhs = lhs;
        this.rhs = rhs;
    }
    @Override
    public void execute(){ 
        //TODO
    }
}
