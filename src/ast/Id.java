package ast;

import parser.Token;

public class Id implements No {
	public String nome;
	public Posicao posicao;

	public Id(Token tok) {
		this.posicao = new Posicao(tok);
		this.nome = tok.image;
	}

	@Override
	public String toString() {
		return this.nome;
	}

	public Posicao obterPosicao() {
		return this.posicao;
	}

	@Override
	public void print() {
		
	}
}
