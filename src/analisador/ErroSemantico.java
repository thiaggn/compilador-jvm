package analisador;

import ast.No;
import ast.Posicao;

public class ErroSemantico
{
	public Posicao posicao;
	public String descricao;

	public ErroSemantico(No no, String descricao)
	{
		this.posicao = no.obterPosicao();
		this.descricao = descricao;
	}

	public ErroSemantico(Posicao posicao, String descricao)
	{
		this.posicao = posicao;
		this.descricao = descricao;
	}


	public String toString()
	{
		return String.format("\nErro semântico: %s", this.descricao);
	}
}

