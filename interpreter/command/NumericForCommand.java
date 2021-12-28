package interpreter.command;

import interpreter.expr.ConstExpr;
import interpreter.expr.Expr;
import interpreter.expr.Variable;
import interpreter.value.NumberValue;


public class NumericForCommand extends Command{
    private Variable var;
    private Expr expr1;
    private Expr expr2;
    private Expr expr3;
    private Command cmds;

    public NumericForCommand(int line, Variable var, Expr expr1, Expr expr2, Expr expr3, Command cmds){
        super(line);
        this.var = var;
        this.expr1 = expr1; 
        this.expr2 = expr2;
        this.expr3 = (expr3 != null) ? expr3 : new ConstExpr(line, new NumberValue(1.0));
        this.cmds = cmds;
    }

    @Override 
    public void execute(){
        //var = expr1;
        //condição expr2
        //pós instrução expr3
        Double i =  (Double) expr1.expr().value();
        for(; Double.compare(i, (Double) expr2.expr().value()) <= 0 ; i += (Double) expr3.expr().value()){
            NumberValue value = new NumberValue(i);
            this.var.SetValue(value);
            cmds.execute();
        }

    }
}
