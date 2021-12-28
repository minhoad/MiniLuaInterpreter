package interpreter.command;

import interpreter.expr.Expr;
import interpreter.value.Value;

public class WhileCommand extends Command{
    private Expr expr;
    private Command cmds;

    public WhileCommand(int line, Expr expr, Command cmds){
        super(line);
        this.expr = expr;
        this.cmds = cmds;
    }

    @Override
    public void execute(){
        // do{
        //     Value<?> v = expr.expr();
        //     if(v != null && v.eval())cmds.execute();
        //     else break;
        // }while(true);//Para sempre atualizar o valor de V (infinito)
        Value<?> v;
        while((v = expr.expr()) != null && v.eval())cmds.execute();
    }
}
