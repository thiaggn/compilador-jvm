package ast;

public class Id extends Expr
{
	public String nome;

	public Id(parser.Token tok)
	{
		super(tok.beginLine, tok.beginColumn, SimboloTipo.Indeterminado);
		this.nome = tok.image;
	}

	public Id(String nome, int linha, int coluna)
	{
		super(linha, coluna, SimboloTipo.Indeterminado);
		this.nome = nome;
	}

	public Id(String nome)
	{
		super(0, 0, SimboloTipo.Indeterminado);
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
