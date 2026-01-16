package ast;

import parser.Token;

public class CmdWhile extends Cmd
{
	public Expr  exprCondicao;
	public Bloco bloco;

	public CmdWhile(Expr condicao, Bloco bloco, Token tokWhile)
	{
		super(tokWhile.beginLine, tokWhile.beginColumn);
		this.exprCondicao = condicao;
		this.bloco        = bloco;
	}

	@Override
	public void print()
	{
		Print.abreObjeto("CmdWhile");
		Print.campoComObjeto("expr_cond", this.exprCondicao);
		Print.campoComObjeto("bloco", this.bloco);
		Print.fechaObjeto();
	}
}
