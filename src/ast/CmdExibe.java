package ast;

import parser.Token;

public class CmdExibe extends Cmd
{
	public Expr valor;

	public CmdExibe(Expr valor, Token tokExibe)
	{
		super(tokExibe.beginLine, tokExibe.beginColumn);
		this.valor = valor;
	}

	@Override
	public void print()
	{
		Print.abreObjeto("CmdExibe");
		Print.campoComObjeto("expr", this.valor);
		Print.fechaObjeto();
	}
}