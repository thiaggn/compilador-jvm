package ast;

public class ExprBinaria implements Expr
{
	public Expr 	   esq;
	public Expr 	   dir;
	public Operador op;
	public Posicao 		posicao;

	public ExprBinaria(Expr esq, Expr dir, Operador op)
	{
		this.esq = esq;
		this.dir = dir;
		this.op = op;
		this.posicao = new Posicao(esq.obterPosicao(), dir.obterPosicao());
	}

	@Override
	public void print()
	{
		Print.abreObjeto("ExprBinária");
		Print.campoComValor("operador", op.name());
		Print.campoComObjeto("expr_esq", esq);
		Print.campoComObjeto("expr_dir", dir);
		Print.fechaObjeto();
	}

	@Override
	public Posicao obterPosicao()
	{
		return this.posicao;
	}
}
