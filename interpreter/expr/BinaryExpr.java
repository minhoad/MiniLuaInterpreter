package interpreter.expr;

import interpreter.value.BooleanValue;
import interpreter.value.Value;
import interpreter.value.NumberValue;
import interpreter.value.TableValue;
import interpreter.value.StringValue;

import java.util.Map;

import interpreter.util.Utils;

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
        Value<?> leftValue = left.expr();
        Value<?> rightValue = right.expr();
        
        if(op == BinaryOp.AndOp){
            if((leftValue instanceof BooleanValue && rightValue instanceof BooleanValue)){
                BooleanValue leftValue_ = (BooleanValue) leftValue;
                BooleanValue rightValue_ = (BooleanValue) rightValue;
               return new BooleanValue(leftValue_.eval() && rightValue_.eval());
            }else Utils.abort(super.getLine());
        }
        else if(op == BinaryOp.OrOp){
            if((leftValue instanceof BooleanValue && rightValue instanceof BooleanValue)){
                BooleanValue leftValue_ = (BooleanValue) leftValue;
                BooleanValue rightValue_ = (BooleanValue) rightValue;
                return new BooleanValue(leftValue_.eval() || rightValue_.eval());
            }else Utils.abort(super.getLine());
        }
        else if(op == BinaryOp.EqualOp){
            if(leftValue instanceof NumberValue && rightValue instanceof NumberValue){
                NumberValue leftValue_ = (NumberValue) leftValue;
                NumberValue rightValue_ = (NumberValue) rightValue;
                return new BooleanValue(leftValue_.equals(rightValue_));
            }else if(leftValue instanceof BooleanValue && rightValue instanceof BooleanValue){
                BooleanValue leftValue_ = (BooleanValue) leftValue;
                BooleanValue rightValue_ = (BooleanValue) rightValue;
                return new BooleanValue(leftValue_.equals(rightValue_));
            }else if(leftValue instanceof StringValue && rightValue instanceof StringValue){
                StringValue leftValue_ = (StringValue) leftValue;
                StringValue rightValue_ = (StringValue) rightValue;
                return new BooleanValue(leftValue_.equals(rightValue_));
            }else if(leftValue instanceof TableValue && rightValue instanceof TableValue){
                TableValue leftValue_ = (TableValue) leftValue;
                TableValue rightValue_ = (TableValue) rightValue;
                return new BooleanValue(leftValue_.equals(rightValue_));
            }
        }
        else if(op == BinaryOp.NotEqualOp){
            if(leftValue instanceof NumberValue && rightValue instanceof NumberValue){
                NumberValue leftValue_ = (NumberValue) leftValue;
                NumberValue rightValue_ = (NumberValue) rightValue;
                return new BooleanValue(!(leftValue_.equals(rightValue_)));
            }else if(leftValue instanceof BooleanValue && rightValue instanceof BooleanValue){
                BooleanValue leftValue_ = (BooleanValue) leftValue;
                BooleanValue rightValue_ = (BooleanValue) rightValue;
                return new BooleanValue(!(leftValue_.equals(rightValue_)));
            }else if(leftValue instanceof StringValue && rightValue instanceof StringValue){
                StringValue leftValue_ = (StringValue) leftValue;
                StringValue rightValue_ = (StringValue) rightValue;
                return new BooleanValue(!(leftValue_.equals(rightValue_)));
            }else if(leftValue instanceof TableValue && rightValue instanceof TableValue){
                TableValue leftValue_ = (TableValue) leftValue;
                TableValue rightValue_ = (TableValue) rightValue;
                return new BooleanValue(!(leftValue_.equals(rightValue_)));
            }
        }
        else if(op == BinaryOp.LowerThanOp){
            if((leftValue instanceof NumberValue && rightValue instanceof NumberValue)){
                NumberValue leftValue_ = (NumberValue) leftValue;
                NumberValue rightValue_ = (NumberValue) rightValue;
                return new BooleanValue(rightValue_.value() < leftValue_.value());
            }else if(leftValue instanceof StringValue && rightValue instanceof StringValue){
                StringValue leftValue_ = (StringValue) leftValue;
                StringValue rightValue_ = (StringValue) rightValue;
                return new BooleanValue(rightValue_.value().length() < leftValue_.value().length());
            }else if(leftValue instanceof TableValue && rightValue instanceof TableValue){
                TableValue leftValue_ = (TableValue) leftValue;
                TableValue rightValue_ = (TableValue) rightValue;
                return new BooleanValue(rightValue_.value().size() < leftValue_.value().size());
            }else Utils.abort(super.getLine());
        }else if(op == BinaryOp.LowerEqualOp){
            if((leftValue instanceof NumberValue && rightValue instanceof NumberValue)){
                NumberValue leftValue_ = (NumberValue) leftValue;
                NumberValue rightValue_ = (NumberValue) rightValue;
                return new BooleanValue(rightValue_.value() <= leftValue_.value());
            }else if(leftValue instanceof StringValue && rightValue instanceof StringValue){
                StringValue leftValue_ = (StringValue) leftValue;
                StringValue rightValue_ = (StringValue) rightValue;
                return new BooleanValue(rightValue_.value().length() <= leftValue_.value().length());
            }else if(leftValue instanceof TableValue && rightValue instanceof TableValue){
                TableValue leftValue_ = (TableValue) leftValue;
                TableValue rightValue_ = (TableValue) rightValue;
                return new BooleanValue(rightValue_.value().size() <= leftValue_.value().size());
            }else Utils.abort(super.getLine());
        }
        else if(op==BinaryOp.GreaterThanOp){
            if((leftValue instanceof NumberValue && rightValue instanceof NumberValue)){
                NumberValue leftValue_ = (NumberValue) leftValue;
                NumberValue rightValue_ = (NumberValue) rightValue;
                return new BooleanValue(rightValue_.value() > leftValue_.value());
            }else if(leftValue instanceof StringValue && rightValue instanceof StringValue){
                StringValue leftValue_ = (StringValue) leftValue;
                StringValue rightValue_ = (StringValue) rightValue;
                return new BooleanValue(rightValue_.value().length() > leftValue_.value().length());
            }else if(leftValue instanceof TableValue && rightValue instanceof TableValue){
                TableValue leftValue_ = (TableValue) leftValue;
                TableValue rightValue_ = (TableValue) rightValue;
                return new BooleanValue(rightValue_.value().size() > leftValue_.value().size());
            }else Utils.abort(super.getLine());
        }
        else if(op == BinaryOp.GreaterEqualOp){
            if((leftValue instanceof NumberValue && rightValue instanceof NumberValue)){
                NumberValue leftValue_ = (NumberValue) leftValue;
                NumberValue rightValue_ = (NumberValue) rightValue;
                return new BooleanValue(rightValue_.value() >= leftValue_.value());
            }else if(leftValue instanceof StringValue && rightValue instanceof StringValue){
                StringValue leftValue_ = (StringValue) leftValue;
                StringValue rightValue_ = (StringValue) rightValue;
                return new BooleanValue(rightValue_.value().length() >= leftValue_.value().length());
            }else if(leftValue instanceof TableValue && rightValue instanceof TableValue){
                TableValue leftValue_ = (TableValue) leftValue;
                TableValue rightValue_ = (TableValue) rightValue;
                return new BooleanValue(rightValue_.value().size() >= leftValue_.value().size());
            }else Utils.abort(super.getLine());
        }
        else if(op==BinaryOp.ConcatOp){
            if(leftValue instanceof StringValue && rightValue instanceof StringValue){
                StringValue leftValue_ = (StringValue) leftValue;
                StringValue rightValue_ = (StringValue) rightValue;
                return new StringValue(leftValue_.toString().concat(rightValue_.toString()));
            }else if(leftValue instanceof TableValue && rightValue instanceof TableValue){
                TableValue leftValue_ = (TableValue) leftValue;
                TableValue rightValue_ = (TableValue) rightValue;
                Map<Value<?>,Value<?>> mapleftValue = leftValue_.value();
                Map<Value<?>,Value<?>> maprightValue = rightValue_.value();
                Map<Value<?>,Value<?>> aux = null; // inicializar com null?
                for (Value<?> key : mapleftValue.keySet()) {
                   aux.put(key, mapleftValue.get(key));
                }
                for (Value<?> key : maprightValue.keySet()) {
                    aux.put(key, maprightValue.get(key));
                }
                return new TableValue(aux);
            }else Utils.abort(super.getLine());
        }
        else if(op == BinaryOp.AddOp){
            if(!(leftValue instanceof NumberValue && rightValue instanceof NumberValue)){
                Utils.abort(super.getLine());
            }else{
                NumberValue leftValue_ = (NumberValue) leftValue;
                NumberValue rightValue_ = (NumberValue) rightValue;
                Double doubleleftValue = leftValue_.value();
                Double doublerightValue = rightValue_.value();
                return new NumberValue(doublerightValue + doubleleftValue);
            }
        }
        else if(op == BinaryOp.SubOp){
            if(!(leftValue instanceof NumberValue && rightValue instanceof NumberValue)){
                Utils.abort(super.getLine());
            }else{
                NumberValue leftValue_ = (NumberValue) leftValue;
                NumberValue rightValue_ = (NumberValue) rightValue;
                Double doubleleftValue = leftValue_.value();
                Double doublerightValue = rightValue_.value();
                return new NumberValue(doublerightValue - doubleleftValue);
            }
            // COLOCAR COM STRING? OU OUTROS
        }
        else if(op == BinaryOp.MulOp){
            if(!(leftValue instanceof NumberValue && rightValue instanceof NumberValue)){
                Utils.abort(super.getLine());
            }else{
                NumberValue leftValue_ = (NumberValue) leftValue;
                NumberValue rightValue_ = (NumberValue) rightValue;
                Double doubleleftValue = leftValue_.value();
                Double doublerightValue = rightValue_.value();
                return new NumberValue(doublerightValue * doubleleftValue);
            }
        }
        else if(op == BinaryOp.DivOp){
            if(!(leftValue instanceof NumberValue && rightValue instanceof NumberValue)){
                Utils.abort(super.getLine());
            }else{
                NumberValue leftValue_ = (NumberValue) leftValue;
                NumberValue rightValue_ = (NumberValue) rightValue;
                Double doubleleftValue = leftValue_.value();
                Double doublerightValue = rightValue_.value();
                return new NumberValue(doublerightValue / doubleleftValue);
            }
        }
        else{ //if(op == BinaryOp.ModOp){
            if(!(leftValue instanceof NumberValue && rightValue instanceof NumberValue)){
                Utils.abort(super.getLine());
            }else{
                NumberValue leftValue_ = (NumberValue) leftValue;
                NumberValue rightValue_ = (NumberValue) rightValue;
                Double doubleleftValue = leftValue_.value();
                Double doublerightValue = rightValue_.value();
                return new NumberValue(doublerightValue % doubleleftValue);
            }
        }
        return null;
    }
}
