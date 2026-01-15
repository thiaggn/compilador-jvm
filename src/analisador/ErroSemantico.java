package analisador;


public class ErroSemantico
{
	public int linha;
	public int coluna;
	public String descricao;

	public ErroSemantico(int linha, int coluna, String descricao)
	{
		this.linha = linha;
		this.coluna = coluna;
		this.descricao = descricao;
	}

	public String toString()
	{
		return String.format("\nErro semântico: %s", this.descricao);
	}
}

