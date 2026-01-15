package ast;

import parser.Token;

public class ExprBinaria extends Expr
{
	public Operador op;
	public Expr 	esq;
	public Expr 	dir;

	public ExprBinaria(Expr esq, Expr dir, Operador op, Token tokOp)
	{
		super(tokOp.beginLine, tokOp.beginColumn, Tipo.Indeterminado);
		this.op  = op;
		this.esq = esq;
		this.dir = dir;
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
}
