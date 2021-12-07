package interpreter.command;

import interpreter.expr.Expr;
import interpreter.expr.Variable;

public class GenericForCommand extends Command{
    private Variable var1;
    private Variable var2;
    private Expr expr;
    private Command cmds;

    public GenericForCommand(int line, Variable var1, Variable var2, Expr expr, Command cmds){
        super(line);
        this.var1 = var1;
        this.var2 = var2;
        this.expr = expr;
        this.cmds = cmds;
    }

    @Override
    public void execute(){

    }
}
