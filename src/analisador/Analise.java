package analisador;

import java.util.ArrayList;


/// Contém os erros capturados durante a análise semântica de uma árvore sintática abstrata
public class Analise
{
	ArrayList<ErroSemantico> erros;

	public Analise()
	{
		this.erros = new ArrayList<>();
	}

	public void exibirErros()
	{
		for (ErroSemantico err : this.erros) {
			System.out.printf("\nerro semântico em %d:%d:\n --> %s\n", err.linha, err.coluna, err.descricao);
		}
	}

	public boolean ok()
	{
		return this.erros.size() == 0;
	}

	public void add(int linha, int coluna, String msg)
	{
		this.erros.add(new ErroSemantico(linha, coluna, msg));
	}
}
