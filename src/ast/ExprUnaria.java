package ast;

public class ExprUnaria extends Expr
{
	public Expr     expr;
	public Operador operador;
	
	public ExprUnaria(Expr expr, Operador op, parser.Token tokOp)
	{
		super(tokOp.beginLine, tokOp.beginColumn, SimboloTipo.Indeterminado);
		this.expr = expr;
		this.operador   = op;
	}

	@Override
	public void print()
	{
		Print.abreObjeto("ExprUnária");
		Print.campoComValor("tipo", tipo.nome);
		Print.campoComValor("operador", operador.toString());
		Print.campoComObjeto("expr", expr);
		Print.fechaObjeto();
	}
}
