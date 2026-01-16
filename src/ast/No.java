package ast;

public abstract class No implements Debug
{
	public final int linha;
	public final int coluna;

	protected No(int linha, int coluna)
	{
		this.linha = linha;
		this.coluna = coluna;
	}
} 