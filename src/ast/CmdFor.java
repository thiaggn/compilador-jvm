package ast;

import parser.Token;

public final class CmdFor extends Cmd
{
	public No    exprInicial;
	public Expr  exprCond;
	public Expr  exprUpdate;
	public Bloco bloco;

	public CmdFor (No inicio, Expr condicao, Expr atualizacao, Bloco bloco, Token tokFor)
	{
		super(tokFor.beginLine, tokFor.beginColumn);
		this.exprInicial = inicio;
		this.exprCond = condicao;
		this.exprUpdate = atualizacao;
		this.bloco = bloco;
	}

	@Override
	public void print()
	{
		Print.abreObjeto("CmdFor");
		Print.campoComObjeto("expr_inicial", this.exprInicial);
		Print.campoComObjeto("expr_condicional", this.exprCond);
		Print.campoComObjeto("expr_update", this.exprUpdate);
		Print.campoComObjeto("bloco", this.bloco);
		Print.fechaObjeto();
	}
}