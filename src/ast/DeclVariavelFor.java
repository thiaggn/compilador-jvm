package ast;

public class DeclVariavelFor implements Debug
{
	public Simbolo simbolo;
	public Id      identificador;
	public Expr    exprInicial;

	public DeclVariavelFor(Id identificador, Expr exprInicial)
	{
		this.identificador = identificador;
		this.exprInicial = exprInicial;
	}

	@Override
	public void print() {
		Print.abreObjeto("DeclVariavelFor");
		Print.campoComValor("nome", this.identificador.nome);
		Print.campoComValor("ref", String.format("%d", this.simbolo.ref));
		Print.campoComValor("tipo", String.format("%s", this.simbolo.tipo.nome));
		Print.campoComObjeto("valor", this.exprInicial);
		Print.fechaObjeto();
	}
}
