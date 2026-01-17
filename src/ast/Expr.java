package ast;


public abstract class Expr extends No
{
	public SimboloTipo tipo;

	protected Expr(int linha, int coluna, SimboloTipo tipo)
	{
		super(linha, coluna);
		this.tipo = tipo;
	} 
}
