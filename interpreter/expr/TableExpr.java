package interpreter.expr;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import interpreter.value.Value;
import interpreter.value.TableValue;
import interpreter.value.NumberValue;


public class TableExpr extends Expr{
    private List<TableEntry> table = new ArrayList<TableEntry>();

    public TableExpr(int line){
        super(line);
    }
    
    public void addEntry(TableEntry aux){
        this.table.add(aux);
    }

    @Override
    public Value<?> expr(){
        Map<Value<?>, Value<?>> aux = new HashMap<Value<?>, Value<?>>();
        int seq = 1;
        for(TableEntry a : table){
            Expr key = a.key;
            Expr value = a.value;

            if (key == null) {
                aux.put(new NumberValue(Double.valueOf(seq)),value.expr());
                seq++;
            } else {
                aux.put(key.expr(), value.expr());
                //sera se coloca seq++?
                seq++;
            }
        }
        return (new TableValue(aux));
    }
}
