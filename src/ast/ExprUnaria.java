package ast;

public class ExprUnaria extends Expr
{
	public Expr     expr;
	public Operador op;
	
	public ExprUnaria(Expr expr, Operador op, parser.Token tokOp)
	{
		super(tokOp.beginLine, tokOp.beginColumn, Tipo.Indeterminado);
		this.expr = expr;
		this.op   = op;
	}

	@Override
	public void print()
	{
		Print.abreObjeto("ExprUnária");
		Print.campoComValor("operador", op.toString());
		Print.campoComObjeto("expr", expr);
		Print.fechaObjeto();
	}
}
