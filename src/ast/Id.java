package ast;

import parser.Token;

public class Id extends Expr
{
	public String nome;

	public Id(Token tok)
	{
		super(tok.beginLine, tok.beginColumn, Tipo.Indeterminado);
		this.nome = tok.image;
	}

	public Id(String nome, int linha, int coluna)
	{
		super(linha, coluna, Tipo.Indeterminado);
		this.nome = nome;
	}

	public Id(String nome)
	{
		super(0, 0, Tipo.Indeterminado);
		this.nome = nome;
	}

	@Override
	public String toString() {
		return this.nome;
	}

	@Override
	public void print() {
		
	}
}
