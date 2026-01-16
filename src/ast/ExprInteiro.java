package ast;

import parser.Token;

public class ExprInteiro extends Expr 
{
	public int valor;
	public Token token;

	public ExprInteiro(Token tok)
	{
		super(tok.beginLine, tok.beginColumn, Tipo.Inteiro);
		this.token = tok;
		this.valor = Integer.parseInt(tok.image);
	}

	@Override
	public void print()
	{
		Print.valorDeCampo(String.format("\u001B[95m%d\u001B[0m", this.valor, this.tipo.nome));
	}
}
