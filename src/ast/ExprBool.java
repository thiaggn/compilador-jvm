package ast;

public final class ExprBool extends Expr
{
	public boolean valor;

	public ExprBool(boolean valor, parser.Token token)
	{
		super(token.beginLine, token.endColumn, SimboloTipo.Bool);
		this.valor = valor;
	}

	@Override
	public void print()
	{
		Print.valorDeCampo(this.valor + " (bool)");
	}
}
