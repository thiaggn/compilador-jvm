

import java.util.HashMap;


public class Escopo
{
	HashMap<String, ast.Simbolo> tabela;
	public int                   id;

	static int proximoId = 0;

	public Escopo()
	{
		this.tabela = new HashMap<>();
		this.id = proximoId++;
	}

	public boolean contem(String nome)
	{
		return this.tabela.containsKey(nome);
	}

	public ast.Simbolo resolver(String nome)
	{
		return this.tabela.get(nome);
	}

	public void declarar(ast.Simbolo simbolo)
	{
		simbolo.idEscopo = id;
		this.tabela.put(simbolo.nome, simbolo);
	}

	public void redeclarar(String nome, ast.Simbolo simbolo)
	{
		simbolo.idEscopo = this.id;
		this.tabela.replace(nome, simbolo);
	}
}
