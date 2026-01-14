package ast;

import parser.Token;

public class Posicao {
	public int linhaInicio, colunaInicio;
	public int linhaFim, colunaFim;

	public Posicao(Token tok) {
		this.linhaInicio = tok.beginLine;
		this.colunaInicio = tok.beginColumn;
		this.linhaFim = tok.endLine;
		this.colunaFim = tok.endColumn;
	}

	public Posicao(Posicao inicio, Posicao fim) {
		this.linhaInicio = inicio.linhaInicio;
		this.colunaInicio = inicio.colunaInicio;
		this.linhaFim = fim.linhaFim;
		this.colunaFim = fim.colunaFim;
	}
}
