package ast;

import parser.Token;

public class ExprString implements Expr 
{
	public String valor;
	public Token token;
	public Posicao posicao;

	public ExprString(Token tok) 
	{
		this.token = tok;
		this.valor = tok.image.substring(1, tok.image.length() - 1);
		this.posicao = new Posicao(tok);
	}

	@Override
	public void print() 
	{
		Print.valorDeCampo("\"" + this.valor + "\"");
	}

	@Override
	public Posicao obterPosicao() 
	{
		return this.posicao;
	}
}
