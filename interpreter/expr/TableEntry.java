package interpreter.expr;

public class TableEntry extends TableExpr{
    public Expr key;
    public Expr value;

    public TableEntry(int line){
        super(line);
    }
}
