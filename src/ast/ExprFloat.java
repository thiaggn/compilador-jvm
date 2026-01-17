package ast;


public class ExprFloat extends Expr 
{
	public double valor;

	public ExprFloat(parser.Token tok)
	{
		super(tok.beginLine, tok.endColumn, Tipo.Float);
		this.valor = Double.parseDouble(tok.image);
	}

	@Override
	public void print()
	{
		Print.valorDeCampo(String.format("\u001B[95m%.2f\u001B[0m (%s)", this.valor, this.tipo.nome));
	}
}
