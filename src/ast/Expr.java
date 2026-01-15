package ast;


public abstract class Expr extends No
{
	public Tipo tipo;

	protected Expr(int linha, int coluna, Tipo tipo)
	{
		super(linha, coluna);
		this.tipo = tipo;
	} 
}
