package ast;

import ast.simbolo.SimboloNome;

public class ExprAtribuicao extends Expr
{
	public Id 		   destino;
	public Expr 	   exprInicial;
	public SimboloNome simboloDestino;
	public boolean	   ehDeclaracao;

	public ExprAtribuicao(Id destino, Expr valor)
	{
		super(destino.linha, destino.coluna, Tipo.Indeterminado);
		this.destino = destino;
		this.exprInicial = valor;
		this.simboloDestino = null;
		this.ehDeclaracao = false;
	}

	@Override
	public void print()
	{
		Print.abreObjeto("ExprAtribuição");
		Print.campoComValor("eh_declaração", String.format("%b", this.ehDeclaracao));
		Print.campoComObjeto("destino", this.simboloDestino);
		Print.campoComObjeto("valor", this.exprInicial);
		Print.fechaObjeto();
	}
}
