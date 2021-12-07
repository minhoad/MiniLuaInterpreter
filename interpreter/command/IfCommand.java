package interpreter.command;

import interpreter.expr.Expr;


public class IfCommand extends Command{
    private Expr expr;
    private Command thenCmds;
    private Command elseCmds;

    public IfCommand(int line, Command thenCmds, Expr expr){
        super(line);
        this.expr = expr;
        this.thenCmds = thenCmds;
    }

    public void setElseCommands(Command elseCmds){
        this.elseCmds = elseCmds;
    }

    @Override
    public void execute(){
        //TODO
    }
}
