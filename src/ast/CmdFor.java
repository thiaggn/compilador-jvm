package ast;

import parser.Token;

public final class CmdFor extends Cmd
{
	public Id    varInicial;  
	public Expr  exprInicial;
	public Expr  exprTeste;
	public Expr  exprIterativa;
	public Bloco bloco;

	public CmdFor (Id varInicial, Expr exprInicial, Expr exprTeste, Expr exprIterativa, Bloco bloco, Token tokFor)
	{
		super(tokFor.beginLine, tokFor.beginColumn);
		this.varInicial = varInicial;
		this.exprInicial = exprInicial;
		this.exprTeste = exprTeste;
		this.exprIterativa = exprIterativa;
		this.bloco = bloco;
	}

	@Override
	public void print()
	{
		Print.abreObjeto("CmdFor");
		Print.campoComObjeto("expr_inicial", this.exprInicial);
		Print.campoComObjeto("expr_condicional", this.exprTeste);
		Print.campoComObjeto("expr_update", this.exprIterativa);
		Print.campoComObjeto("bloco", this.bloco);
		Print.fechaObjeto();
	}
}