package interpreter.expr;

import interpreter.value.Value;
import interpreter.value.TableValue;


public class AccessExpr extends SetExpr{
    private Expr base;
    private Expr index;

    public AccessExpr(int line, Expr base, Expr index){
        super(line);
        this.base = base;
        this.index = index;
    }

    @Override
    public Value<?> expr(){
        TableValue base_ = (TableValue) this.base.expr();
        Value<?> index_ = this.index.expr();
        return base_.value().get(index_); 
    }
    
    @Override
    public void SetValue(Value<?> value){
        TableValue base_ = (TableValue) base.expr();
        Value<?> index_= index.expr();
        base_.value().put(index_, value);
    }
}

