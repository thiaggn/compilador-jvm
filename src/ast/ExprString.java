package ast;

import parser.Token;

public class ExprString extends Expr 
{
	public String valor;
	public Token token;
	public Posicao posicao;

	public ExprString(Token tok) 
	{
		super(tok.beginLine, tok.beginColumn, Tipo.String);
		this.token = tok;
		this.valor = tok.image.substring(1, tok.image.length() - 1);
		this.posicao = new Posicao(tok);
	}

	@Override
	public void print() 
	{
		Print.valorDeCampo("\"" + this.valor + "\"");
	}
}
