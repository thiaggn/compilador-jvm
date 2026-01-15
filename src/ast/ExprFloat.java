package ast;

import parser.Token;

public class ExprFloat extends Expr 
{
	public double valor;
	public Token  token;

	public ExprFloat(Token tok)
	{
		super(tok.beginLine, tok.endColumn, Tipo.Float);
		this.token = tok;
		this.valor = Double.parseDouble(tok.image);
	}

	@Override
	public void print()
	{
		Print.valorDeCampo(this.valor + " (float)");
	}
}
