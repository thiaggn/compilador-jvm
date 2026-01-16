package ast;

import parser.Token;

public class ExprString extends Expr 
{
	public String valor;
	public Token  token;

	public ExprString(Token tok) 
	{
		super(tok.beginLine, tok.beginColumn, Tipo.String);
		this.token = tok;
		this.valor = tok.image.substring(1, tok.image.length() - 1);
	}

	@Override
	public void print() 
	{
		Print.valorDeCampo(String.format("\u001B[32m\"%s\"\u001B[0m", this.valor));
	}
}
