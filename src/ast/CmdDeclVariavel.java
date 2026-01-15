package ast;


import ast.simbolo.Simbolo;

public class CmdDeclVariavel extends Cmd
{
	public Id 		   identificador;
	public Id 		   tipo;
	public Expr 	   exprInicial;
	public Simbolo simbolo;

	public CmdDeclVariavel(Id tipo, Id nome, Expr valor)
	{
		super(tipo.linha, tipo.coluna);
		this.tipo = tipo;
		this.identificador = nome;
		this.exprInicial = valor;
	}

	public CmdDeclVariavel(Id tipo, Id nome)
	{
		super(tipo.linha, tipo.coluna);
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
}