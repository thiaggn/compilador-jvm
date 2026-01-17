package analisador;

import java.util.Deque;

public class PilhaDeEscopos
{
	Deque<Escopo> escopos;

	public PilhaDeEscopos()
	{
		escopos = new java.util.ArrayDeque<>();
	}

	public ast.Simbolo resolver(String nome)
	{
		for (Escopo escopo : this.escopos)
		{
			if (escopo.contem(nome))
			{
				return escopo.resolver(nome);
			}
		}
		return null;
	}

	public void redeclarar(String nome, ast.Simbolo simbolo)
	{
		for (Escopo escopo : this.escopos)
		{
			if (escopo.contem(nome))
			{
				escopo.redeclarar(nome, simbolo);
			}
		}
	}

	public Escopo atual()
	{
		return this.escopos.peek();
	}

	public void abrirEscopo()
	{
		escopos.push(new Escopo());
	}

	public void fecharEscopo()
	{
		escopos.pop();
	}

	public void declarar(ast.Simbolo simbolo)
	{
		this.atual().declarar(simbolo);
	}
}
