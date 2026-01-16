package ast;

import parser.Token;

public final class ExprBool extends Expr
{
	public boolean valor;
	public Token token;

	public ExprBool(Token token, boolean valor)
	{
		super(token.beginLine, token.endColumn, Tipo.Bool);
		this.token = token;
		this.valor = valor;
	}

	@Override
	public void print()
	{
		Print.valorDeCampo(this.valor + " (bool)");
	}
}
