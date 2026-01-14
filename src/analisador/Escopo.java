package analisador;

import java.util.HashMap;

import ast.simbolo.SimboloNome;

public class Escopo {
	HashMap<String, SimboloNome> tabela;

	public Escopo() {
		this.tabela = new HashMap<>();
	}

	public boolean contem(String nome) {
		return this.tabela.containsKey(nome);
	}

	public SimboloNome resolver(String nome) {
		return this.tabela.get(nome);
	}

	public void declarar(SimboloNome simbolo) {
		this.tabela.put(simbolo.nome, simbolo);
	}

	public void substituir(String nome, SimboloNome simbolo) {
		this.tabela.replace(nome, simbolo);
	}
}
