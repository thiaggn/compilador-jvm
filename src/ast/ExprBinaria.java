package ast;

public class ExprBinaria extends Expr
{
	public Operador operador;
	public Expr     esq;
	public Expr     dir;

	public ExprBinaria(Expr esq, Expr dir, Operador op, parser.Token tokOp)
	{
		super(tokOp.beginLine, tokOp.beginColumn, SimboloTipo.Indeterminado);
		this.operador  = op;
		this.esq = esq;
		this.dir = dir;
	}

	@Override
	public void print()
	{
		Print.abreObjeto("ExprBinária");
		Print.campoComValor("tipo", tipo.nome);
		Print.campoComValor("operador", operador.toString());
		Print.campoComObjeto("expr_esq", esq);
		Print.campoComObjeto("expr_dir", dir);
		Print.fechaObjeto();
	}
}
