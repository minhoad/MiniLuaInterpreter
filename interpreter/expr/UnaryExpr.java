package interpreter.expr;

import interpreter.value.BooleanValue;
import interpreter.value.NumberValue;
import interpreter.value.StringValue;
import interpreter.value.TableValue;
import interpreter.value.Value;
import interpreter.util.Utils;
import java.util.Scanner;

public class UnaryExpr extends Expr{
    private Expr expr;
    private UnaryOp op;
    private static Scanner in;

    static {
        in = new Scanner(System.in);
    }

    public UnaryExpr(int line, Expr expr, UnaryOp op){
        super(line);
        this.expr = expr;
        this.op = op;
    }

    @Override
    public Value<?> expr(){
        Value<?> v = (expr != null) ? expr.expr() : null;

        Value<?> ret = null;
        switch (op) {
            case NegOp:
                ret = negOp(v);
                break;
            case SizeOp:
                ret = sizeOp(v);
                break;
            case NotOp:
                ret = notOp(v);
                break;
            case ReadOp:
                ret = readOp(v);
                break;
            case ToNumberOp:
                ret = toNumberOp(v);
                break;
            case ToStringOp:
                ret = toStringOp(v);
                break;
            default:
                Utils.abort(super.getLine());
        }

        return ret;
    }

    private Value<?> negOp(Value<?> v) {
        Value<?> ret = null;
        if (v instanceof NumberValue) {
            NumberValue nv = (NumberValue) v;
            Double d = -nv.value();
            ret = new NumberValue(d);
        } else if (v instanceof StringValue) {
            StringValue sv = (StringValue) v;
            String tmp = sv.value();

            try {
                Double d = -Double.valueOf(tmp);
                ret = new NumberValue(d);
            } catch (Exception e) {
                Utils.abort(super.getLine());
            }
        } else {
            Utils.abort(super.getLine());
        }

        return ret;
    }

    private Value<?> notOp(Value<?> v) {
        boolean b = (v == null || !v.eval());
        BooleanValue bv = new BooleanValue(b);
        return bv;
    }

    private Value<?> sizeOp(Value<?> v) {
        if (v instanceof TableValue) {
            TableValue tv = (TableValue) v;
            int size = tv.value().size();
            
            NumberValue nv = new NumberValue(Double.valueOf(size));
            return nv;
        } else {
            Utils.abort(super.getLine());
            return null;
        }
    }

    private Value<?> readOp(Value<?> v) {
        if (v instanceof StringValue) {
            StringValue msg = (StringValue) v;
            System.out.print(msg.value());
        } else {
            Utils.abort(super.getLine());
        }
    
        String tmp = in.nextLine().trim();
        StringValue sv = new StringValue(tmp);
        return sv;
    }

    private Value<?> toNumberOp(Value<?> v){
        if(!(v instanceof StringValue)){
            Utils.abort(super.getLine());
       }
       else {
            String string = (String) v.value();
            return new NumberValue(Double.valueOf(string));
       } 
        return null;
    }

    private Value<?> toStringOp(Value<?> v){
        if(!(v instanceof NumberValue)){
            Utils.abort(super.getLine());
       }
       else {
            Double number = (Double) v.value();
            String string = Double.toString(number);
            return new StringValue(string);
       } 
        return null;
    }


}
