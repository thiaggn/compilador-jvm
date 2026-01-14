package analisador;

import java.util.ArrayList;

import ast.No;

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
			System.out.printf(
				"\nerro semântico em %d:%d:\n --> %s\n", 
				err.posicao.linhaInicio,err.posicao.colunaInicio,
				err.descricao
			);
		}
	}

	public boolean ok()
	{
		return this.erros.size() == 0;
	}

	public void add(No no, String msg)
	{
		this.erros.add(new ErroSemantico(no, msg));
	}
}
