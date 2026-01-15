package ast;

public abstract class No implements Printavel
{
	public int linha;
	public int coluna;

	protected No(int linha, int coluna)
	{
		this.linha = linha;
		this.coluna = coluna;
	}
} 