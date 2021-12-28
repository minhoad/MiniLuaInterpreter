package interpreter.command;

import java.util.Vector;
import interpreter.expr.Expr;
import interpreter.expr.SetExpr;
import interpreter.expr.Variable;
import interpreter.util.Utils;

public class AssignCommand extends Command{
    private Vector<SetExpr> lhs;
    private Vector<Expr> rhs;

    public AssignCommand(int line, Vector<SetExpr> lhs, Vector<Expr> rhs){
        super(line);
        this.lhs = lhs;
        this.rhs = rhs;
    }
    @Override
    public void execute(){ 
        if(lhs.size() != rhs.size())
            Utils.abort(super.getLine());
        else{    
            int size = lhs.size();
            for(int i=0;i<size;i++){
                if(!(lhs.get(i) instanceof Variable)){
                    Utils.abort(super.getLine());
                }
                else { 
                    lhs.get(i).SetValue(rhs.get(i).expr());
                }
            }
        }
    }
}
