package ast;

import parser.Token;

public class ExprFloat implements Expr 
{
	public double valor;
	public Token token;
	public Posicao posicao;

	public ExprFloat(Token tok)
	{
		this.token = tok;
		this.valor = Double.parseDouble(tok.image);
		this.posicao = new Posicao(tok);
	}

	@Override
	public void print()
	{
		Print.valorDeCampo(this.valor + " (float)");
	}

	@Override
	public Posicao obterPosicao() 
	{
		return this.posicao;
	}
}
