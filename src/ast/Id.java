package ast;

import parser.Token;

public class Id extends Expr {
	public String nome;

	public Id(Token tok) {
		super(tok.beginLine, tok.beginColumn, Tipo.Indeterminado);
		this.nome = tok.image;
	}

	@Override
	public String toString() {
		return this.nome;
	}

	@Override
	public void print() {
		
	}
}
