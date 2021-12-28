package interpreter.command;

import interpreter.expr.Expr;
import interpreter.value.Value;

public class RepeatCommand extends Command{
    private Command cmds;
    private Expr expr;

    public RepeatCommand(int line, Command cmds, Expr expr){
        super(line);
        this.cmds = cmds;
        this.expr = expr;
    }

    @Override
    public void execute(){
        Value<?> value = expr.expr();
        while (value != null && ! value.eval())cmds.execute();
    }
}
