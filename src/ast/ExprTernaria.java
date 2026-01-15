package ast;

public class ExprTernaria extends Expr
{
	public Expr    exprCondicao;
	public Expr    exprEntao;
	public Expr	   exprSenao;

	public ExprTernaria(Expr exprCondicao, Expr exprEntao, Expr exprSenao)
	{
		super(exprCondicao.linha, exprCondicao.coluna, Tipo.Indeterminado);
		this.exprCondicao = exprCondicao;
		this.exprEntao    = exprEntao;
		this.exprSenao    = exprSenao;
	}

	@Override
	public void print()
	{
		Print.abreObjeto("ExprTernária");
		Print.campoComObjeto("condição", this.exprCondicao);
		Print.campoComObjeto("expr_entao", this.exprEntao);
		Print.campoComObjeto("expr_senao", this.exprSenao);
		Print.fechaObjeto();
	}
}
