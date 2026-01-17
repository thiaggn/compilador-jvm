package ast;

public class ExprId extends Expr
{
	public String  nome;
	public Simbolo simbolo;

	public ExprId(parser.Token token)
	{
		super(token.beginLine, token.beginColumn, SimboloTipo.Indeterminado);
		this.nome = token.image;
	}

	@Override
	public void print()
	{
		Print.valorDeCampo(String.format("\u001B[96m%s\u001B[0m (%d, %s)", this.simbolo.nome, this.simbolo.ref, this.simbolo.tipo.nome));
	}
}
