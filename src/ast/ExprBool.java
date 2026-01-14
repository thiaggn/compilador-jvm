package ast;

import parser.Token;

public final class ExprBool implements Expr
{
	public boolean valor;
	public Token token;
	public Posicao posicao;

	public ExprBool(Token token)
	{
		this.token = token;
		this.valor = Boolean.parseBoolean(token.image);
		this.posicao = new Posicao(token);
	}

	@Override
	public void print()
	{
		Print.valorDeCampo(this.valor + " (bool)");
	}

	@Override
	public Posicao obterPosicao()
	{
		return this.posicao;
	}
}
