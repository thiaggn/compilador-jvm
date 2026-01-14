package ast;

public class ExprUnaria implements Expr
{
	public Expr 	   expr;
	public Operador op;
	public Posicao 	   posicao;

	public ExprUnaria(Expr expr, Operador op)
	{
		this.expr    = expr;
		this.op 	 = op;
		this.posicao = expr.obterPosicao();
	}

	@Override
	public void print()
	{
		Print.abreObjeto("ExprUnária");
		Print.campoComValor("operador", op.name());
		Print.campoComObjeto("expr", expr);
		Print.fechaObjeto();
	}

	@Override
	public Posicao obterPosicao()
	{
		return this.posicao;
	}
}
