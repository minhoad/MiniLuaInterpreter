package interpreter.command;

import java.util.Map;

import interpreter.expr.Expr;
import interpreter.expr.Variable;
import interpreter.value.Value;
import interpreter.value.TableValue;
import interpreter.value.NumberValue;
import interpreter.value.StringValue;
import interpreter.util.Utils;

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
        //var1 key var2 value in expr (table)

        Value<?> value = expr.expr();
        if(!(value instanceof TableValue)){ // perguntar se usa com string
            Utils.abort(super.getLine());
        }
        else {
            TableValue value_ = (TableValue) value;
            Map<Value<?>, Value<?>> map = value_.value();
            if(var2!=null){
                for(var values : map.entrySet()){
                    if(!((values.getKey()) instanceof NumberValue) || !((values.getKey()) instanceof StringValue)){
                        Utils.abort(super.getLine());
                    }
                    else {
                        var1.SetValue(values.getKey());
                        var2.SetValue(values.getValue());
                        cmds.execute();
                    }
                }
            }else{
                for(Value<?> key : value_.value().keySet()){
                    var1.SetValue(key);
                    cmds.execute();
                }
            }
        }
    }
}
