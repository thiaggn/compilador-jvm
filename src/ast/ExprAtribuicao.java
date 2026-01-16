package ast;

public class ExprAtribuicao extends Expr
{
	public Id 		   destino;
	public Expr 	   exprInicial;
	public Simbolo	   simboloDestino;

	public ExprAtribuicao(Id destino, Expr valor)
	{
		super(destino.linha, destino.coluna, Tipo.Indeterminado);
		this.destino = destino;
		this.exprInicial = valor;
	}

	@Override
	public void print()
	{
		Print.abreObjeto("ExprAtribuição");
		Print.campoComValor("destino", 
			String.format("%s (%d, %s)", this.simboloDestino.nome, this.simboloDestino.ref, this.simboloDestino.tipo.nome)
		);
		Print.campoComObjeto("valor", this.exprInicial);
		Print.fechaObjeto();
	}
}
