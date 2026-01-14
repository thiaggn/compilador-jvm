package ast;

import ast.simbolo.SimboloNome;

public class ExprAtribuicao implements Expr {
	public Id destino;
	public Expr exprInicial;
	public SimboloNome simboloDestino;
	public boolean ehDeclaracao;
	public Posicao posicao;
	
	public ExprAtribuicao(Id destino, Expr valor) {
		this.destino = destino;
		this.exprInicial = valor;
		this.simboloDestino = null;
		this.ehDeclaracao = false;
		this.posicao = new Posicao(destino.obterPosicao(), valor.obterPosicao());
	}

	@Override
	public void print() {
		Print.abreObjeto("ExprAtribuição");
		Print.campoComValor("eh_declaração", String.format("%b", this.ehDeclaracao));
		Print.campoComObjeto("destino", this.simboloDestino);
		Print.campoComObjeto("valor", this.exprInicial);
		Print.fechaObjeto();
	}

	@Override
	public Posicao obterPosicao() {
		return this.posicao;
	}
}
