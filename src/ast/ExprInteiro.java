package ast;

import parser.Token;

public class ExprInteiro implements Expr 
{
	public int valor;
	public Token token;
	public Posicao posicao;

	public ExprInteiro(Token tok)
	{
		this.token = tok;
		this.valor = Integer.parseInt(tok.image);
		this.posicao = new Posicao(tok);
	}

	@Override
	public void print()
	{
		Print.valorDeCampo(this.valor + " (int)");
	}

	@Override
	public Posicao obterPosicao() 
	{
		return this.posicao;
	}
}
