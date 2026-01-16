package ast;

import parser.Token;

public class ExprChar extends Expr
{
	public char  valor;
	public Token token;

	public ExprChar(Token tok)
	{
		super(tok.beginLine, tok.endColumn, Tipo.Char);
		this.token = tok;
		this.valor = tok.image.substring(1, tok.image.length() - 1).charAt(0);
	}

	@Override
	public void print()
	{
		Print.valorDeCampo(String.format("\u001B[32m\"%c\"\u001B[0m", this.valor));
	}
}
