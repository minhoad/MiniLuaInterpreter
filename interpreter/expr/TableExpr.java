package interpreter.expr;

import java.util.ArrayList;
import java.util.List;

public class TableExpr extends Expr{
    
    private static class TableEntry{
        public Expr key;
        public Expr value;
    }

    private List<TableEntry> table = new ArrayList<TableEntry>();

    public TableExpr(int line){
        super(line);
    }
    
    public void addEntry(Expr key, Expr value){
        TableEntry aux = new TableEntry();
        aux.key = key;
        aux.value = value;
        this.table.add(aux);
    }

    @Override
    public Value<?> expr(){

    }
}
