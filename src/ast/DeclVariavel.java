package ast;


import ast.simbolo.SimboloNome;

public class DeclVariavel implements Expr
{
	public Id 		   identificador;
	public Id 		   tipo;
	public Expr 	   exprInicial;
	public SimboloNome simbolo;

	public DeclVariavel(Id tipo, Id nome, Expr valor)
	{
		this.tipo = tipo;
		this.identificador = nome;
		this.exprInicial = valor;
	}

	public DeclVariavel(Id tipo, Id nome)
	{
		this.tipo = tipo;
		this.identificador = nome;
		this.exprInicial = null;
	}

	@Override
	public void print()
	{
		Print.abreObjeto("DeclVariável");
		Print.campoComValor("nome", this.identificador.nome);
		Print.campoComValor("referencia", String.format("%d", this.simbolo.id));
		Print.campoComValor("tipo", this.tipo.nome);
		if (this.exprInicial != null) {
			Print.campoComObjeto("valor", this.exprInicial);
		}
		Print.fechaObjeto();
	}

	@Override
	public Posicao obterPosicao()
	{
		return null;
	}
}