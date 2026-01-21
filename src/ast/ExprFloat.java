package ast;

public class ExprFloat extends Expr 
{
	public float valor;

	public ExprFloat(parser.Token tok)
	{
		super(tok.beginLine, tok.endColumn, SimboloTipo.Float);
		this.valor = Float.parseFloat(tok.image);
	}

	@Override
	public void print()
	{
		Print.valorDeCampo(String.format("\u001B[95m%.2f\u001B[0m (%s)", this.valor, this.tipo.nome));
	}
}
