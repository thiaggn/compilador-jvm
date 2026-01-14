package ast;

public class ExprTernaria implements Expr
{
	public Expr    exprCondicao;
	public Expr    exprEntao;
	public Expr	   exprSenao;
	public Posicao posicao;

	public ExprTernaria(Expr exprCondicao, Expr exprEntao, Expr exprSenao)
	{
		this.exprCondicao = exprCondicao;
		this.exprEntao    = exprEntao;
		this.exprSenao    = exprSenao;
		this.posicao      = new Posicao(exprCondicao.obterPosicao(), exprEntao.obterPosicao());
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

	@Override
	public Posicao obterPosicao()
	{
		return this.posicao;
	}
}
