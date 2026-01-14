package analisador;

import java.util.Deque;

import ast.simbolo.SimboloNome;

public class PilhaDeEscopos {
	Deque<Escopo> escopos;

	public PilhaDeEscopos() {
		escopos = new java.util.ArrayDeque<>();
	}

	public SimboloNome resolver(String nome) {
		for (Escopo escopo : this.escopos) {
			if (escopo.contem(nome)) {
				return escopo.resolver(nome);
			}
		}
		return null;
	}

	public void substituir(String nome, SimboloNome simbolo) {
		for (Escopo escopo : this.escopos) {
			if (escopo.contem(nome)) {
				escopo.substituir(nome, simbolo);
			}
		}
	}

	public Escopo atual() {
		return this.escopos.peek();
	}

	public void abrirEscopo() {
		escopos.push(new Escopo());
	}

	public void fecharEscopo() {
		escopos.pop();
	}

	public void declarar(SimboloNome simbolo) {
		this.atual().declarar(simbolo);
	}
}
